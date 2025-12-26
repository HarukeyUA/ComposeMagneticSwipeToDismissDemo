package com.harukeyua.magneticswipetodismiss

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MagneticSwipeProperties(
    val alpha: Float = 1f,
    val offset: Float = 0f,
    val scaleY: Float = 1f,
    val scaleOriginY: Float = 0f,
    val roundness: Float = 0f,
)

@Stable
class MagneticSwipeController(
    private val scope: CoroutineScope,
    private val density: Density,
    private val onDismiss: (Any) -> Unit,
) {
    companion object {
        private val OFFSET_MULTIPLIERS = listOf(0.04f, 0.12f, 0.5f, 0.12f, 0.04f)
        private val OFFSET_CENTER_INDEX = OFFSET_MULTIPLIERS.size / 2

        private val ROUNDNESS_MULTIPLIERS = listOf(0.5f, 0.7f, 0.9f, 1.0f, 0.9f, 0.7f, 0.5f)
        private val ROUNDNESS_CENTER_INDEX = ROUNDNESS_MULTIPLIERS.size / 2

        private val SQUISH_MULTIPLIERS = listOf(0.03f, 0f, 0.03f)
        private val SQUISH_CENTER_INDEX = SQUISH_MULTIPLIERS.size / 2
        private const val LIST_REMOVAL_ANIMATION_OFFSET = 200L
    }

    private class ItemContext {
        val alpha = Animatable(1f)
        val animatableOffset = Animatable(0f)
        var dragOffset by mutableFloatStateOf(Float.NaN)

        val roundness = Animatable(0f)
        val scaleY = Animatable(1f)
        var scaleOriginY by mutableFloatStateOf(0f)

        var detachCorrection by mutableFloatStateOf(0f)
        var detachCorrectionVelocity by mutableFloatStateOf(0f)
        var detachCorrectionJob: Job? = null

        val currentOffset: Float
            get() = if (dragOffset.isNaN()) animatableOffset.value else dragOffset
    }

    private val items = mutableStateMapOf<Any, ItemContext>()

    private var itemKeys: List<Any> = emptyList()
    private var keyToIndexMap: Map<Any, Int> = emptyMap()

    private val detachThreshold = with(density) { 150.dp.toPx() }
    private val attachThreshold = with(density) { 100.dp.toPx() }
    private val dismissVelocityThreshold = with(density) { 500.dp.toPx() }

    private val detachSpec = spring<Float>(stiffness = 800f, dampingRatio = 0.95f)
    private val snapBackSpec = spring<Float>(stiffness = 550f, dampingRatio = 0.6f)
    private val roundnessSpec =
        spring<Float>(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioNoBouncy)
    private val slideDismissSpec = tween<Float>()
    private val alphaDismissSpec = tween<Float>(durationMillis = 200)
    private val squishSpec = spring<Float>(stiffness = 600f, dampingRatio = 0.5f)

    private var currentState = MagneticState.IDLE
    private var swipedKey: Any? = null
    private val isDetached
        get() = currentState == MagneticState.DETACHED

    private var currentRawOffset = 0f

    enum class MagneticState {
        IDLE,
        PULLING,
        DETACHED,
    }

    @Composable
    fun getItemProperties(key: Any): MagneticSwipeProperties {
        return items[key]?.let {
            MagneticSwipeProperties(
                alpha = it.alpha.value,
                offset = it.currentOffset,
                scaleY = it.scaleY.value,
                scaleOriginY = it.scaleOriginY,
                roundness = it.roundness.value,
            )
        } ?: MagneticSwipeProperties()
    }

    fun updateKeys(newKeys: List<Any>) {
        if (itemKeys == newKeys) return

        itemKeys = newKeys
        keyToIndexMap = newKeys.withIndex().associate { it.value to it.index }
        performCleanup(newKeys.toSet())
    }

    private fun performCleanup(activeKeys: Set<Any>) {
        val keysToRemove = items.keys - activeKeys
        keysToRemove.forEach { key -> items.remove(key) }
    }

    fun onDragStart(key: Any) {
        if (swipedKey != key) {
            reset()
        }
        swipedKey = key
        currentRawOffset = 0f
    }

    fun onDragBy(key: Any, delta: Float) {
        if (swipedKey != key) return
        currentRawOffset += delta
        onDrag(key, currentRawOffset)
    }

    private fun onDrag(key: Any, rawOffset: Float) {
        val index = keyToIndexMap[key] ?: return
        val absOffset = abs(rawOffset)

        handleStateTransitions(
            key = key,
            index = index,
            rawOffset = rawOffset,
            absOffset = absOffset,
        )
        applyMagneticTranslation(centerIndex = index, rawOffset = rawOffset, swipedKey = key)
        applyRoundness(centerIndex = index, rawOffset = rawOffset)
    }

    private fun handleStateTransitions(key: Any, index: Int, rawOffset: Float, absOffset: Float) {
        when {
            !isDetached && absOffset >= detachThreshold -> {
                currentState = MagneticState.DETACHED

                val item = items.getOrPut(key) { ItemContext() }
                val currentVisual = item.currentOffset
                val correctionValue = currentVisual - rawOffset

                animateCorrection(item = item, correctionValue = correctionValue, spec = detachSpec)

                animateSnapBack(centerIndex = index, velocity = 0f, snapNeighborsOnly = true)
            }

            isDetached && absOffset <= attachThreshold -> {
                currentState = MagneticState.PULLING

                forEachNeighbor(
                    centerIndex = index,
                    multipliers = OFFSET_MULTIPLIERS,
                    multiplierCenterIndex = OFFSET_CENTER_INDEX,
                ) { neighborKey, multiplier ->
                    val item = items.getOrPut(neighborKey) { ItemContext() }
                    val targetOffset = multiplier * rawOffset
                    val currentVisual = item.currentOffset
                    val correctionValue = currentVisual - targetOffset

                    animateCorrection(
                        item = item,
                        correctionValue = correctionValue,
                        spec = snapBackSpec,
                    )
                }
            }

            currentState == MagneticState.IDLE -> {
                currentState = MagneticState.PULLING
            }
        }
    }

    private fun animateCorrection(
        item: ItemContext,
        correctionValue: Float,
        spec: AnimationSpec<Float>,
    ) {
        item.detachCorrectionJob?.cancel()
        val initialVelocity = item.detachCorrectionVelocity

        item.detachCorrection = correctionValue

        item.detachCorrectionJob =
            scope.launch {
                AnimationState(initialValue = correctionValue, initialVelocity = initialVelocity)
                    .animateTo(targetValue = 0f, animationSpec = spec) {
                        item.detachCorrection = value
                        item.detachCorrectionVelocity = velocity
                    }
            }
    }

    private fun applyMagneticTranslation(centerIndex: Int, rawOffset: Float, swipedKey: Any) {
        forEachNeighbor(
            centerIndex = centerIndex,
            multipliers = OFFSET_MULTIPLIERS,
            multiplierCenterIndex = OFFSET_CENTER_INDEX,
        ) { neighborKey, multiplier ->
            val item = items.getOrPut(neighborKey) { ItemContext() }
            var targetTranslation = multiplier * rawOffset

            if (isDetached) {
                if (neighborKey == swipedKey) {
                    targetTranslation = rawOffset + item.detachCorrection
                    setOffset(neighborKey, targetTranslation)
                }
            } else {
                targetTranslation += item.detachCorrection
                setOffset(neighborKey, targetTranslation)
            }
        }
    }

    private fun applyRoundness(centerIndex: Int, rawOffset: Float) {
        val swipedMultiplier = OFFSET_MULTIPLIERS[OFFSET_CENTER_INDEX]
        val normalized = abs(swipedMultiplier * rawOffset) / detachThreshold
        val baseRoundness = if (isDetached) 1f else normalized.coerceIn(0f, 0.8f)

        forEachNeighbor(centerIndex, ROUNDNESS_MULTIPLIERS, ROUNDNESS_CENTER_INDEX) {
            neighborKey,
            multiplier ->
            if (isDetached && neighborKey != swipedKey) return@forEachNeighbor

            val targetRoundness = multiplier * baseRoundness
            setRoundness(neighborKey, targetRoundness)
        }
    }

    fun onDragEnd(key: Any, velocity: Float, layoutWidth: Float) {
        val index = keyToIndexMap[key]
        val isFling = abs(velocity) > dismissVelocityThreshold
        val shouldDismiss = isDetached || isFling

        if (index != null) {
            if (shouldDismiss) {
                animateDismiss(key = key, velocity = velocity, layoutWidth = layoutWidth)
            } else {
                animateSnapBack(index, velocity)
            }
        }

        swipedKey = null
        currentRawOffset = 0f
        currentState = MagneticState.IDLE
    }

    private fun animateSnapBack(
        centerIndex: Int,
        velocity: Float,
        snapNeighborsOnly: Boolean = false,
    ) {
        forEachNeighbor(centerIndex, OFFSET_MULTIPLIERS, OFFSET_CENTER_INDEX) { neighborKey, _ ->
            val isMainItem = neighborKey == itemKeys[centerIndex]
            if (snapNeighborsOnly && isMainItem) return@forEachNeighbor

            val item = items.getOrPut(neighborKey) { ItemContext() }
            scope.launch {
                launch {
                    if (!item.dragOffset.isNaN()) {
                        item.animatableOffset.snapTo(item.dragOffset)
                        item.dragOffset = Float.NaN // Turn off the override
                    }
                    item.animatableOffset.animateTo(
                        targetValue = 0f,
                        initialVelocity = if (isMainItem) velocity else 0f,
                        animationSpec = snapBackSpec,
                    )
                }
                launch { item.roundness.animateTo(0f, roundnessSpec) }
            }
        }
    }

    private fun animateDismiss(key: Any, velocity: Float, layoutWidth: Float) {
        val index = keyToIndexMap[key] ?: return
        val item = items.getOrPut(key) { ItemContext() }
        val currentVal = item.currentOffset
        val direction = if (abs(velocity) > 100f) sign(velocity) else sign(currentVal)
        val target = direction * (layoutWidth * 1.5f)

        animateSnapBack(centerIndex = index, velocity = velocity, snapNeighborsOnly = true)

        scope.launch {
            if (!item.dragOffset.isNaN()) {
                item.animatableOffset.snapTo(item.dragOffset)
                item.dragOffset = Float.NaN
            }

            launch {
                item.animatableOffset.animateTo(
                    targetValue = target,
                    initialVelocity = velocity,
                    animationSpec = slideDismissSpec,
                )
            }
            launch {
                item.alpha.animateTo(targetValue = 0f, animationSpec = alphaDismissSpec)
                animateNeighborSquish(index)
                onDismiss(key)
            }
        }
    }

    private fun animateNeighborSquish(dismissedIndex: Int) {
        forEachNeighbor(
            centerIndex = dismissedIndex,
            multipliers = SQUISH_MULTIPLIERS,
            multiplierCenterIndex = SQUISH_CENTER_INDEX,
        ) { neighborKey, multiplier ->
            if (multiplier == 0f) return@forEachNeighbor

            val neighborIndex = keyToIndexMap[neighborKey] ?: return@forEachNeighbor
            val neighborItem = items.getOrPut(neighborKey) { ItemContext() }
            val isBelow = neighborIndex > dismissedIndex
            val targetScale = 1f - multiplier

            scope.launch {
                delay(LIST_REMOVAL_ANIMATION_OFFSET)
                neighborItem.scaleOriginY = if (isBelow) 1f else 0f
                neighborItem.scaleY.animateTo(targetValue = targetScale, animationSpec = squishSpec)
                neighborItem.scaleY.animateTo(targetValue = 1f, animationSpec = squishSpec)
            }
        }
    }

    fun reset() {
        swipedKey = null
        currentRawOffset = 0f
        currentState = MagneticState.IDLE
    }

    private inline fun forEachNeighbor(
        centerIndex: Int,
        multipliers: List<Float>,
        multiplierCenterIndex: Int,
        action: (key: Any, multiplier: Float) -> Unit,
    ) {
        val startOffset = -multiplierCenterIndex

        val actualStart = maxOf(startOffset, -centerIndex)
        val actualEnd = minOf(multiplierCenterIndex, itemKeys.lastIndex - centerIndex)

        for (i in actualStart..actualEnd) {
            val neighborIndex = centerIndex + i
            val neighborKey = itemKeys[neighborIndex]
            val multiplier = multipliers[multiplierCenterIndex + i]
            action(neighborKey, multiplier)
        }
    }

    private fun setOffset(key: Any, value: Float) {
        val item = items.getOrPut(key) { ItemContext() }
        item.dragOffset = value
    }

    private fun setRoundness(key: Any, value: Float) {
        val item = items.getOrPut(key) { ItemContext() }
        scope.launch { item.roundness.snapTo(value) }
    }
}

@Composable
fun <T> rememberMagneticSwipeController(
    items: List<T>,
    keySelector: (T) -> Any,
    onDismiss: (Any) -> Unit,
): MagneticSwipeController {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val controller =
        remember(scope, density) {
            MagneticSwipeController(scope = scope, density = density, onDismiss = onDismiss)
        }

    LaunchedEffect(items, controller) {
        snapshotFlow { items.map(keySelector) }.collect { keys -> controller.updateKeys(keys) }
    }

    return controller
}

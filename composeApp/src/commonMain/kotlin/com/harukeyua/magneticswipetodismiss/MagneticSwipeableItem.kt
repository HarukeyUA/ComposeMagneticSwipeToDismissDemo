package com.harukeyua.magneticswipetodismiss

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun MagneticSwipeableItem(
    key: String,
    controller: MagneticSwipeController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val properties = controller.getItemProperties(key)
    var layoutWidth by remember { mutableStateOf(0f) }

    Box(
        modifier =
            modifier
                .onSizeChanged { layoutWidth = it.width.toFloat() }
                .pointerInput(key) {
                    val velocityTracker = VelocityTracker()
                    detectHorizontalDragGestures(
                        onDragStart = {
                            velocityTracker.resetTracking()
                            controller.onDragStart(key)
                        },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().x
                            controller.onDragEnd(key, velocity, layoutWidth)
                        },
                        onDragCancel = { controller.onDragEnd(key, 0f, layoutWidth) },
                        onHorizontalDrag = { change, dragAmount ->
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                            change.consume()
                            controller.onDragBy(key, dragAmount)
                        },
                    )
                }
                .graphicsLayer {
                    translationX = properties.offset
                    scaleY = properties.scaleY
                    shape = RoundedCornerShape(properties.roundness * 32.dp)
                    transformOrigin = TransformOrigin(0f, properties.scaleOriginY)
                    alpha = properties.alpha
                    clip = true
                }
    ) {
        content()
    }
}

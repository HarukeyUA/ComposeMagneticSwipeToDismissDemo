package com.harukeyua.magneticswipetodismiss

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector,
    val color: Color,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        val sampleData =
            listOf(
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Morning Glow",
                    message = "A new day, a softer you.",
                    time = "07:00 AM",
                    icon = Icons.Default.WbSunny,
                    color = Color(0xFF55CDFC),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Gentle Reminder",
                    message = "Drink some water.",
                    time = "07:20 AM",
                    icon = Icons.Default.WaterDrop,
                    color = Color(0xFF8ECDF4),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Coffee Break",
                    message = "Your comfort ritual awaits ☕",
                    time = "07:50 AM",
                    icon = Icons.Default.LocalCafe,
                    color = Color(0xFFF7A8B8),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Affirmation",
                    message = "You are allowed to take up space.",
                    time = "08:15 AM",
                    icon = Icons.Default.SelfImprovement,
                    color = Color(0xFFB19CD9),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "New Message",
                    message = "Hope today feels kind to you.",
                    time = "08:40 AM",
                    icon = Icons.Default.ChatBubbleOutline,
                    color = Color(0xFF7AA2F7),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Daily Intent",
                    message = "Move at your own pace.",
                    time = "09:05 AM",
                    icon = Icons.Default.Flag,
                    color = Color(0xFF9FA8DA),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Soft Check-in",
                    message = "How does your body feel right now?",
                    time = "09:30 AM",
                    icon = Icons.Default.FavoriteBorder,
                    color = Color(0xFFF48FB1),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Progress Note",
                    message = "Small steps still count.",
                    time = "10:00 AM",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    color = Color(0xFFCE93D8),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Kind Thought",
                    message = "You don’t owe anyone an explanation.",
                    time = "10:25 AM",
                    icon = Icons.Default.Psychology,
                    color = Color(0xFF81D4FA),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Pause Moment",
                    message = "Unclench your jaw. Breathe.",
                    time = "10:50 AM",
                    icon = Icons.Default.PauseCircleOutline,
                    color = Color(0xFF90CAF9),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Midday Light",
                    message = "You’re doing better than you think.",
                    time = "11:15 AM",
                    icon = Icons.Default.LightMode,
                    color = Color(0xFFFFAB91),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Lunch Time",
                    message = "Nourishment is care.",
                    time = "11:45 AM",
                    icon = Icons.Default.Restaurant,
                    color = Color(0xFFFFCC80),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Quiet Pride",
                    message = "Look how far you’ve come.",
                    time = "12:10 PM",
                    icon = Icons.Default.EmojiEvents,
                    color = Color(0xFFBA68C8),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Gentle Ping",
                    message = "No rush. You’re allowed to unfold.",
                    time = "12:40 PM",
                    icon = Icons.Default.NotificationsNone,
                    color = Color(0xFF64B5F6),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Inner Note",
                    message = "You deserve kindness — even from yourself.",
                    time = "01:05 PM",
                    icon = Icons.Default.EditNote,
                    color = Color(0xFFF06292),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Milestone",
                    message = "Another day lived honestly.",
                    time = "01:35 PM",
                    icon = Icons.Default.CheckCircleOutline,
                    color = Color(0xFF9575CD),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Breathing Space",
                    message = "Inhale. Exhale.",
                    time = "02:00 PM",
                    icon = Icons.Default.Air,
                    color = Color(0xFF4FC3F7),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Self Trust",
                    message = "You know who you’re becoming.",
                    time = "02:30 PM",
                    icon = Icons.Default.AutoAwesome,
                    color = Color(0xFFD1C4E9),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Soft Reminder",
                    message = "Rest is part of the process.",
                    time = "03:00 PM",
                    icon = Icons.Default.Bedtime,
                    color = Color(0xFF7986CB),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Check-in",
                    message = "Your feelings make sense.",
                    time = "03:30 PM",
                    icon = Icons.Default.Forum,
                    color = Color(0xFF81C784),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Evening Ease",
                    message = "Let the day loosen its grip.",
                    time = "04:30 PM",
                    icon = Icons.Default.Waves,
                    color = Color(0xFF4DB6AC),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Reflection",
                    message = "You showed up as yourself today.",
                    time = "05:00 PM",
                    icon = Icons.Default.FaceRetouchingNatural,
                    color = Color(0xFFF48FB1),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Tiny Win",
                    message = "That thing you did? It mattered.",
                    time = "05:30 PM",
                    icon = Icons.Default.StarOutline,
                    color = Color(0xFFB39DDB),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Home Time",
                    message = "You’re allowed to soften now.",
                    time = "06:00 PM",
                    icon = Icons.Default.Home,
                    color = Color(0xFFAED581),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Comfort Cue",
                    message = "Wrap yourself in something cozy.",
                    time = "06:30 PM",
                    icon = Icons.Default.Cake,
                    color = Color(0xFFFFAB91),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Affirmation",
                    message = "Your identity is not a phase.",
                    time = "07:00 PM",
                    icon = Icons.Default.Verified,
                    color = Color(0xFF64B5F6),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Gentle Pride",
                    message = "Becoming yourself is brave.",
                    time = "07:30 PM",
                    icon = Icons.Default.VolunteerActivism,
                    color = Color(0xFFF06292),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Skincare Time",
                    message = "Care for your future self.",
                    time = "08:00 PM",
                    icon = Icons.Default.Spa,
                    color = Color(0xFFCE93D8),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Quiet Moment",
                    message = "You don’t have to perform.",
                    time = "08:30 PM",
                    icon = Icons.Default.VisibilityOff,
                    color = Color(0xFF90CAF9),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Wind Down",
                    message = "The evening can be gentle.",
                    time = "09:00 PM",
                    icon = Icons.Default.NightsStay,
                    color = Color(0xFF7986CB),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Night Affirmation",
                    message = "You are real. You are valid.",
                    time = "09:30 PM",
                    icon = Icons.Default.Favorite,
                    color = Color(0xFFF48FB1),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Soft Closing",
                    message = "Thank yourself for today.",
                    time = "10:00 PM",
                    icon = Icons.Default.DoneAll,
                    color = Color(0xFF81C784),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Rest Mode",
                    message = "Tomorrow doesn’t need you yet.",
                    time = "10:20 PM",
                    icon = Icons.Default.DoNotDisturbOn,
                    color = Color(0xFF9575CD),
                ),
                NotificationItem(
                    id = Uuid.random().toString(),
                    title = "Good Night",
                    message = "Sleep well. You’re becoming.",
                    time = "10:40 PM",
                    icon = Icons.Default.NightsStay,
                    color = Color(0xFF64B5F6),
                ),
            )

        @OptIn(ExperimentalUuidApi::class)
        fun random() =
            sampleData.shuffled().map { item -> item.copy(id = Uuid.random().toString()) }
    }
}

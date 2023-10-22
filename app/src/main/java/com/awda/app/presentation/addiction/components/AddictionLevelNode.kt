package com.awda.app.presentation.addiction.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awda.app.data.home.models.AddictionLevel
import com.awda.app.presentation.common.components.NodeType
import com.awda.app.presentation.common.components.SingleNode
import kotlinx.coroutines.delay
import java.util.Locale

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AddictionLevelHeader(level: AddictionLevel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleNode(
            color = MaterialTheme.colorScheme.primary,
            nodeType = NodeType.SPACER,
            nodeSize = 50f,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(192.dp),
            isChecked = true,
            isDashed = false
        )

        Text(modifier = Modifier.padding(vertical = 48.dp),
            text = "From the study of your phone usage of last 7 days, it seems you fall into ${
                level.name.lowercase().replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            } category.")
    }
}

@Composable
fun AddictionLevelItem(level: AddictionLevel, userLevel: AddictionLevel) {
    val isPastUserLevel = userLevel.value >= level.value
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val nodeHeight = 288
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            SingleNode(
                color = if (isPastUserLevel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                nodeType = if (level == AddictionLevel.CHAMPION) NodeType.LAST else NodeType.MIDDLE,
                nodeSize = 50f,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(nodeHeight.dp),
                isChecked = false,
                isDashed = false
            )

            if (level == userLevel) {
                SingleNode(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    nodeType = NodeType.SPACER,
                    nodeSize = 50f,
                    modifier = Modifier
                        .padding(
                            start = 20.dp, end = 20.dp, top = (nodeHeight / 2).dp
                        )
                        .height((nodeHeight / 2).dp),
                    isChecked = false,
                    isDashed = false
                )
            }
        }

        AddictionLevelCard(level = level, userLevel = userLevel, isPastUserLevel = isPastUserLevel)
    }
}

@Composable
fun AddictionLevelCard(level: AddictionLevel, userLevel: AddictionLevel, isPastUserLevel: Boolean) {
    var animationRunning by remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition()
    val startColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val endColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    val animatedColor by infiniteTransition.animateColor(
        initialValue = startColor,
        targetValue = endColor,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = FastOutLinearInEasing),
            RepeatMode.Reverse,
        )
    )
    val position by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
    )

    val color = if (level == userLevel && animationRunning) {
        animatedColor
    } else {
        if (isPastUserLevel)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    }

    val yOffset = if (level == userLevel && animationRunning) {
        position.dp
    } else {
        0.dp
    }

    LaunchedEffect(level == userLevel) {
        delay(10000)
        animationRunning = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color)
            .padding(all = 24.dp)
            .offset(y = yOffset),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "${
            level.name.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
        } ${
            when (level) {
                AddictionLevel.ADDICTED -> {
                    "(More than 5.5 hr)"
                }

                AddictionLevel.OBSESSED -> {
                    "(4.5 hr - 5.5 hr)"
                }

                AddictionLevel.DEPENDENT -> {
                    "(3.5 hr - 4.5 hr)"
                }

                AddictionLevel.HABITUAL -> {
                    "(2.5 hr - 3.5 hr)"
                }

                AddictionLevel.ACHIEVER -> {
                    "(1 hr - 2.5 hr)"
                }

                AddictionLevel.CHAMPION -> {
                    "(Less than 1 hr)"
                }

                AddictionLevel.UNAVAILABLE -> {
                    "()"
                }
            }
        }", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)

        Row(modifier = Modifier.padding(top = 8.dp)) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = if (level.value >= index + 1) MaterialTheme.colorScheme.primary else Color.Gray,
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 16.dp), text = when (level) {
                AddictionLevel.ADDICTED -> {
                    "You're really attached to your phone, both mentally and physically. If it's not in your sight, you start feeling uneasy!"
                }

                AddictionLevel.OBSESSED -> {
                    "Even though it's a notch below, your mind often revolves around your phone. You discuss and stress over it as if it's a person!"
                }

                AddictionLevel.DEPENDENT -> {
                    "You heavily rely on your phone for small tasks. It still serves as your main support in many ways!"
                }

                AddictionLevel.HABITUAL -> {
                     "Your phone is your daily buddy and has become a habit. You can't go a day without it!"
                }

                AddictionLevel.ACHIEVER -> {
                    "Even though you control your phone use, you're still pretty eager to use it. Your achievement level is still high!"
                }

                AddictionLevel.CHAMPION -> {
                    "You've conquered the addiction. You're a savvy user of your smartphone, but watch out for a possible relapse!"
                }

                AddictionLevel.UNAVAILABLE -> {
                    ""
                }
            }
        )
    }
}
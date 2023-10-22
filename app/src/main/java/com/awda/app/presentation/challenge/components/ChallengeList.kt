package com.awda.app.presentation.challenge.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.awda.app.common.parseMillisToFullClock
import com.awda.app.common.parseTimestampToFormattedClock
import com.awda.app.common.parseTimestampToFullDate
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.challenge.models.AppChallengeState
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.util.Locale

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun ChallengeList(
    challenges: List<AppChallenge>,
    onEnableChallenge: (AppChallenge, Boolean) -> Unit,
    month: String
) {
    var showChallengeBottomSheet by remember { mutableStateOf(false) }
    var clickedChallenge by remember { mutableStateOf(AppChallenge()) }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = month)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            repeat(challenges.size) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .alpha(if (challenges[it].enabled) 1f else 0.6f)
                        .clickable(
                            indication = rememberRipple(bounded = false),
                            interactionSource = remember {
                                MutableInteractionSource()
                            }, onClick = {
                                showChallengeBottomSheet = true
                                clickedChallenge = challenges[it]
                            }
                        ),
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
                    leadingContent = {
                        Image(
                            modifier = Modifier.size(72.dp),
                            painter = rememberDrawablePainter(drawable = challenges[it].app.icon),
                            contentDescription = null
                        )
                    },
                    overlineContent = {
                        Text(text = parseTimestampToFormattedClock(challenges[it].occurrence))
                    },
                    headlineContent = { Text(challenges[it].app.name) },
                    supportingContent = { Text(parseMillisToFullClock(challenges[it].time)) },
                    trailingContent = {
                        Column {
                            Text(text = parseTimestampToFullDate(challenges[it].occurrence))
                            Spacer(modifier = Modifier.height(8.dp))
                            ChallengeState(challenges[it])
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                modifier = Modifier.width(96.dp),
                                text = if (challenges[it].repetitionPattern.default == DefaultRepetitionPattern.CUSTOM) {
                                    if (challenges[it].repetitionPattern.custom.isEmpty()) {
                                        DefaultRepetitionPattern.ONCE.capitalizedName()
                                    } else {
                                        challenges[it].repetitionPattern.custom.joinToString(", ") {
                                            it.name.lowercase()
                                                .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                                        }
                                    }
                                } else {
                                    challenges[it].repetitionPattern.default.name.lowercase(Locale.getDefault())
                                        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                                },
                                overflow = TextOverflow.Clip
                            )
                        }
                    }
                )
            }

            if (showChallengeBottomSheet) {
                ChallengeBottomSheet(
                    challenge = clickedChallenge,
                    onEnableChallenge = {
                        onEnableChallenge(clickedChallenge, it)
                        showChallengeBottomSheet = false
                    },
                    onDismiss = {
                        showChallengeBottomSheet = false
                    }
                )
            }
        }
    }
}

@Composable
fun ChallengeState(challenge: AppChallenge) {
    val state = challenge.state.name
    val color = when (challenge.state) {
        AppChallengeState.PENDING -> {
            MaterialTheme.colorScheme.primaryContainer
        }

        AppChallengeState.RUNNING -> {
            MaterialTheme.colorScheme.primary
        }

        AppChallengeState.PASSED -> {
            MaterialTheme.colorScheme.tertiaryContainer
        }

        AppChallengeState.LOST -> {
            MaterialTheme.colorScheme.error
        }
    }

    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(4.dp))
            .width(64.dp)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = state)
    }
}
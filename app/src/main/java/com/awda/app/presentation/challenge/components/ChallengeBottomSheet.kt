package com.awda.app.presentation.challenge.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awda.app.common.parseMillisToFullClock
import com.awda.app.common.parseTimestampToFormattedClock
import com.awda.app.common.parseTimestampToFullDate
import com.awda.app.data.challenge.models.AppChallenge
import com.awda.app.data.challenge.models.DefaultRepetitionPattern
import com.awda.app.presentation.common.components.MainActionButton
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Created by Abdelrahman Rizq
 */

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChallengeBottomSheet(
    challenge: AppChallenge,
    onEnableChallenge: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.6).dp),
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                modifier = Modifier,
                painter = rememberDrawablePainter(drawable = challenge.app.icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = challenge.app.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Created At: ")
                Text(text = parseTimestampToFullDate(challenge.createdAt))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Next Occurrence: ")
                Text(
                    text = "${
                        parseTimestampToFullDate(
                            challenge.nextOccurrence()
                        )
                    } ${
                        parseTimestampToFormattedClock(
                            challenge.nextOccurrence()
                        )
                    }"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Challenge Time: ")
                Text(text = parseMillisToFullClock(challenge.time))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Repetition: ")
                Text(
                    text = if (challenge.repetitionPattern.default == DefaultRepetitionPattern.CUSTOM) {
                        challenge.repetitionPattern.custom.joinToString(", ") {
                            it.name.lowercase()
                                .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                        }
                    } else {
                        challenge.repetitionPattern.default.name.lowercase(Locale.getDefault())
                            .replaceFirstChar { it.titlecase(Locale.getDefault()) }
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            MainActionButton(
                text = if (challenge.enabled) "Disable Upcoming Occurrences" else "Resume Challenge Occurrences",
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onEnableChallenge(!challenge.enabled)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
package com.awda.app.presentation.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.awda.app.common.parseMillisToClock
import com.awda.app.common.parseMillisToFormattedClock

/**
 * Created by Abdelrahman Rizq
 */


@Composable
fun UsageText(time: Long) {
    val (hours, minutes) = parseMillisToClock(time)
    val annotatedString = buildAnnotatedString {
        if (hours != 0) {
            withStyle(
                style = SpanStyle(
                    fontSize = 30.sp
                )
            ) {
                append(hours.toString())
            }

            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp
                )
            ) {
                append("h")
            }
        }

        if (minutes != 0) {
            withStyle(
                style = SpanStyle(
                    fontSize = 30.sp
                )
            ) {
                append(minutes.toString())
            }

            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp
                )
            ) {
                append("m")
            }
        }

        if (hours == 0 && minutes == 0) {
            withStyle(
                style = SpanStyle(
                    fontSize = 30.sp
                )
            ) {
                append(0.toString())
            }

            withStyle(
                style = SpanStyle(
                    fontSize = 16.sp
                )
            ) {
                append("m")
            }
        }
    }

    Text(
        text = annotatedString,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 16.sp
    )
}

@Composable
fun UsageComparisonText(timeToday: Long, timeYesterday: Long) {
    val less = timeYesterday > timeToday
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append(parseMillisToFormattedClock(if (less) (timeYesterday - timeToday) else (timeToday - timeYesterday)))
        }

        append("${if (less) " less " else " more "}than yesterday")
    }
    Text(
        text = annotatedString,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 16.sp
    )
}

@Composable
fun annotatedString(vararg parameters: Pair<String, Int>): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        parameters.forEach { (text, size) ->
            withStyle(SpanStyle(fontSize = size.sp)) {
                append(text)
            }
        }
    }
    return annotatedString
}
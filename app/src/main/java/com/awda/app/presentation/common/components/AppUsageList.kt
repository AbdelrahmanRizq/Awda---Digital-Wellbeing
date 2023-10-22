package com.awda.app.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awda.app.common.parseMillisToFormattedClock
import com.awda.app.data.home.models.CombinedAppUsage

import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppUsageList(
    apps: List<CombinedAppUsage>,
    maxAppDisplayCount: Int,
    scrollable: Boolean,
    sharedImage: @Composable ((Int) -> Unit)? = null,
    onItemClick: ((CombinedAppUsage) -> Unit)? = null
) {
    Column(
        modifier = if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier
    ) {
        repeat(if (apps.size <= 3) apps.size else maxAppDisplayCount) {
            if (onItemClick == null) {
                AppUsageItem(app = apps[it], it)
            } else {
                AppUsageItem(app = apps[it], it, sharedImage) {
                    onItemClick.invoke(apps[it])
                }
            }
        }
    }
}

@Composable
fun AppUsageItem(
    app: CombinedAppUsage,
    index: Int,
    sharedImage: @Composable ((Int) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val isHome = onClick == null
    val modifier = if (isHome) {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                indication = rememberRipple(bounded = false),
                interactionSource = remember {
                    MutableInteractionSource()
                }, onClick = {
                    onClick!!.invoke()
                }
            )
    }

    val color = when (index) {
        0 -> {
            MaterialTheme.colorScheme.primary
        }

        1 -> {
            MaterialTheme.colorScheme.secondary
        }

        else -> {
            MaterialTheme.colorScheme.tertiary
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (sharedImage == null) {
                Image(
                    modifier = Modifier.size(72.dp),
                    painter = rememberDrawablePainter(drawable = app.icon),
                    contentDescription = null
                )
            } else {
                sharedImage(index)
            }
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = app.name ?: "NA",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
        }

        if (isHome) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                )
                Text(
                    text = parseMillisToFormattedClock(app.totalUsage),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }
        } else {
            Text(
                text = parseMillisToFormattedClock(app.totalUsage),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
        }

    }
}
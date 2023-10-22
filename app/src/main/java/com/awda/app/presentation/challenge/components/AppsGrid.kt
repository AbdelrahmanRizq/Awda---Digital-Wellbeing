package com.awda.app.presentation.challenge.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awda.app.data.challenge.models.InstalledApp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppsGrid(
    apps: List<InstalledApp>,
    onSelect: (InstalledApp) -> Unit,
    onShowMoreApps: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Select App",
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyHorizontalGrid(
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .height(250.dp)
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp),
            rows = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val maxCount = 10
            val shouldFill = apps.size >= maxCount
            items(if (shouldFill) maxCount else apps.size) {

                if ((shouldFill && it == maxCount - 1) || (!shouldFill && it == apps.size - 1)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clickable(
                                indication = rememberRipple(bounded = false),
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }, onClick = {
                                    onShowMoreApps()
                                }
                            ),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null
                        )

                        Text(
                            text = "Show more",
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp
                        )
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = rememberRipple(bounded = false),
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }, onClick = {
                                    onSelect(apps[it])
                                }
                            )
                            .border(
                                if (apps[it].selected) {
                                    BorderStroke(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    )
                                } else {
                                    BorderStroke(0.dp, Color.Transparent)
                                },
                                RoundedCornerShape(4.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(52.dp),
                                painter = rememberDrawablePainter(drawable = apps[it].icon),
                                contentDescription = null
                            )

                            Text(
                                modifier = Modifier.width(60.dp),
                                textAlign = TextAlign.Center,
                                text = apps[it].name,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.awda.app.presentation.usage

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.awda.app.data.home.models.CombinedAppUsage
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateBounds
import com.skydoves.orbital.rememberMovableContentOf

/**
 * Created by Abdelrahman Rizq
 */

enum class AppUsageScreen {
    LIST,
    DETAILS
}

@Composable
fun AppUsageScreens(
    viewModel: AppUsageViewModel,
    appUsage: List<CombinedAppUsage>
) {
    LaunchedEffect(Unit) {
        viewModel.setAppUsage(appUsage)
    }

    Orbital {
        var screen by rememberSaveable { mutableStateOf(AppUsageScreen.LIST) }
        var selectedApp by remember { mutableStateOf<CombinedAppUsage?>(null) }
        val images by remember { mutableStateOf(mutableListOf<@Composable () -> Unit>()) }

        val sizeAnim = spring<IntSize>(stiffness = Spring.StiffnessLow)
        val positionAnim = spring<IntOffset>(stiffness = Spring.StiffnessLow)
        val apps = viewModel.appUsageState

        val image: @Composable (Int) -> @Composable () -> Unit = {
            images.add(rememberMovableContentOf(it) {
                Image(
                    modifier = Modifier
                        .animateBounds(
                            modifier = if (screen == AppUsageScreen.LIST) {
                                Modifier.size(72.dp)
                            } else {
                                Modifier.size(96.dp)
                            },
                            sizeAnimationSpec = sizeAnim,
                            positionAnimationSpec = positionAnim,
                        ),
                    painter = rememberDrawablePainter(drawable = apps.value[it].icon),
                    contentDescription = null
                )
            })
            images[it]
        }


        if (screen == AppUsageScreen.LIST) {
            AppUsageListScreen(
                viewModel = viewModel,
                sharedImage = { index ->
                    image(index)()
                },
                navigateToDetailsScreen = {
                    selectedApp = it
                    screen = AppUsageScreen.DETAILS
                }
            )
        } else {
            AppUsageScreen(
                viewModel = viewModel,
                app = selectedApp!!,
                sharedImage = {
                    images[apps.value.indexOf(selectedApp)]()
                }, navigateToListScreen = {
                    screen = AppUsageScreen.LIST
                }
            )
        }
    }
}
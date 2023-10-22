package com.awda.app.presentation.intro.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppIntroAnimation(name: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(name))
    LottieAnimation(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 3),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}
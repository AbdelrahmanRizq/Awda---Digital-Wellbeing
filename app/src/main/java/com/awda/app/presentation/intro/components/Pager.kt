package com.awda.app.presentation.intro.components

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.awda.app.common.isPermissionGranted
import com.awda.app.presentation.common.components.MainActionButton
import com.awda.app.presentation.common.components.PermissionButton
import com.awda.app.presentation.common.components.TimeLimitSlider
import com.awda.app.presentation.intro.AppIntroViewModel

/**
 * Created by Abdelrahman Rizq
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppIntroPager(state: PagerState, pages: List<@Composable () -> Unit>) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
        state = state
    ) { page ->
        pages[page]()
    }
}

@Composable
fun AppIntroPage(
    viewModel: AppIntroViewModel,
    text: String,
    permission: String?,
    timePage: Boolean = false,
    onTimeSet: (Long) -> Unit = {}
) {
    val context = LocalContext.current as Activity
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
    var permissionGranted by remember {
        mutableStateOf(permission?.let {
            context.isPermissionGranted(
                it
            )
        })
    }
    var openTimePopup by remember { mutableStateOf(false) }

    LaunchedEffect(permissionGranted, lifecycleState.value) {
        if (permissionGranted == false && permission != null) {
            permissionGranted = context.isPermissionGranted(permission)
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (permission != null && permissionGranted == false) {
            PermissionButton(context, permission)
        }

        if (permission != null && permissionGranted == true) {
            Text(
                text = "Permission granted",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (permission == null && timePage) {
            MainActionButton(text = "Set Usage Goal", onClick = {
                openTimePopup = true
            })

            if (openTimePopup) {
                TimeLimitSlider(
                    time = viewModel.usageTimeLimitState.value,
                    onDismissRequest = {
                        openTimePopup = false
                        onTimeSet(it)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagesIndicator(modifier: Modifier, state: PagerState, pageCount: Int) {
    Row(
        modifier
            .height(32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val color =
                if (state.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)

            )
        }
    }
}
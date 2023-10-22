package com.awda.app.presentation.settings.components

import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import com.awda.app.common.isAccessibilityServiceRunning
import com.awda.app.common.openPermissionSettings
import com.awda.app.domain.service.WebLockerService
import com.awda.app.presentation.intro.components.observeAsState

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AccessibilitySetting(
    name: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var isServiceRunning by mutableStateOf(context.isAccessibilityServiceRunning(name))
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()

    LaunchedEffect(
        key1 = isServiceRunning,
        key2 = lifecycleState.value,
        block = {
            isServiceRunning =
                context.isAccessibilityServiceRunning(name)
        }
    )

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .clickable(
                    indication = rememberRipple(bounded = false),
                    interactionSource = remember {
                        MutableInteractionSource()
                    }, onClick = {
                        onClick()
                    }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (name == WebLockerService::class.java.name) "Blocked Websites" else "Secure Apps")
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (name == WebLockerService::class.java.name) "Web Locker Service Status" else "Secure Apps Service Status")
            Switch(
                checked = isServiceRunning,
                onCheckedChange = {
                    context.openPermissionSettings(
                        Settings.ACTION_ACCESSIBILITY_SETTINGS,
                        highlightPackage = false
                    )
                }
            )
        }
    }
}
package com.awda.app.presentation.common.components

import android.app.Activity
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.awda.app.common.Constants
import com.awda.app.common.openPermissionSettings
import com.awda.app.common.requestPermission

/**
 * Created by Abdelrahman Rizq
 */


@Composable
fun PermissionButton(
    context: Activity,
    permission: String
) {
    MainActionButton(text = "Grant Permission", onClick = {
        when (permission) {
            Constants.PACKAGE_USAGE_PERMISSION -> {
                context.openPermissionSettings(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            }

            Constants.OVERLAY_PERMISSION -> {
                context.openPermissionSettings(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            }

            else -> {
                context.requestPermission(permission)
            }
        }
    })
}

@Composable
fun MainActionButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f),
        contentPadding = PaddingValues(16.dp),
        enabled = enabled,
        onClick = { onClick() },
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            containerColor = color,
            disabledContentColor = textColor
        )
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun BorderlessTransparentButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        enabled = enabled
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
    }
}
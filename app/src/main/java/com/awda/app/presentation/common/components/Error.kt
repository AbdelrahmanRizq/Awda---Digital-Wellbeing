package com.awda.app.presentation.common.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.awda.app.common.isPermissionGranted
import com.awda.app.presentation.intro.components.observeAsState

/**
 * Created by Abdelrahman Rizq
 */


@Composable
fun ErrorPopup(
    header: String,
    message: String,
    permission: String? = null,
    onDismissRequest: () -> Unit
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

    LaunchedEffect(permissionGranted, lifecycleState.value) {
        if (permissionGranted == false && permission != null) {
            permissionGranted = context.isPermissionGranted(permission)
        }
    }

    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties(focusable = true),
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )

                Text(
                    text = header,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )

                if (permission != null && permissionGranted == false) {
                    PermissionButton(context = context, permission = permission)
                }

                if (permission != null && permissionGranted == true) {
                    Text(
                        text = "Permission granted",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    BorderlessTransparentButton(
                        modifier = Modifier,
                        text = "DONE",
                        enabled = true
                    ) {
                        onDismissRequest()
                    }
                }
            }
        }
    }
}
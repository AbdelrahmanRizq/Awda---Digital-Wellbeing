package com.awda.app.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.awda.app.data.challenge.models.InstalledApp

/**
 * Created by Abdelrahman Rizq
 */

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BlockedAppBottomSheet(
    apps: List<InstalledApp>,
    onSelect: (Boolean, InstalledApp) -> Unit,
    onChange: (InstalledApp) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedApps = remember(apps) {
        apps.filter { it.selected }.toMutableList()
    }
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {

        BlockedAppSearchBar(
            apps = apps,
            onSelect = { selected, app ->
                onSelect(selected, app)
            },
            onChange = {
                onChange(it)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .height(this@BoxWithConstraints.maxHeight),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                items(apps.size, key = { apps[it].pName }) {
                    BlockedAppItem(
                        app = apps[it],
                        selected = selectedApps.contains(apps[it]),
                        onSelect = { selected ->
                            if (selected) {
                                selectedApps.add(apps[it])
                            } else {
                                selectedApps.remove(apps[it])
                            }
                            onSelect(selected, apps[it])
                        },
                        onChange = {
                            onChange(it)
                        }
                    )
                }
            }
        }
    }
}
package com.awda.app.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.awda.app.presentation.home.Destination

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun BottomBar(
    destinations: List<Destination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (route: String) -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(BottomNavCurve())
                .height(96.dp),
            tonalElevation = 0.dp,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ) {
            destinations.forEachIndexed { index, destination ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == destination.route } == true

                val topPadding = if (index == 0 || index == 2) {
                    48.dp
                } else {
                    0.dp
                }

                NavigationBarItem(
                    modifier = Modifier.padding(top = topPadding),
                    selected = selected,
                    onClick = { onNavigateToDestination(destination.route) },
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            imageVector = destination.icon,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}
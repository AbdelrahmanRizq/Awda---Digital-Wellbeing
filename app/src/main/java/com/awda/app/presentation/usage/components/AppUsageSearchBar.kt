package com.awda.app.presentation.usage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.awda.app.common.parseMillisToFormattedClock
import com.awda.app.data.home.models.CombinedAppUsage
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by Abdelrahman Rizq
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUsageSearchBar(
    apps: List<CombinedAppUsage>,
    onSelected: (CombinedAppUsage) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = -1f },
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            query = text,
            onQueryChange = { text = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("Search apps") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        ) {
            val suggestions = if (text.isEmpty()) {
                emptyList()
            } else {
                apps.filter { it.name!!.contains(text, ignoreCase = true) }.toList()
            }

            repeat(suggestions.size) {
                val suggestion = suggestions[it]
                AppUsageSuggestionItem(suggestion) {
                    text = suggestion.name!!
                    active = false
                    onSelected(it)
                }
            }
        }
    }
}

@Composable
fun AppUsageSuggestionItem(
    suggestion: CombinedAppUsage,
    onSelected: (CombinedAppUsage) -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable(
                indication = rememberRipple(bounded = false),
                interactionSource = remember {
                    MutableInteractionSource()
                }, onClick = {
                    onSelected(suggestion)
                }
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
        leadingContent = {
            Image(
                modifier = Modifier.size(72.dp),
                painter = rememberDrawablePainter(drawable = suggestion.icon),
                contentDescription = null
            )
        },
        headlineContent = { Text(suggestion.name!!) },
        supportingContent = { Text(parseMillisToFormattedClock(suggestion.totalUsage)) }
    )
}


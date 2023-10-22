package com.awda.app.presentation.challenge.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.awda.app.data.challenge.models.InstalledApp

/**
 * Created by Abdelrahman Rizq
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    apps: List<InstalledApp>,
    onClick: (InstalledApp) -> Unit,
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
                apps.filter { it.name.contains(text, ignoreCase = true) }.toList()
            }

            repeat(suggestions.size) {
                val suggestion = suggestions[it]
                AppItem(suggestion) {
                    text = suggestion.name
                    active = false
                    onClick(it)
                }
            }
        }
    }
}


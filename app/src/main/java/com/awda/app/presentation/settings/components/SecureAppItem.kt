package com.awda.app.presentation.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.awda.app.data.settings.models.SecureApp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun SecureAppItem(
    app: SecureApp,
    selected: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(
                if (selected) {
                    BorderStroke(
                        1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    BorderStroke(0.dp, Color.Transparent)
                },
                RoundedCornerShape(24.dp)
            )
    ) {
        ListItem(
            modifier = Modifier
                .clickable(
                    onClick = {
                        onSelect(!selected)
                    }
                )
                .fillMaxWidth(),
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            leadingContent = {
                Image(
                    modifier = Modifier.size(72.dp),
                    painter = rememberDrawablePainter(drawable = app.info.icon),
                    contentDescription = null
                )
            },
            headlineContent = { Text(app.info.name) }
        )
    }
}
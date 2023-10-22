package com.awda.app.presentation.addiction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.awda.app.data.home.models.AddictionLevel
import com.awda.app.presentation.addiction.components.AddictionLevelHeader
import com.awda.app.presentation.addiction.components.AddictionLevelItem

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AddictionLevelScreen(level: AddictionLevel) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(vertical = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(24.dp))
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        AddictionLevelHeader(level)
                        AddictionLevelItem(level = AddictionLevel.ADDICTED, userLevel = level)
                        AddictionLevelItem(level = AddictionLevel.OBSESSED, userLevel = level)
                        AddictionLevelItem(level = AddictionLevel.DEPENDENT, userLevel = level)
                        AddictionLevelItem(level = AddictionLevel.HABITUAL, userLevel = level)
                        AddictionLevelItem(level = AddictionLevel.ACHIEVER, userLevel = level)
                        AddictionLevelItem(level = AddictionLevel.CHAMPION, userLevel = level)

                    }
                }
            }
        }
    )
}
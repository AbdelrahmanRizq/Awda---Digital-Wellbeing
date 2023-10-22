package com.awda.app.presentation.timeline.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.awda.app.common.parseMillisToFullClock
import com.awda.app.common.parseTimestampToFormattedClock
import com.awda.app.data.home.models.TimelineNode
import com.awda.app.presentation.common.components.ErrorPopup
import com.awda.app.presentation.common.components.NodeType
import com.awda.app.presentation.common.components.SingleNode

import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppUsageTimeline(timeline: Map<Long, List<TimelineNode>>?) {
    var expandedTimeline by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if (timeline != null) {
            repeat(timeline.size) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = rememberRipple(bounded = false),
                            interactionSource = remember {
                                MutableInteractionSource()
                            }, onClick = {
                                expandedTimeline = if (expandedTimeline == it) -1 else it
                            }
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = parseTimestampToFormattedClock(timeline.keys.elementAt(it)),
                        modifier = Modifier.weight(1f)
                    )

                    SingleNode(
                        color = MaterialTheme.colorScheme.primary,
                        nodeType = if (it == 0) NodeType.FIRST else if (it == timeline.size - 1) NodeType.LAST else NodeType.MIDDLE,
                        nodeSize = 50f,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .weight(1f)
                            .height(96.dp),
                        isChecked = true,
                        isDashed = false
                    )

                    Row(modifier = Modifier.weight(1f)) {
                        repeat(timeline.values.elementAt(it).size) { index ->
                            val node = timeline.values.elementAt(it)[index]
                            if (index != 0) {
                                val previousApp =
                                    timeline.values.elementAt(it)[index - 1]
                                if (node.appUsage.pName != previousApp.appUsage.pName) {
                                    TimelineAppImage(node)
                                }
                            } else {
                                TimelineAppImage(node)
                            }
                        }
                    }
                }

                if (expandedTimeline == it) {
                    TimelineDetails(
                        timeline.values.elementAt(it)
                            .sortedBy { it.timestamp })
                }
            }
        } else {
            ErrorPopup(
                header = "Unknown Error",
                message = "Oops! Something went wrong.."
            ) {}
        }
    }
}

@Composable
fun TimelineDetails(timeline: List<TimelineNode>) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        var currentNode = timeline[0]
        var usageSum = currentNode.usageTime
        var nodeIndex = 1
        while (nodeIndex < timeline.size) {
            val nextNode = timeline[nodeIndex]
            if (currentNode.appUsage.pName == nextNode.appUsage.pName && nodeIndex != timeline.size - 1) {
                usageSum += nextNode.usageTime
                nodeIndex++
                continue
            }
            TimelineNode(node = currentNode, appUsageSum = usageSum)
            currentNode = nextNode
            usageSum = nextNode.usageTime
            nodeIndex++
        }

        if (timeline.size > 1 && timeline[nodeIndex - 1].appUsage.pName != timeline[nodeIndex - 2].appUsage.pName) {
            TimelineNode(node = currentNode, appUsageSum = usageSum)
        }
    }
}

@Composable
fun TimelineNode(node: TimelineNode, appUsageSum: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = parseTimestampToFormattedClock(node.timestamp),
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 25.dp),
            contentAlignment = Alignment.Center
        ) {
            SingleNode(
                color = MaterialTheme.colorScheme.secondary,
                nodeType = NodeType.SPACER,
                nodeSize = 25f,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(96.dp),
                isChecked = true,
                isDashed = false
            )
            TimelineAppImage(
                node = node,
                size = 48.dp
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = node.appUsage.name ?: "NA")
            Text(parseMillisToFullClock(appUsageSum))
        }
    }
}

@Composable
fun TimelineAppImage(node: TimelineNode, size: Dp = 24.dp) {
    Image(
        modifier = Modifier.size(size),
        painter = rememberDrawablePainter(
            drawable = node.appUsage.icon
        ),
        contentDescription = null
    )
}
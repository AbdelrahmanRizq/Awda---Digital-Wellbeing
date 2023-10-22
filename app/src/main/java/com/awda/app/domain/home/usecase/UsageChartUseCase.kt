package com.awda.app.domain.home.usecase

import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.HomeRepository
import com.awda.app.data.home.models.TimelineNode
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */


class UsageChartUseCase(private val repository: HomeRepository) :
    AsyncUseCase<ChartTimeBase, Pair<Resource<List<BarData>>, Map<Long, List<TimelineNode>>?>> {
    override suspend fun execute(params: ChartTimeBase): Pair<Resource<List<BarData>>, Map<Long, List<TimelineNode>>?> {
        val (chart, timelines) = repository.getUsageChartData(params).first()
        if (timelines != null) {
            fun timelineMap(timelines: MutableList<MutableList<TimelineNode>>): Map<Long, List<TimelineNode>> {
                val timelineMap = mutableMapOf<Long, MutableList<TimelineNode>>()

                for (timeline in timelines) {
                    for (node in timeline) {
                        val start = node.start

                        if (!timelineMap.containsKey(start)) {
                            timelineMap[start] = mutableListOf()
                        }

                        timelineMap[start]!!.add(node)
                    }
                }

                return timelineMap
            }
            return Pair(chart, timelineMap(timelines))
        }
        return Pair(chart, null)
    }
}
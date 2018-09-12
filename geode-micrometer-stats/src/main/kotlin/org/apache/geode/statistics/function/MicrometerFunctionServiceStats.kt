package org.apache.geode.statistics.function

import org.apache.geode.internal.cache.execute.FunctionServiceStats
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementer
import org.apache.geode.stats.common.statistics.Statistics
import org.apache.geode.stats.common.statistics.StatisticsFactory

class MicrometerFunctionServiceStats(statisticsFactory: StatisticsFactory, private val identifier: String) :
        MicrometerMeterGroup(statisticsFactory = statisticsFactory, groupName = identifier), FunctionServiceStats, MicrometerStatsImplementer {
    override fun initializeStaticMeters() {
    }

    override fun getFunctionExecutionsCompleted(): Int = 0

    override fun incFunctionExecutionsCompleted() {

    }

    override fun getFunctionExecutionCompleteProcessingTime(): Long = 0

    override fun getFunctionExecutionsRunning(): Int = 0

    override fun incFunctionExecutionsRunning() {

    }

    override fun getResultsSentToResultCollector(): Int = 0

    override fun incResultsReturned() {

    }

    override fun getResultsReceived(): Int = 0

    override fun incResultsReceived() {

    }

    override fun getFunctionExecutionCalls(): Int = 0

    override fun incFunctionExecutionCalls() {

    }

    override fun getFunctionExecutionHasResultCompleteProcessingTime(): Int = 0

    override fun getFunctionExecutionHasResultRunning(): Int = 0

    override fun incFunctionExecutionHasResultRunning() {

    }

    override fun getFunctionExecutionExceptions(): Int = 0

    override fun incFunctionExecutionExceptions() {

    }

    override fun startTime(): Long = 0L

    override fun startFunctionExecution(haveResult: Boolean) {

    }

    override fun endFunctionExecution(start: Long, haveResult: Boolean) {

    }

    override fun endFunctionExecutionWithException(haveResult: Boolean) {

    }

    override fun close() {

    }

    override fun getStats(): Statistics? = null

}
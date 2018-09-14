/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
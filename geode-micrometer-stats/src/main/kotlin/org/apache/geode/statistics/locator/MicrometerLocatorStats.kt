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
package org.apache.geode.statistics.locator

import org.apache.geode.stats.common.distributed.internal.LocatorStats
import org.apache.geode.stats.common.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementer
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerLocatorStats(statisticsFactory: StatisticsFactory, private val locatorName: String) :
        MicrometerMeterGroup(statisticsFactory,"LocatorStats-$locatorName"), LocatorStats, MicrometerStatsImplementer {

    override fun getGroupTags(): Array<String> = arrayOf("locatorName", locatorName)

    override fun initializeStaticMeters() {
        registerMeter(locatorKnownMeter)
        registerMeter(locatorClientRequestMeter)
        registerMeter(locatorClientResponseMeter)
        registerMeter(locatorKnownServerMeter)
        registerMeter(locatorRequestInProgressMeter)
        registerMeter(locatorClientRequestTimer)
        registerMeter(locatorClientResponseTimer)
        registerMeter(locatorServerLoadUpdateMeter)
    }

    private val locatorKnownMeter = GaugeStatisticMeter("locator.count", "Number of locators known to this locator")
    private val locatorClientRequestMeter = CounterStatisticMeter("locator.client.request", "Number of requests this locator has received from clients")
    private val locatorClientResponseMeter = CounterStatisticMeter("locator.client.response", "Number of responses this locator has sent to clients")
    private val locatorKnownServerMeter = GaugeStatisticMeter("locator.server.count", "Number of servers this locator knows about")
    private val locatorRequestInProgressMeter = GaugeStatisticMeter("locator.request.inprogress", "The number of location requests currently being processed by the thread pool.")
    private val locatorClientRequestTimer = TimerStatisticMeter("locator.request.time", "Time spent processing server location requests", meterUnit = "nanoseconds")
    private val locatorClientResponseTimer = TimerStatisticMeter("locator.response.time", "Time spent sending location responses to clients", meterUnit = "nanoseconds")
    private val locatorServerLoadUpdateMeter = CounterStatisticMeter("locator.server.load.update.count", "Total number of times a server load update has been received.")

    override fun hookupStats(name: String?) {
        //noop
    }

    override fun close() {
        //noop
    }

    override fun setServerCount(serverCount: Int) {
        locatorKnownServerMeter.setValue(serverCount)
    }

    override fun setLocatorCount(locatorCount: Int) {
        locatorKnownMeter.setValue(locatorCount)
    }

    override fun endLocatorRequest(startTime: Long) {
        locatorClientRequestMeter.increment()
        locatorClientRequestTimer.recordValue(NOW_NANOS - startTime)
    }

    override fun endLocatorResponse(startTime: Long) {
        locatorClientResponseMeter.increment()
        locatorClientResponseTimer.recordValue(NOW_NANOS - startTime)
    }


    override fun setLocatorRequests(locatorRequests: Long) {
        locatorClientRequestMeter.increment(locatorRequests)
    }

    override fun setLocatorResponses(locatorResponses: Long) {
        locatorClientResponseMeter.increment(locatorResponses)
    }

    override fun setServerLoadUpdates(serverLoadUpdates: Long) {
        locatorServerLoadUpdateMeter.increment(serverLoadUpdates)
    }

    override fun incServerLoadUpdates() {
        locatorServerLoadUpdateMeter.increment()
    }

    override fun incRequestInProgress(threads: Int) {
        locatorRequestInProgressMeter.increment(threads)
    }
}
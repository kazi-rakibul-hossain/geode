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
package org.apache.geode.statistics.cache

import org.apache.geode.stats.common.internal.cache.tier.sockets.CCUStats
import org.apache.geode.stats.common.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup

class MicrometerCCUStatsImpl internal constructor(statisticsFactory: StatisticsFactory, private val serverLocation: String) :
        CCUStats, MicrometerMeterGroup(statisticsFactory, "CacheClientUpdaterStats-$serverLocation") {

    override fun getGroupTags(): Array<String> = arrayOf("serverLocation", serverLocation)

    private val clientUpdaterReceivedBytesMeter = CounterStatisticMeter("client.updater.received.bytes.count", "Total number of bytes received from the server.", meterUnit = "bytes")
    private val clientUpdateMessagesReceivedMeter = GaugeStatisticMeter("client.updater.received.messages.count", "Current number of message being received off the network or being processed after reception.")
    private val clientUpdateMessagesReceivedBytesMeter = GaugeStatisticMeter("client.updater.received.messages.bytes", "Current number of bytes consumed by messages being received or processed.", meterUnit = "bytes")

    override fun initializeStaticMeters() {
        registerMeter(clientUpdaterReceivedBytesMeter)
        registerMeter(clientUpdateMessagesReceivedMeter)
        registerMeter(clientUpdateMessagesReceivedBytesMeter)
    }

    override fun close() {
        //noop
    }

    override fun incReceivedBytes(bytes: Long) {
        clientUpdaterReceivedBytesMeter.increment(bytes)
    }

    override fun incSentBytes(v: Long) {
        // noop since we never send messages
    }

    private fun incMessagesBytesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedBytesMeter.increment(bytes)
    }

    private fun decMessagesBytesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedBytesMeter.decrement(bytes)
    }

    override fun incMessagesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedMeter.increment()
        incMessagesBytesBeingReceived(bytes)
    }

    override fun decMessagesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedMeter.decrement()
        decMessagesBytesBeingReceived(bytes)
    }
}

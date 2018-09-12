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
package org.apache.geode.statistics.eviction

import org.apache.geode.stats.common.internal.cache.eviction.EvictionStats
import org.apache.geode.stats.common.statistics.Statistics
import org.apache.geode.stats.common.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter

import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup

abstract class MicrometerEvictionStatsImpl(statisticsFactory: StatisticsFactory, private val regionName: String, private val groupName: String = "EvictionStats-$regionName") :
        MicrometerMeterGroup(statisticsFactory,groupName), EvictionStats {


    override fun getGroupTags(): Array<String> = arrayOf("regionName", regionName)

    override fun initializeStaticMeters() {
        registerMeter(evictionLRUEvictionCountMeter)
        registerMeter(evictionLRUDestroyCountMeter)
        registerMeter(evictionLRUEvaluationsCountMeter)
        registerMeter(evictionLRUGreedyReturnsCountMeter)
    }

    private val evictionLRUEvictionCountMeter = CounterStatisticMeter("eviction.lru.evictions.count", "Number of total entry evictions triggered by LRU.")
    private val evictionLRUDestroyCountMeter = CounterStatisticMeter("eviction.lru.destroy.count", "Number of entries destroyed in the region through both destroy cache operations and eviction.")
    private val evictionLRUEvaluationsCountMeter = CounterStatisticMeter("eviction.lru.evaluation.count", "Number of entries evaluated during LRU operations.")
    private val evictionLRUGreedyReturnsCountMeter = CounterStatisticMeter("eviction.lru.greedyreturns.count", "Number of non-LRU entries evicted during LRU operations")

    override fun getStatistics(): Statistics? {
        //this is a noop for Micrometer stats
        return null
    }

    override fun close() {
        //this is a noop for Micrometer stats
    }

    override fun incEvictions() {
        evictionLRUEvictionCountMeter.increment()
    }

    override fun incEvaluations(delta: Long) {
        evictionLRUEvaluationsCountMeter.increment(delta)
    }

    override fun incDestroys() {
        evictionLRUDestroyCountMeter.increment()
    }

    override fun incGreedyReturns(delta: Long) {
        evictionLRUGreedyReturnsCountMeter.increment(delta)
    }
}
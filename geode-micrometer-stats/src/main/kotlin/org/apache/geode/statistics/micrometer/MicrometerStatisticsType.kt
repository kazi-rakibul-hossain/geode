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
package org.apache.geode.statistics.micrometer

import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerStatisticMeter
import org.apache.geode.stats.common.statistics.StatisticDescriptor
import org.apache.geode.stats.common.statistics.StatisticsFactory
import org.apache.geode.stats.common.statistics.StatisticsType

class MicrometerStatisticsType(private val name: String,
                               private val description: String,
                               private val statistics: Array<StatisticDescriptor>,
                               private val statisticsFactory: StatisticsFactory) :
        StatisticsType, MicrometerMeterGroup(statisticsFactory = statisticsFactory, groupName = name) {

    override fun initializeStaticMeters() {
        //noop
    }

    private val statsArray: Array<StatisticDescriptor> = statistics
    //    private val statsIdToNameMap = hashMapOf<Int, String>()
    private val statsNameToIdMap = hashMapOf<String, Int>()

    init {
        statistics.forEachIndexed { index, statisticDescriptor ->
            run {
                statisticDescriptor as MicrometerStatisticMeter
                statisticDescriptor.meterId = index
                statsNameToIdMap[statisticDescriptor.name] = index
                registerMeter(statisticDescriptor)
            }
        }
    }

    override fun getName() = name
    override fun getDescription() = description
    override fun getStatistics() = statistics

    override fun nameToId(name: String): Int = statsNameToIdMap[name]
            ?: throw IllegalArgumentException("Stat does not exist for name: $name in group: ${this.name}")

    override fun nameToDescriptor(name: String): StatisticDescriptor = statsArray[nameToId(name)]

    override fun getIntStatCount(): Int = 0
    override fun getLongStatCount(): Int = 0
    override fun getDoubleStatCount(): Int = 0

    fun getStatsForId(id: Int) = statsArray[id] as MicrometerStatisticMeter
}
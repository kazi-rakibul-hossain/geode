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
package org.apache.geode.statistics.internal.micrometer.impl

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import org.apache.geode.statistics.internal.micrometer.StatisticsMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementer
import org.apache.geode.stats.common.statistics.StatisticsFactory

abstract class MicrometerMeterGroup(private val statisticsFactory: StatisticsFactory,private val groupName: String) : StatisticsMeterGroup, MeterBinder,
        MicrometerStatsImplementer {

    override fun postConstruct(factory: StatisticsFactory) {
        initializeImplementer(statisticsFactory)
        registerStatsImplementer(statisticsFactory)
    }

    private val registeredMeters = mutableListOf<MicrometerStatisticMeter>()
    private val registeredMeterGroups = mutableListOf<MicrometerMeterGroup>()

    private val commonGroupTags: Array<String> by lazy { getGroupTags() }

    abstract fun initializeStaticMeters()

    open fun getGroupTags(): Array<String> = emptyArray()

    override fun registerStatsImplementer(factory: StatisticsFactory) {
        MicrometerStatisticsManager.registerMeterGroup(this.groupName, this)
    }

    final override fun initializeImplementer(factory: StatisticsFactory) {
        initializeStaticMeters()
    }

    override fun getMeterGroupName(): String = groupName

    override fun bindTo(registry: MeterRegistry) {
        registeredMeters.forEach { it.register(registry, commonGroupTags) }
        registeredMeterGroups.forEach { micrometerMeterGroup -> micrometerMeterGroup.registeredMeters.forEach { it.register(registry, commonGroupTags) } }
    }

    protected fun registerMeter(meter: MicrometerStatisticMeter) {
        registeredMeters.add(meter)
    }

    protected fun registerMeterGroup(meterGroup: MicrometerMeterGroup) {
        registeredMeterGroups.add(meterGroup)
    }
}
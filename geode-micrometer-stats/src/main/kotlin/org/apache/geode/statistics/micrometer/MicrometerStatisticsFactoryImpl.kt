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

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerStatisticsManager
import org.apache.geode.stats.common.statistics.StatisticDescriptor
import org.apache.geode.stats.common.statistics.Statistics
import org.apache.geode.stats.common.statistics.StatisticsFactory
import org.apache.geode.stats.common.statistics.StatisticsType
import org.apache.geode.stats.common.statistics.factory.StatsFactory
import java.io.Reader

class MicrometerStatisticsFactoryImpl(vararg meterRegistries: MeterRegistry = arrayOf(SimpleMeterRegistry())) : StatisticsFactory {

    private val micrometerStatisticsManager = MicrometerStatisticsManager.createWithRegistries(meterRegistries)
    private val meterGroupMap = hashMapOf<String, StatisticsType>()

    override fun getType(): String = "Micrometer"


    override fun createOsStatistics(type: StatisticsType, textId: String, numericId: Long, osStatFlags: Int): Statistics =
            MicrometerStatisticsImpl(0, type as MicrometerStatisticsType, textId, numericId)

    override fun createStatistics(type: StatisticsType): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStatistics(type: StatisticsType, textId: String): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStatistics(type: StatisticsType, textId: String, numericId: Long): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createAtomicStatistics(type: StatisticsType): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createAtomicStatistics(type: StatisticsType, textId: String): Statistics =
            createAtomicStatistics(type, textId, 0)

    override fun createAtomicStatistics(type: StatisticsType, textId: String, numericId: Long): Statistics =
            MicrometerStatisticsImpl(0, type as MicrometerStatisticsType, textId, numericId)

    override fun findStatisticsByType(type: StatisticsType): Array<Statistics> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findStatisticsByTextId(textId: String): Array<Statistics> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findStatisticsByNumericId(numericId: Long): Array<Statistics> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createIntCounter(name: String, description: String, units: String) = CounterStatisticMeter(name, description, meterUnit = units)

    override fun createLongCounter(name: String, description: String, units: String) = CounterStatisticMeter(name, description, meterUnit = units)

    override fun createDoubleCounter(name: String, description: String, units: String) = CounterStatisticMeter(name, description, meterUnit = units)

    override fun createIntGauge(name: String, description: String, units: String) = GaugeStatisticMeter(name, description, meterUnit = units)

    override fun createLongGauge(name: String, description: String, units: String) = GaugeStatisticMeter(name, description, meterUnit = units)

    override fun createDoubleGauge(name: String, description: String, units: String) = GaugeStatisticMeter(name, description, meterUnit = units)

    override fun createIntCounter(name: String, description: String, units: String, largerBetter: Boolean) = createIntCounter(name, description, units)

    override fun createLongCounter(name: String, description: String, units: String, largerBetter: Boolean) = createLongCounter(name, description, units)

    override fun createDoubleCounter(name: String, description: String, units: String, largerBetter: Boolean) = createDoubleCounter(name, description, units)

    override fun createIntGauge(name: String, description: String, units: String, largerBetter: Boolean) = createIntGauge(name, description, units)

    override fun createLongGauge(name: String, description: String, units: String, largerBetter: Boolean) = createIntGauge(name, description, units)

    override fun createDoubleGauge(name: String, description: String, units: String, largerBetter: Boolean) = createIntGauge(name, description, units)

    override fun createType(name: String, description: String, stats: Array<StatisticDescriptor>): StatisticsType {
        val micrometerStatisticsType = MicrometerStatisticsType(name, description, stats, StatsFactory.getStatisticsFactory())
        micrometerStatisticsManager.registerMeterGroup(name, micrometerStatisticsType)
        meterGroupMap[name] = micrometerStatisticsType
        return micrometerStatisticsType
    }

    override fun findType(name: String): StatisticsType = meterGroupMap[name]
            ?: throw IllegalArgumentException("No stats group for $name exists")

    override fun createTypesFromXml(reader: Reader): Array<StatisticsType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findStatisticsByUniqueId(uniqueId: Long): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStatsList(): MutableList<Any?> {
        return super.getStatsList()
    }
}
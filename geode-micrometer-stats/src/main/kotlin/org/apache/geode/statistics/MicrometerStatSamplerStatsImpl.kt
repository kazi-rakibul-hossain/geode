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
package org.apache.geode.statistics

import org.apache.geode.internal.statistics.StatSampleStats
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementer
import org.apache.geode.stats.common.statistics.StatisticsFactory

/**
 * Statistics related to the statistic sampler.
 */
class MicrometerStatSamplerStats(factory: StatisticsFactory, id: String) : MicrometerMeterGroup(factory, id), StatSampleStats, MicrometerStatsImplementer {

    private val statSamplerSampleCountMeter = CounterStatisticMeter("stat.sample.count", "Total number of samples taken by this sampler.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val statSamplerSampleTimeTimer = CounterStatisticMeter("stat.sample.timer", "Total amount of time spent taking samples.")
    private val statSamplerDelayDurationMeter = GaugeStatisticMeter("stat.sample.delay.timer",
            "Actual duration of sampling delay taken before taking this sample.")
    private val statSamplerStatisticResoucesCountMeter = GaugeStatisticMeter("stat.sample.resource.count",
            "Current number of statistic resources being sampled by this sampler.")
    private val statSamplerJVMPausesMeter = CounterStatisticMeter("stat.sample.jvm.pause.count",
            "Total number of JVM pauses (which may or may not be full GC pauses) detected by this sampler. " +
                    "A JVM pause is defined as a system event which kept the statistics sampler thread from sampling for 3000 or more milliseconds. " +
                    "This threshold can be customized by setting the system property gemfire.statSamplerDelayThreshold (units are milliseconds).")
    private val statSamplerSampleCallbackCountMeter = GaugeStatisticMeter("stat.sample.callbacks.count",
            "Current number of statistics that are sampled using callbacks.", arrayOf("status", "success"))
    private val statSamplerSampleCallbackErrorCountMeter = CounterStatisticMeter("stat.sample.callbacks.count",
            "Total number of exceptions thrown by callbacks when performing sampling", arrayOf("status", "error"))
    private val statSamplerSampleCallbackTimer = CounterStatisticMeter("stat.sample.callbacks.timer", "Total amount of time invoking sampling callbacks")

    override fun initializeStaticMeters() {
        registerMeter(statSamplerDelayDurationMeter)
        registerMeter(statSamplerJVMPausesMeter)
        registerMeter(statSamplerSampleCallbackCountMeter)
        registerMeter(statSamplerSampleCallbackErrorCountMeter)
        registerMeter(statSamplerSampleCountMeter)
        registerMeter(statSamplerStatisticResoucesCountMeter)
        registerMeter(statSamplerSampleCallbackTimer)
        registerMeter(statSamplerSampleTimeTimer)
    }


    fun tookSample(nanosSpentWorking: Long, statResources: Int, nanosSpentSleeping: Long) {
        statSamplerSampleCountMeter.increment()
        statSamplerSampleTimeTimer.increment(nanosSpentWorking / 1000000)
        statSamplerDelayDurationMeter.increment(nanosSpentSleeping / 1000000)
        statSamplerStatisticResoucesCountMeter.increment(statResources)
    }

    override fun incJvmPauses() {
        statSamplerJVMPausesMeter.increment()
    }

    override fun incSampleCallbackErrors(delta: Int) {
        statSamplerSampleCallbackErrorCountMeter.increment(delta)
    }

    override fun setSampleCallbacks(count: Int) {
        statSamplerSampleCallbackCountMeter.setValue(count)
    }

    override fun incSampleCallbackDuration(delta: Long) {
        statSamplerSampleCallbackTimer.increment(delta)
    }

    override fun getSampleCount(): Int = statSamplerSampleCountMeter.getValue().toInt()

    override fun getSampleTime(): Long = statSamplerSampleTimeTimer.getValue()

    override fun getDelayDuration(): Int = statSamplerDelayDurationMeter.getValue().toInt()

    override fun getStatResources(): Int = statSamplerStatisticResoucesCountMeter.getValue().toInt()

    override fun getJvmPauses(): Int = statSamplerJVMPausesMeter.getValue().toInt()
}

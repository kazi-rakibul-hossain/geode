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
package org.apache.geode.statistics.query

import org.apache.geode.stats.common.cache.query.internal.cq.CqServiceVsdStats
import org.apache.geode.stats.common.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementer
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerCqServiceVsdStats(statisticsFactory: StatisticsFactory, identifier: String) :
        MicrometerMeterGroup(statisticsFactory= statisticsFactory,groupName = "CqServiceStats"),
        CqServiceVsdStats,MicrometerStatsImplementer {

    private val cqNumberCQsCreatedMeter = GaugeStatisticMeter("cq.count", "Number of CQs created")
    private val cqNumberCQsActiveMeter = GaugeStatisticMeter("cq.count", "Number of CQS actively executing.", arrayOf("state", "active"))
    private val cqNumberCQsStoppedMeter = GaugeStatisticMeter("cq.count", "Number of CQs stopped.", arrayOf("state", "stopped"))
    private val cqNumberCQsClosedMeter = GaugeStatisticMeter("cq.count", "Number of CQs closed.", arrayOf("state", "closed"))
    private val cqNumberCQsDefinedOnClient = GaugeStatisticMeter("cq.count.client", "Number of CQs on the client.")
    private val cqNumberOfClientsWithCQDefinedMeter = GaugeStatisticMeter("cq.client.count", "Number of Clients with CQs.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val cqQueryExecutionTimer = CounterStatisticMeter("cq.query.execution.time", "Time taken for CQ Query Execution.", meterUnit = "nanoseconds")
    private val cqQueryExecutionCompletedMeter = CounterStatisticMeter("cq.query.execution.completed.count", "Number of CQ Query Executions.")
    private val cqQueryExecutionInProgressMeter = GaugeStatisticMeter("cq.query.execution.inprogress.count", "CQ Query Execution In Progress.")
    private val cqUniqueQueriesMeter = GaugeStatisticMeter("cq.query.unique.count", "Number of Unique CQ Querys.")

    override fun initializeStaticMeters() {
        registerMeter(cqNumberCQsCreatedMeter)
        registerMeter(cqNumberCQsActiveMeter)
        registerMeter(cqNumberCQsStoppedMeter)
        registerMeter(cqNumberCQsClosedMeter)
        registerMeter(cqNumberOfClientsWithCQDefinedMeter)
        registerMeter(cqQueryExecutionTimer)
        registerMeter(cqQueryExecutionCompletedMeter)
        registerMeter(cqQueryExecutionInProgressMeter)
        registerMeter(cqUniqueQueriesMeter)
    }


    override fun incCqsCreated() {
        cqNumberCQsCreatedMeter.increment()
    }

    override fun incCqsActive() {
        cqNumberCQsActiveMeter.increment()
    }

    override fun decCqsActive() {
        cqNumberCQsActiveMeter.decrement()
    }

    override fun incCqsStopped() {
        cqNumberCQsStoppedMeter.increment()
    }

    override fun decCqsStopped() {
        cqNumberCQsStoppedMeter.decrement()
    }

    override fun incCqsClosed() {
        cqNumberCQsClosedMeter.increment()
    }

    override fun incCqsOnClient() {
        cqNumberCQsDefinedOnClient.increment()
    }

    override fun decCqsOnClient() {
        cqNumberCQsDefinedOnClient.decrement()
    }

    override fun incClientsWithCqs() {
        cqNumberOfClientsWithCQDefinedMeter.increment()
    }

    override fun decClientsWithCqs() {
        cqNumberOfClientsWithCQDefinedMeter.decrement()
    }

    override fun startCqQueryExecution(): Long {
        cqQueryExecutionInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endCqQueryExecution(start: Long) {
        cqQueryExecutionTimer.increment(NOW_NANOS - start)
        cqQueryExecutionInProgressMeter.decrement()
        cqQueryExecutionCompletedMeter.increment()
    }

    override fun incUniqueCqQuery() {
        cqUniqueQueriesMeter.increment()
    }

    override fun decUniqueCqQuery() {
        cqUniqueQueriesMeter.decrement()
    }

    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumCqsCreated(): Long = cqNumberCQsCreatedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumCqsActive(): Long = cqNumberCQsActiveMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumCqsStopped(): Long = cqNumberCQsStoppedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumCqsClosed(): Long = cqNumberCQsClosedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumCqsOnClient(): Long = cqNumberCQsDefinedOnClient.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumClientsWithCqs(): Long = cqNumberOfClientsWithCQDefinedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCqQueryExecutionTime(): Long = cqQueryExecutionTimer.getValue()
}
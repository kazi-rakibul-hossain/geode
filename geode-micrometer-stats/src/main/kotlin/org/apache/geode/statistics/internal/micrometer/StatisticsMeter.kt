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
package org.apache.geode.statistics.internal.micrometer

import java.time.Duration
import java.util.concurrent.TimeUnit

interface StatisticsMeter {
    fun getMetricName(): String
    fun getBaseUnit(): String
}

interface ScalarStatisticsMeter {
    fun increment()
    fun increment(value: Double = 1.0)
    fun increment(value: Long = 1L)
    fun increment(value: Int = 1)
    fun decrement()
    fun decrement(value: Double = -1.0)
    fun decrement(value: Long = -1L)
    fun decrement(value: Int = -1)
}

interface TimedStatisticsMeter {
    fun recordValue(amount: Long, timeUnit: TimeUnit = TimeUnit.NANOSECONDS)
    fun recordValue(duration: Duration)
}
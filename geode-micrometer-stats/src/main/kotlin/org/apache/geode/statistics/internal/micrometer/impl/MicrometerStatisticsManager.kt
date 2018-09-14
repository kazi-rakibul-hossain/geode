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

import com.sun.net.httpserver.HttpServer
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.config.MeterFilterReply
import io.micrometer.influx.InfluxConfig
import io.micrometer.influx.InfluxMeterRegistry
import io.micrometer.jmx.JmxConfig
import io.micrometer.jmx.JmxMeterRegistry
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.pivotal.gemfire.micrometer.binder.LoadAvgMetrics
import io.pivotal.gemfire.micrometer.binder.MemInfoMetrics
import io.pivotal.gemfire.micrometer.binder.StatMetrics
import io.pivotal.gemfire.micrometer.procOS.ProcOSLoadAvg
import io.pivotal.gemfire.micrometer.procOS.ProcOSMemInfo
import io.pivotal.gemfire.micrometer.procOS.ProcOSReaderFactory
import io.pivotal.gemfire.micrometer.procOS.ProcOSStat
import org.apache.geode.statistics.internal.micrometer.StatisticsManager
import org.apache.geode.statistics.internal.micrometer.StatisticsMeterGroup
import java.io.IOException
import java.lang.management.ManagementFactory
import java.net.InetSocketAddress
import java.time.Duration

object MicrometerStatisticsManager : StatisticsManager {

    //    @JvmOverloads constructor(private val enableStats: Boolean = true,
//                                                            private val serverName: String = "cacheServer" + InetAddress.getLocalHost().hostAddress,
//                                                            vararg meterRegistries: MeterRegistry,
//                                                            private val meterRegistry: CompositeMeterRegistry =
//                                                                    CompositeMeterRegistry(Clock.SYSTEM)) : StatisticsManager {
    private val registeredMeterGroups = mutableMapOf<String, MicrometerMeterGroup>()
    private val meterRegistry: CompositeMeterRegistry = createCompositeRegistry()
    private var serverName: String = "cacheServer_" + ManagementFactory.getRuntimeMXBean().name
    private var enableStats: Boolean = true

    fun registerMeterRegistries(vararg meterRegistries: MeterRegistry) {
        meterRegistries.forEach { meterRegistry.add(it) }
    }

    fun disableStatsCollection() {
        enableStats = false
    }


    init {
//        meterRegistries.forEach { meterRegistry.add(it) }
        meterRegistry.config().commonTags("serverName", serverName)
        JvmGcMetrics().bindTo(meterRegistry)
        JvmThreadMetrics().bindTo(meterRegistry)
        JvmMemoryMetrics().bindTo(meterRegistry)
        ClassLoaderMetrics().bindTo(meterRegistry)
        FileDescriptorMetrics().bindTo(meterRegistry)
        ProcessorMetrics().bindTo(meterRegistry)
        UptimeMetrics().bindTo(meterRegistry)
        val procOSReaderFactory = ProcOSReaderFactory()
        LoadAvgMetrics(procOSLoadAvg = ProcOSLoadAvg(procOSReaderFactory.getInstance(LoadAvgMetrics.LOAD_AVG))).bindTo(meterRegistry)
        MemInfoMetrics(procOSMemInfo = ProcOSMemInfo(procOSReaderFactory.getInstance(MemInfoMetrics.MEM_INFO))).bindTo(meterRegistry)
        StatMetrics(procOSStat = ProcOSStat(procOSReaderFactory.getInstance(StatMetrics.STAT))).bindTo(meterRegistry)
    }

    override fun registerMeterRegistry(meterRegistry: MeterRegistry) {
        this.meterRegistry.add(meterRegistry)
    }

    override fun registerMeterGroup(groupName: String, meterGroup: StatisticsMeterGroup) {
        if (meterGroup is MicrometerMeterGroup) {
            registeredMeterGroups.putIfAbsent(groupName, meterGroup)
                    ?.run { println("MeterGroup: $groupName was already registered") }
//            if (!enableStats) {
//                meterRegistry.config().meterFilter(object : MeterFilter {
//                    override fun accept(id: Meter.Id): MeterFilterReply {
//                        return MeterFilterReply.DENY
//                    }
//                })
//            }
            meterGroup.bindTo(meterRegistry)
        } else {
            TODO("Register Non-MircometerMeterGrouops, this feature is not yet supported. Most likely never will be")
        }
    }

    fun createWithRegistries(meterRegistries: Array<out MeterRegistry>): MicrometerStatisticsManager {
        registerMeterRegistries(*meterRegistries)
        return this
    }

    private fun createCompositeRegistry(): CompositeMeterRegistry {
        val compositeMeterRegistry = CompositeMeterRegistry(Clock.SYSTEM)
        compositeMeterRegistry.add(createInfluxDB())
//        compositeMeterRegistry.add(createPrometheus())
//        compositeMeterRegistry.add(createJMX())
        return compositeMeterRegistry
    }

    private fun createJMX(): JmxMeterRegistry {
        val config = object : JmxConfig {
            override fun step(): Duration = Duration.ofSeconds(10)
            override fun get(k: String): String? = null
            override fun domain(): String = "geodeMetrics"
        }
        return JmxMeterRegistry(config, Clock.SYSTEM)
    }

    private fun createInfluxDB(): InfluxMeterRegistry {
        val config = object : InfluxConfig {
            override fun step(): Duration = Duration.ofSeconds(10)
            override fun db(): String = "mydb4"
            override fun get(k: String): String? = null
        }
        return InfluxMeterRegistry(config, Clock.SYSTEM)
    }

    private fun createPrometheus(): PrometheusMeterRegistry {
        val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        try {
            val port = System.getProperty("statsPort")?.toInt() ?: 10080
            val server = HttpServer.create(InetSocketAddress(port), 0)
            server.createContext("/geode") {
                val response = prometheusRegistry.scrape()
                it.sendResponseHeaders(200, response.toByteArray().size.toLong())
                it.responseBody?.run { this.write(response.toByteArray()) }
            }
            Thread(server::start).start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return prometheusRegistry
    }
}
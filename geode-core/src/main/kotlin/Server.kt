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
import org.apache.geode.cache.CacheFactory
import org.apache.geode.cache.RegionShortcut
import org.apache.geode.distributed.ConfigurationProperties
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val properties = Properties().apply {
        setProperty("locators", "localhost[44550]")
        setProperty("mcast-port", "0")
        setProperty("statistic-sampling-enabled", "true")
        setProperty(ConfigurationProperties.JMX_MANAGER, "false")
        setProperty(ConfigurationProperties.JMX_MANAGER_START, "false")
        setProperty(ConfigurationProperties.ENABLE_CLUSTER_CONFIGURATION, "false")
    }
    val cache = CacheFactory(properties).create()
    cache.addCacheServer().apply {
        port = 0
        bindAddress = "localhost"
    }.start()


    val diskStoreFactory = cache.createDiskStoreFactory()
    val diskStore1Name = "${System.identityHashCode(cache)}_someDiskStore"
    diskStoreFactory.setDiskDirs(arrayOf(File("/tmp/$diskStore1Name")))
    diskStoreFactory.create(diskStore1Name)

    val regionFactory = cache.createRegionFactory<String, String>(RegionShortcut.PARTITION_REDUNDANT_OVERFLOW)
    regionFactory.setStatisticsEnabled(true)
    regionFactory.setDiskStoreName(diskStore1Name)
    val region1 = regionFactory.create("Region1")

    val diskStoreFactory2 = cache.createDiskStoreFactory()
    val diskStore2Name = "${System.identityHashCode(cache)}_someDiskStore2"
    diskStoreFactory2.setDiskDirs(arrayOf(File("/tmp/$diskStore2Name")))
    diskStoreFactory2.create(diskStore2Name)

    val regionFactory2 = cache.createRegionFactory<String, String>(RegionShortcut.PARTITION_REDUNDANT_OVERFLOW)
    regionFactory2.setStatisticsEnabled(true)
    regionFactory2.setDiskStoreName(diskStore2Name)
    val region2 = regionFactory2.create("Region3")

    val manager = cache.resourceManager
    val op = manager.createRebalanceFactory().start()
}
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

import org.apache.geode.cache.client.ClientCacheFactory
import org.apache.geode.cache.client.ClientRegionShortcut
import java.util.*
import java.util.stream.IntStream

fun main(args: Array<String>) {
    val properties = Properties().apply {
        setProperty("mcast-port", "0")
        setProperty("statistic-sampling-enabled", "true")
    }
    val cache = ClientCacheFactory(properties).addPoolLocator("localhost", 44550).create()

    val regionFactory = cache.createClientRegionFactory<String, String>(ClientRegionShortcut.PROXY)
    regionFactory.setStatisticsEnabled(true)
    val region1 = regionFactory.create("Region1")

    val regionFactory2 = cache.createClientRegionFactory<String, String>(ClientRegionShortcut.PROXY)
    regionFactory2.setStatisticsEnabled(true)
    val region2 = regionFactory2.create("Region3")

    val random = Random()
    IntStream.range(0, Int.MAX_VALUE).parallel().forEach { count ->

        region1[count.toString()] = region1.size.toString()

        for (value in 0..random.nextInt(25)) {
            region2[(value*count).toString()] = value.toString()
        }

        if (random.nextBoolean()) {
            for (value in 0..random.nextInt(35)) {
                region1[count.toString()]
                region2.destroy((value*count).toString())
            }
        } else {
            for (value in 0..random.nextInt(10)) {
                region2[count.toString()]
                region1.destroy((value*count).toString())
            }
        }
//        Thread.sleep(100)

        println("Processed value $count")

    }
}
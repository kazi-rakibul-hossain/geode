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
package org.apache.geode.stats.common.statistics.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import org.apache.geode.stats.common.Stats;
import org.apache.geode.stats.common.cache.client.internal.ConnectionStats;
import org.apache.geode.stats.common.internal.cache.CachePerfStats;
import org.apache.geode.stats.common.internal.cache.PoolStats;
import org.apache.geode.stats.common.internal.cache.RegionPerfStats;
import org.apache.geode.stats.common.statistics.GFSStatsImplementer;
import org.apache.geode.stats.common.statistics.StatisticsFactory;
import org.apache.geode.stats.common.statistics.StatsImplementer;

public class StatsFactory {

  private static final boolean isExperimentalEnabled =
      Boolean.getBoolean("geode.experimental.stats.micrometer");
  private static final StatsFactory statsFactory =
      new StatsFactory(getStatsImplementor(), getStatisticsTypeFactory());
  private final Class<? extends StatsImplementer> selectedStatsImplementor;
  private final Map<Class<?>, Class<? extends StatsImplementer>> resolvedStatsImplForClass =
      new HashMap<>();
  private final StatisticsFactory statisticsFactory;

  private StatsFactory(Class<? extends StatsImplementer> selectedStatsImplementor,
      StatisticsFactory statisticsFactory) {
    List<ClassLoader> classLoadersList = new LinkedList<>();
    classLoadersList.add(ClasspathHelper.contextClassLoader());
    classLoadersList.add(ClasspathHelper.staticClassLoader());

    /* don't exclude Object.class */
    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setScanners(new SubTypesScanner(false /* don't exclude Object.class */),
            new ResourcesScanner())
        .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
        .filterInputsBy(new FilterBuilder()
            .includePackage("org.apache.geode..*").excludePackage("org.springframework..*")));
    this.selectedStatsImplementor = selectedStatsImplementor;
    this.statisticsFactory = statisticsFactory;
    initializeStatsImplementations(reflections);
  }

  private static Class<? extends StatsImplementer> getStatsImplementor() {
    try {
      return isExperimentalEnabled
          ? (Class<? extends StatsImplementer>) Class
              .forName("org.apache.geode.statistics.micrometer.MicrometerStatsImplementer")
          : GFSStatsImplementer.class;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return GFSStatsImplementer.class;
    }
  }

  private static StatisticsFactory getStatisticsTypeFactory() {
    final ServiceLoader<StatisticsFactory> services = ServiceLoader.load(StatisticsFactory.class);
    for (StatisticsFactory statisticsFactory : services) {
      if (isExperimentalEnabled && statisticsFactory.getType().equals("Micrometer")) {
        return statisticsFactory;
      } else if (statisticsFactory.getType().equals("GeodeStats")) {
        return statisticsFactory;
      }
    }
    throw new IllegalStateException(
        "There should have been a statsfactory found. Please check your settings");
  }

  public static <V extends StatsImplementer> V createStatsImpl(Class<?> interfaceClazz,
      String identifier) {
    return (V) resolveInstanceFromClass(interfaceClazz, identifier);
  }

  public static ConnectionStats createConnectionStatsImpl(
      String statName, PoolStats poolStats) {
    return (ConnectionStats) resolveConnectionStatInstanceFromClass(statName, poolStats);
  }

  public static CachePerfStats createCachePerfStatsImpl(String name) {
    return (CachePerfStats) resolveCachePerfInstanceFromClass(name);
  }

  private static StatsImplementer resolveInstanceFromClass(Class<?> interfaceClazz, String name) {
    return statsFactory.createInstanceFromClass(interfaceClazz, name);
  }

  private static StatsImplementer resolveCachePerfInstanceFromClass(String name) {
    return statsFactory.createInstanceFromClass(CachePerfStats.class, name);
  }

  private static StatsImplementer resolveConnectionStatInstanceFromClass(String name,
      PoolStats poolStats) {
    return statsFactory.createConnectionStatInstanceFromClass(name, poolStats);
  }

  public static RegionPerfStats createRegionPerfStatsImplFromClass(Class<?> interfaceClazz,
      CachePerfStats cachePerfStats, String regionName) {
    try {
      Class<? extends StatsImplementer> resolvedLocatorClassImpl =
          statsFactory.resolvedStatsImplForClass.get(interfaceClazz);
      return (RegionPerfStats) resolvedLocatorClassImpl
          .getDeclaredConstructor(StatisticsFactory.class, CachePerfStats.class, String.class)
          .newInstance(statsFactory.getFactory(), cachePerfStats, regionName);
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean isLegacyGeodeStats() {
    return statsFactory.getFactory().getType().equals("GeodeStats");
  }

  public static StatisticsFactory getStatisticsFactory() {
    return statsFactory.statisticsFactory;
  }

  private StatisticsFactory getFactory() {
    return statisticsFactory;
  }

  private void initializeStatsImplementations(Reflections reflections) {
    reflections.getSubTypesOf(Stats.class)
        .stream().filter(Class::isInterface)
        .forEach(interfaze -> reflections.getSubTypesOf(interfaze).stream()
            .filter(selectedStatsImplementor::isAssignableFrom)
            .forEach(aClass -> resolvedStatsImplForClass
                .put(interfaze, (Class<? extends StatsImplementer>) aClass)));
    // The loading of CachePerfStats implementations needs to be special cased,
    // as RegionPerfStats is also of type CachePerfStats, which causes problems when you
    // want to find the direct implementation of CachePerfStats
    reflections.getSubTypesOf(CachePerfStats.class).stream()
        .filter(clazz -> selectedStatsImplementor.isAssignableFrom(clazz)
            && !RegionPerfStats.class.isAssignableFrom(clazz) && !clazz.getName()
                .contains("DummyCachePerfStats"))
        .forEach(aClass -> resolvedStatsImplForClass
            .put(CachePerfStats.class, (Class<? extends StatsImplementer>) aClass));
  }

  private StatsImplementer createConnectionStatInstanceFromClass(String locatorName,
      PoolStats poolStats) {
    try {
      Class<? extends StatsImplementer> resolvedLocatorClassImpl =
          resolveImplementationForClass(ConnectionStats.class);
      return resolvedLocatorClassImpl
          .getDeclaredConstructor(StatisticsFactory.class, String.class, PoolStats.class)
          .newInstance(statisticsFactory, locatorName, poolStats);
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Class<? extends StatsImplementer> resolveImplementationForClass(Class<?> interfaceClazz) {
    Class<? extends StatsImplementer> resolvedLocatorClassImpl;
    resolvedLocatorClassImpl = resolvedStatsImplForClass.get(interfaceClazz);
    if (resolvedLocatorClassImpl == null) {
      throw new IllegalArgumentException(
          "Expected to have stats for type: " + interfaceClazz);
    }
    return resolvedLocatorClassImpl;
  }

  private StatsImplementer createInstanceFromClass(Class<?> interfaceClazz, String name) {
    try {
      Class<? extends StatsImplementer> resolvedLocatorClassImpl =
          resolveImplementationForClass(interfaceClazz);
      return resolvedLocatorClassImpl
          .getDeclaredConstructor(StatisticsFactory.class, String.class)
          .newInstance(statisticsFactory, name);
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}

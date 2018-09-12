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
package org.apache.geode.internal.statistics;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.geode.distributed.internal.DistributionConfig;
import org.apache.geode.distributed.internal.InternalDistributedSystem;
import org.apache.geode.internal.cache.execute.FunctionServiceStats;
import org.apache.geode.internal.statistics.platform.OsStatisticsFactory;
import org.apache.geode.stats.common.internal.cache.execute.FunctionStats;
import org.apache.geode.stats.common.statistics.Statistics;
import org.apache.geode.stats.common.statistics.StatisticsFactory;
import org.apache.geode.stats.common.statistics.StatisticsType;
import org.apache.geode.stats.common.statistics.factory.StatsFactory;

public class InternalDistributedSystemStats {
  // implements OsStatisticsFactory {

  // TODO, sorry another singleton... BLECH!!
  private static final InternalDistributedSystemStats singleton =
      new InternalDistributedSystemStats();
  // As the function execution stats can be lot in number, its better to put
  // them in a map so that it will be accessible immediately
  private final ConcurrentHashMap<String, FunctionStats> functionExecutionStatsMap =
      new ConcurrentHashMap<>();
  /**
   * The sampler for this DistributedSystem.
   */
  private GemFireStatSampler sampler = null;
  private FunctionServiceStats functionServiceStats;
  private boolean statsDisabled;

  private StatisticsFactory statisticsFactory = StatsFactory.getStatisticsFactory();

  private InternalDistributedSystemStats() {
  }

  public static InternalDistributedSystemStats createInstance(boolean statsDisabled) {

    singleton.statsDisabled = statsDisabled;
    singleton.functionServiceStats =
        StatsFactory.createStatsImpl(FunctionServiceStats.class, "FunctionExecution");
    return singleton;
  }

  private static void startGemFireStatSampler(boolean statsDisabled,
                                              DistributionConfig distributionConfig,
                                              InternalDistributedSystem distributedSystem,
                                              InternalDistributedSystemStats internalDistributedSystemStats) {
    if (!statsDisabled && StatsFactory.isLegacyGeodeStats()) {
      internalDistributedSystemStats.sampler =
          new GemFireStatSampler(distributedSystem.getId(), distributionConfig,
              distributedSystem.getCancelCriterion(), internalDistributedSystemStats,
              distributedSystem.getDistributionManager());
      internalDistributedSystemStats.sampler.start();

    }
  }

  public void startGemFireStatSampler(boolean statsDisabled,
                                              DistributionConfig distributionConfig,
                                              InternalDistributedSystem distributedSystem) {
    startGemFireStatSampler(statsDisabled, distributionConfig, distributedSystem, this);
  }

  public static InternalDistributedSystemStats getSingleton() {
    return singleton;
  }

  public FunctionStats getFunctionStats(String textId) {
    FunctionStats stats = functionExecutionStatsMap.get(textId);
    if (stats == null) {
      stats = StatsFactory.createStatsImpl(FunctionStats.class, textId);
      FunctionStats oldStats = functionExecutionStatsMap.putIfAbsent(textId, stats);
      if (oldStats != null) {
        stats.close();
        stats = oldStats;
      }
    }
    return stats;
  }

  public FunctionServiceStats getFunctionServiceStats() {
    return functionServiceStats;
  }

  public Set<String> getAllFunctionExecutionIds() {
    return functionExecutionStatsMap.keySet();
  }

  public void closeStats() {
    // closing the Aggregate stats
    if (functionServiceStats != null) {
      functionServiceStats.close();
    }
    // closing individual function stats
    for (FunctionStats functionstats : functionExecutionStatsMap.values()) {
      functionstats.close();
    }

    if (sampler != null) {
      this.sampler.stop();
    }
  }

  public GemFireStatSampler getStatSampler() {
    return this.sampler;
  }

  /**
   * For every registered statistic instance call the specified visitor. This method was added to
   * fix bug 40358
   */
  public void visitStatistics(InternalDistributedSystem.StatisticsVisitor visitor) {
    for (Statistics s : getStatsList()) {
      visitor.visit(s);
    }
  }

  public StatisticsFactory getStatisticsFactory() {
    return statisticsFactory;
  }

  public List<Statistics> getStatsList() {
    return statisticsFactory.getStatsList();
  }

  public Statistics[] findStatisticsByType(StatisticsType gcType) {
    return statisticsFactory.findStatisticsByType(gcType);
  }

  public Statistics findStatisticsByUniqueId(long rsrcUniqueId) {
    return statisticsFactory.findStatisticsByUniqueId(rsrcUniqueId);
  }

  public StatisticsType findType(String cachePerfStats) {
    return statisticsFactory.findType(cachePerfStats);
  }

  public Statistics[] findStatisticsByTextId(String textId) {
    return statisticsFactory.findStatisticsByTextId(textId);
  }

  public StatisticsManager getStatisticsManager() {
    return (StatisticsManager) statisticsFactory;
  }

  public OsStatisticsFactory getOSStatisticsFactory() {
    return (OsStatisticsFactory) statisticsFactory;
  }
}

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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;

import org.apache.geode.distributed.internal.InternalDistributedSystem;
import org.apache.geode.internal.i18n.LocalizedStrings;
import org.apache.geode.internal.statistics.platform.OsStatisticsFactory;
import org.apache.geode.stats.common.statistics.StatisticDescriptor;
import org.apache.geode.stats.common.statistics.Statistics;
import org.apache.geode.stats.common.statistics.StatisticsFactory;
import org.apache.geode.stats.common.statistics.StatisticsType;
import org.apache.geode.stats.common.statistics.StatisticsTypeFactory;

public class GFSStatisticsFactoryImpl implements StatisticsFactory, StatisticsManager,
    OsStatisticsFactory {

  private final CopyOnWriteArrayList<Statistics> statsList = new CopyOnWriteArrayList<>();
  private int statsListModCount = 0;
  private LongAdder statsListUniqueId = new LongAdder();

  // StatisticsTypeFactory methods
  private StatisticsTypeFactory statisticsTypeFactory = new StatisticsTypeFactoryImpl();

  public GFSStatisticsFactoryImpl() {}

  @Override
  public int getStatListModCount() {
    return this.statsListModCount;
  }

  @Override
  public List<Statistics> getStatsList() {
    return this.statsList;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public long getId() {
    return 0;
  }

  @Override
  public long getStartTime() {
    return 0;
  }

  @Override
  public int getStatisticsCount() {
    return ((List<Statistics>) this.statsList).size();
  }

  @Override
  public Statistics findStatistics(long id) {
    List<Statistics> statsList = this.statsList;
    for (Statistics statistics : statsList) {
      if (statistics.getUniqueId() == id) {
        return statistics;
      }
    }
    throw new RuntimeException(
        LocalizedStrings.PureStatSampler_COULD_NOT_FIND_STATISTICS_INSTANCE.toLocalizedString());
  }

  @Override
  public boolean statisticsExists(long id) {
    List<Statistics> statsList = this.statsList;
    for (Statistics statistics : statsList) {
      if (statistics.getUniqueId() == id) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Statistics[] getStatistics() {
    List<Statistics> statsList = this.statsList;
    return statsList.toArray(new Statistics[0]);
  }

  /**
   * Creates or finds a StatisticType for the given shared class.
   */
  @Override
  public StatisticsType createType(String name, String description, StatisticDescriptor[] stats) {
    return statisticsTypeFactory.createType(name, description, stats);
  }

  @Override
  public StatisticsType findType(String name) {
    return statisticsTypeFactory.findType(name);
  }

  @Override
  public StatisticsType[] createTypesFromXml(Reader reader) throws IOException {
    return statisticsTypeFactory.createTypesFromXml(reader);
  }

  @Override
  public StatisticDescriptor createIntCounter(String name, String description, String units) {
    return statisticsTypeFactory.createIntCounter(name, description, units);
  }

  @Override
  public StatisticDescriptor createLongCounter(String name, String description, String units) {
    return statisticsTypeFactory.createLongCounter(name, description, units);
  }

  @Override
  public StatisticDescriptor createDoubleCounter(String name, String description, String units) {
    return statisticsTypeFactory.createDoubleCounter(name, description, units);
  }

  @Override
  public StatisticDescriptor createIntGauge(String name, String description, String units) {
    return statisticsTypeFactory.createIntGauge(name, description, units);
  }

  @Override
  public StatisticDescriptor createLongGauge(String name, String description, String units) {
    return statisticsTypeFactory.createLongGauge(name, description, units);
  }

  @Override
  public StatisticDescriptor createDoubleGauge(String name, String description, String units) {
    return statisticsTypeFactory.createDoubleGauge(name, description, units);
  }

  @Override
  public StatisticDescriptor createIntCounter(String name, String description, String units,
      boolean largerBetter) {
    return statisticsTypeFactory.createIntCounter(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createLongCounter(String name, String description, String units,
      boolean largerBetter) {
    return statisticsTypeFactory.createLongCounter(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createDoubleCounter(String name, String description, String units,
      boolean largerBetter) {
    return statisticsTypeFactory.createDoubleCounter(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createIntGauge(String name, String description, String units,
      boolean largerBetter) {
    return statisticsTypeFactory.createIntGauge(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createLongGauge(String name, String description, String units,
      boolean largerBetter) {
    return statisticsTypeFactory.createLongGauge(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createDoubleGauge(String name, String description, String units,
      boolean largerBetter) {
    return statisticsTypeFactory.createDoubleGauge(name, description, units, largerBetter);
  }

  // StatisticsFactory methods
  @Override
  public Statistics createStatistics(StatisticsType type) {
    return createOsStatistics(type, null, 0, 0);
  }

  @Override
  public Statistics createStatistics(StatisticsType type, String textId) {
    return createOsStatistics(type, textId, 0, 0);
  }

  @Override
  public Statistics createStatistics(StatisticsType type, String textId, long numericId) {
    return createOsStatistics(type, textId, numericId, 0);
  }

  @Override
  public Statistics[] findStatisticsByType(final StatisticsType type) {
    final ArrayList<Statistics> hits = new ArrayList<>();
    visitStatistics(vistorStatistic -> {
      if (type == vistorStatistic.getType()) {
        hits.add(vistorStatistic);
      }
    });
    Statistics[] result = new Statistics[hits.size()];
    return hits.toArray(result);
  }

  @Override
  public Statistics[] findStatisticsByTextId(final String textId) {
    final ArrayList<Statistics> hits = new ArrayList<>();
    visitStatistics(vistorStatistic -> {
      if (vistorStatistic.getTextId().equals(textId)) {
        hits.add(vistorStatistic);
      }
    });
    Statistics[] result = new Statistics[hits.size()];
    return hits.toArray(result);
  }

  @Override
  public Statistics[] findStatisticsByNumericId(final long numericId) {
    final ArrayList<Statistics> hits = new ArrayList<>();
    visitStatistics(vistorStatistic -> {
      if (numericId == vistorStatistic.getNumericId()) {
        hits.add(vistorStatistic);
      }
    });
    Statistics[] result = new Statistics[hits.size()];
    return hits.toArray(result);
  }

  @Override
  public Statistics findStatisticsByUniqueId(final long uniqueId) {
    for (Statistics statistics : this.statsList) {
      if (uniqueId == statistics.getUniqueId()) {
        return statistics;
      }
    }
    return null;
  }

  /**
   * for internal use only. Its called by {@link LocalStatisticsImpl#close}.
   */
  @Override
  public void destroyStatistics(Statistics stats) {
    synchronized (statsList) {
      if (statsList.remove(stats)) {
        statsListModCount++;
      }
    }
  }

  @Override
  public Statistics createOsStatistics(StatisticsType type, String textId, long numericId,
      int osStatFlags) {
    statsListUniqueId.increment();
    Statistics result =
        new LocalStatisticsImpl(type, textId, numericId, statsListUniqueId.longValue(), false,
            osStatFlags, this);
    synchronized (statsList) {
      statsList.add(result);
      statsListModCount++;
    }
    return result;
  }

  /**
   * For every registered statistic instance call the specified visitor. This method was added to
   * fix bug 40358
   */
  public void visitStatistics(InternalDistributedSystem.StatisticsVisitor visitor) {
    for (Statistics s : this.statsList) {
      visitor.visit(s);
    }
  }

  @Override
  public Statistics createAtomicStatistics(StatisticsType type) {
    return createAtomicStatistics(type, null, 0);
  }

  @Override
  public Statistics createAtomicStatistics(StatisticsType type, String textId) {
    return createAtomicStatistics(type, textId, 0);
  }

  @Override
  public Statistics createAtomicStatistics(StatisticsType type, String textId, long numericId) {
    statsListUniqueId.increment();
    return StatisticsImpl
        .createAtomicNoOS(type, textId, numericId, statsListUniqueId.longValue(), this);
  }
}

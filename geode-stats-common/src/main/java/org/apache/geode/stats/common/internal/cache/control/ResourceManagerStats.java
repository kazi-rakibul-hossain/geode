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
package org.apache.geode.stats.common.internal.cache.control;

import org.apache.geode.stats.common.Stats;
import org.apache.geode.stats.common.distributed.internal.PoolStatHelper;
import org.apache.geode.stats.common.distributed.internal.QueueStatHelper;

public interface ResourceManagerStats extends Stats {
  void close();

  long startRebalance();

  void incAutoRebalanceAttempts();

  void endRebalance(long start);

  void startBucketCreate(int regions);

  void endBucketCreate(int regions, boolean success, long bytes, long elapsed);

  void startBucketRemove(int regions);

  void endBucketRemove(int regions, boolean success, long bytes, long elapsed);

  void startBucketTransfer(int regions);

  void endBucketTransfer(int regions, boolean success, long bytes, long elapsed);

  void startPrimaryTransfer(int regions);

  void endPrimaryTransfer(int regions, boolean success, long elapsed);

  void incRebalanceMembershipChanges(int delta);

  int getRebalanceMembershipChanges();

  int getRebalancesInProgress();

  int getRebalancesCompleted();

  int getAutoRebalanceAttempts();

  long getRebalanceTime();

  int getRebalanceBucketCreatesInProgress();

  int getRebalanceBucketCreatesCompleted();

  int getRebalanceBucketCreatesFailed();

  long getRebalanceBucketCreateTime();

  long getRebalanceBucketCreateBytes();

  int getRebalanceBucketTransfersInProgress();

  int getRebalanceBucketTransfersCompleted();

  int getRebalanceBucketTransfersFailed();

  long getRebalanceBucketTransfersTime();

  long getRebalanceBucketTransfersBytes();

  int getRebalancePrimaryTransfersInProgress();

  int getRebalancePrimaryTransfersCompleted();

  int getRebalancePrimaryTransfersFailed();

  long getRebalancePrimaryTransferTime();

  void incResourceEventsDelivered();

  int getResourceEventsDelivered();

  void incHeapCriticalEvents();

  int getHeapCriticalEvents();

  void incOffHeapCriticalEvents();

  int getOffHeapCriticalEvents();

  void incHeapSafeEvents();

  int getHeapSafeEvents();

  void incOffHeapSafeEvents();

  int getOffHeapSafeEvents();

  void incEvictionStartEvents();

  int getEvictionStartEvents();

  void incOffHeapEvictionStartEvents();

  int getOffHeapEvictionStartEvents();

  void incEvictionStopEvents();

  int getEvictionStopEvents();

  void incOffHeapEvictionStopEvents();

  int getOffHeapEvictionStopEvents();

  void changeCriticalThreshold(long newValue);

  long getCriticalThreshold();

  void changeOffHeapCriticalThreshold(long newValue);

  long getOffHeapCriticalThreshold();

  void changeEvictionThreshold(long newValue);

  long getEvictionThreshold();

  void changeOffHeapEvictionThreshold(long newValue);

  long getOffHeapEvictionThreshold();

  void changeTenuredHeapUsed(long newValue);

  long getTenuredHeapUsed();

  void incResourceEventQueueSize(int delta);

  int getResourceEventQueueSize();

  void incThresholdEventProcessorThreadJobs(int delta);

  int getThresholdEventProcessorThreadJobs();

  QueueStatHelper getResourceEventQueueStatHelper();

  PoolStatHelper getResourceEventPoolStatHelper();

  int getNumThreadStuck();

  void setNumThreadStuck(int value);
}

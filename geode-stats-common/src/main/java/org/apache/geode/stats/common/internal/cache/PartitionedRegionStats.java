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
package org.apache.geode.stats.common.internal.cache;

import org.apache.geode.stats.common.Stats;
import org.apache.geode.stats.common.statistics.Statistics;

public interface PartitionedRegionStats extends Stats {
  void close();

  Statistics getStats();

  void endPut(long start);

  void endPutAll(long start);

  void endRemoveAll(long start);

  void endCreate(long start);

  void endGet(long start);

  void endContainsKey(long start);

  void endContainsValueForKey(long start);

  void endPut(long start, int numInc);

  void endPutAll(long start, int numInc);

  void endRemoveAll(long start, int numInc);

  void endCreate(long start, int numInc);

  void endGet(long start, int numInc);

  void endDestroy(long start);

  void endInvalidate(long start);

  void endContainsKey(long start, int numInc);

  void endContainsValueForKey(long start, int numInc);

  void incContainsKeyValueRetries();

  void incContainsKeyValueOpsRetried();

  void incInvalidateRetries();

  void incInvalidateOpsRetried();

  void incDestroyRetries();

  void incDestroyOpsRetried();

  void incPutRetries();

  void incPutOpsRetried();

  void incGetOpsRetried();

  void incGetRetries();

  void incCreateOpsRetried();

  void incCreateRetries();

  void incPreferredReadLocal();

  void incPreferredReadRemote();

  long startPartitionMessageProcessing();

  void endPartitionMessagesProcessing(long start);

  void incPartitionMessagesSent();

  void incBucketCount(int delta);

  void setBucketCount(int i);

  void incDataStoreEntryCount(int amt);

  int getDataStoreEntryCount();

  void incBytesInUse(long delta);

  long getDataStoreBytesInUse();

  int getTotalBucketCount();

  void incPutAllRetries();

  void incPutAllMsgsRetried();

  void incRemoveAllRetries();

  void incRemoveAllMsgsRetried();

  int getVolunteeringInProgress();

  int getVolunteeringBecamePrimary();

  long getVolunteeringBecamePrimaryTime();

  int getVolunteeringOtherPrimary();

  long getVolunteeringOtherPrimaryTime();

  int getVolunteeringClosed();

  long getVolunteeringClosedTime();

  long startVolunteering();

  void endVolunteeringBecamePrimary(long start);

  void endVolunteeringOtherPrimary(long start);

  void endVolunteeringClosed(long start);

  int getTotalNumBuckets();

  void incTotalNumBuckets(int val);

  int getPrimaryBucketCount();

  void incPrimaryBucketCount(int val);

  int getVolunteeringThreads();

  void incVolunteeringThreads(int val);

  int getLowRedundancyBucketCount();

  int getNoCopiesBucketCount();

  void incLowRedundancyBucketCount(int val);

  void incNoCopiesBucketCount(int val);

  int getConfiguredRedundantCopies();

  void setConfiguredRedundantCopies(int val);

  void setLocalMaxMemory(long l);

  int getActualRedundantCopies();

  void setActualRedundantCopies(int val);

  void putStartTime(Object key, long startTime);

  long removeStartTime(Object key);

  void endGetEntry(long startTime);

  void endGetEntry(long start, int numInc);

  // ------------------------------------------------------------------------
  // bucket creation, primary transfer stats (see also rebalancing stats below)
  // ------------------------------------------------------------------------
  long startRecovery();

  void endRecovery(long start);

  long startBucketCreate(boolean isRebalance);

  void endBucketCreate(long start, boolean success, boolean isRebalance);

  long startPrimaryTransfer(boolean isRebalance);

  void endPrimaryTransfer(long start, boolean success, boolean isRebalance);

  int getBucketCreatesInProgress();

  int getBucketCreatesCompleted();

  int getBucketCreatesFailed();

  long getBucketCreateTime();

  int getPrimaryTransfersInProgress();

  int getPrimaryTransfersCompleted();

  int getPrimaryTransfersFailed();

  long getPrimaryTransferTime();

  int getRebalanceBucketCreatesInProgress();

  int getRebalanceBucketCreatesCompleted();

  int getRebalanceBucketCreatesFailed();

  long getRebalanceBucketCreateTime();

  int getRebalancePrimaryTransfersInProgress();

  int getRebalancePrimaryTransfersCompleted();

  int getRebalancePrimaryTransfersFailed();

  long getRebalancePrimaryTransferTime();

  long startApplyReplication();

  void endApplyReplication(long start);

  long startSendReplication();

  void endSendReplication(long start);

  long startPutRemote();

  void endPutRemote(long start);

  long startPutLocal();

  void endPutLocal(long start);

  void incPRMetaDataSentCount();

  long getPRMetaDataSentCount();
}

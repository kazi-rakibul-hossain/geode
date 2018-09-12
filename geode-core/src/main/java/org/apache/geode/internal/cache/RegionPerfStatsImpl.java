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
package org.apache.geode.internal.cache;

import org.apache.geode.internal.NanoTimer;
import org.apache.geode.stats.common.internal.cache.CachePerfStats;
import org.apache.geode.stats.common.internal.cache.RegionPerfStats;
import org.apache.geode.stats.common.statistics.StatisticsFactory;

public class RegionPerfStatsImpl extends CachePerfStatsImpl implements RegionPerfStats {
  CachePerfStats cachePerfStats;

  public RegionPerfStatsImpl(StatisticsFactory statisticsFactory, CachePerfStats superStats,
      String regionName) {
    super(statisticsFactory, regionName);
    this.cachePerfStats = superStats;
  }

  @Override
  public void incReliableQueuedOps(int inc) {
    this.stats.incInt(reliableQueuedOpsId, inc);
    this.cachePerfStats.incReliableQueuedOps(inc);
  }

  @Override
  public void incReliableQueueSize(int inc) {
    this.stats.incInt(reliableQueueSizeId, inc);
    this.cachePerfStats.incReliableQueueSize(inc);
  }

  @Override
  public void incReliableQueueMax(int inc) {
    this.stats.incInt(reliableQueueMaxId, inc);
    this.cachePerfStats.incReliableQueueMax(inc);
  }

  @Override
  public void incReliableRegions(int inc) {
    this.stats.incInt(reliableRegionsId, inc);
    this.cachePerfStats.incReliableRegions(inc);
  }

  @Override
  public void incReliableRegionsMissing(int inc) {
    this.stats.incInt(reliableRegionsMissingId, inc);
    this.cachePerfStats.incReliableRegionsMissing(inc);
  }

  @Override
  public void incReliableRegionsQueuing(int inc) {
    this.stats.incInt(reliableRegionsQueuingId, inc);
    this.cachePerfStats.incReliableRegionsQueuing(inc);
  }

  @Override
  public void incReliableRegionsMissingFullAccess(int inc) {
    this.stats.incInt(reliableRegionsMissingFullAccessId, inc);
    this.cachePerfStats.incReliableRegionsMissingFullAccess(inc);
  }

  @Override
  public void incReliableRegionsMissingLimitedAccess(int inc) {
    this.stats.incInt(reliableRegionsMissingLimitedAccessId, inc);
    this.cachePerfStats.incReliableRegionsMissingLimitedAccess(inc);
  }

  @Override
  public void incReliableRegionsMissingNoAccess(int inc) {
    this.stats.incInt(reliableRegionsMissingNoAccessId, inc);
    this.cachePerfStats.incReliableRegionsMissingNoAccess(inc);
  }

  @Override
  public void incQueuedEvents(int inc) {
    this.stats.incLong(eventsQueuedId, inc);
    this.cachePerfStats.incQueuedEvents(inc);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startLoad() {
    this.stats.incInt(loadsInProgressId, 1);
    return this.cachePerfStats.startLoad();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endLoad(long start) {
    // note that load times are used in health checks and
    // should not be disabled by enableClockStats==false

    // don't use getStatTime so always enabled
    long ts = NanoTimer.getTime();
    this.stats.incLong(loadTimeId, ts - start);
    this.stats.incInt(loadsInProgressId, -1);
    this.stats.incInt(loadsCompletedId, 1);

    // need to think about timings
    this.cachePerfStats.endLoad(start);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startNetload() {
    this.stats.incInt(netloadsInProgressId, 1);
    this.cachePerfStats.startNetload();
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endNetload(long start) {
    if (enableClockStats) {
      this.stats.incLong(netloadTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(netloadsInProgressId, -1);
    this.stats.incInt(netloadsCompletedId, 1);
    this.cachePerfStats.endNetload(start);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startNetsearch() {
    this.stats.incInt(netsearchesInProgressId, 1);
    return this.cachePerfStats.startNetsearch();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endNetsearch(long start) {
    // note that netsearch is used in health checks and timings should
    // not be disabled by enableClockStats==false

    // don't use getStatTime so always enabled
    long ts = NanoTimer.getTime();
    this.stats.incLong(netsearchTimeId, ts - start);
    this.stats.incInt(netsearchesInProgressId, -1);
    this.stats.incInt(netsearchesCompletedId, 1);
    this.cachePerfStats.endNetsearch(start);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startCacheWriterCall() {
    this.stats.incInt(cacheWriterCallsInProgressId, 1);
    this.cachePerfStats.startCacheWriterCall();
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endCacheWriterCall(long start) {
    if (enableClockStats) {
      this.stats.incLong(cacheWriterCallTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(cacheWriterCallsInProgressId, -1);
    this.stats.incInt(cacheWriterCallsCompletedId, 1);
    this.cachePerfStats.endCacheWriterCall(start);
  }

  /**
   * @return the timestamp that marks the start of the operation
   * @since GemFire 3.5
   */
  @Override
  public long startCacheListenerCall() {
    this.stats.incInt(cacheListenerCallsInProgressId, 1);
    this.cachePerfStats.startCacheListenerCall();
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   * @since GemFire 3.5
   */
  @Override
  public void endCacheListenerCall(long start) {
    if (enableClockStats) {
      this.stats.incLong(cacheListenerCallTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(cacheListenerCallsInProgressId, -1);
    this.stats.incInt(cacheListenerCallsCompletedId, 1);
    this.cachePerfStats.endCacheListenerCall(start);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startGetInitialImage() {
    this.stats.incInt(getInitialImagesInProgressId, 1);
    this.cachePerfStats.startGetInitialImage();
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endGetInitialImage(long start) {
    if (enableClockStats) {
      this.stats.incLong(getInitialImageTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(getInitialImagesInProgressId, -1);
    this.stats.incInt(getInitialImagesCompletedId, 1);
    this.cachePerfStats.endGetInitialImage(start);
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endNoGIIDone(long start) {
    if (enableClockStats) {
      this.stats.incLong(getInitialImageTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(getInitialImagesInProgressId, -1);
    this.cachePerfStats.endNoGIIDone(start);
  }

  @Override
  public void incGetInitialImageKeysReceived() {
    this.stats.incInt(getInitialImageKeysReceivedId, 1);
    this.cachePerfStats.incGetInitialImageKeysReceived();
  }

  @Override
  public long startIndexUpdate() {
    this.stats.incInt(indexUpdateInProgressId, 1);
    this.cachePerfStats.startIndexUpdate();
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endIndexUpdate(long start) {
    long ts = CachePerfStats.getStatTime();
    this.stats.incLong(indexUpdateTimeId, ts - start);
    this.stats.incInt(indexUpdateInProgressId, -1);
    this.stats.incInt(indexUpdateCompletedId, 1);
    this.cachePerfStats.endIndexUpdate(start);
  }

  @Override
  public void incRegions(int inc) {
    this.stats.incInt(regionsId, inc);
    this.cachePerfStats.incRegions(inc);

  }

  @Override
  public void incPartitionedRegions(int inc) {
    this.stats.incInt(partitionedRegionsId, inc);
    this.cachePerfStats.incPartitionedRegions(inc);
  }

  @Override
  public void incDestroys() {
    this.stats.incInt(destroysId, 1);
    this.cachePerfStats.incDestroys();
  }

  @Override
  public void incCreates() {
    this.stats.incInt(createsId, 1);
    this.cachePerfStats.incCreates();
  }

  @Override
  public void incInvalidates() {
    this.stats.incInt(invalidatesId, 1);
    this.cachePerfStats.incInvalidates();
  }

  @Override
  public void incTombstoneCount(int amount) {
    this.stats.incInt(tombstoneCountId, amount);
    this.cachePerfStats.incTombstoneCount(amount);
  }

  @Override
  public void incTombstoneGCCount() {
    this.stats.incInt(tombstoneGCCountId, 1);
    this.cachePerfStats.incTombstoneGCCount();
  }

  @Override
  public void incClearTimeouts() {
    this.stats.incInt(clearTimeoutsId, 1);
    this.cachePerfStats.incClearTimeouts();
  }

  @Override
  public void incConflatedEventsCount() {
    this.stats.incLong(conflatedEventsId, 1);
    this.cachePerfStats.incConflatedEventsCount();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endGet(long start, boolean miss) {
    if (enableClockStats) {
      this.stats.incLong(getTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(getsId, 1);
    if (miss) {
      this.stats.incInt(missesId, 1);
    }
    this.cachePerfStats.endGet(start, miss);
  }

  /**
   * @param start the timestamp taken when the operation started
   * @param isUpdate true if the put was an update (origin remote)
   */
  @Override
  public long endPut(long start, boolean isUpdate) {
    long total = 0;
    if (isUpdate) {
      this.stats.incInt(updatesId, 1);
      if (enableClockStats) {
        total = CachePerfStats.getStatTime() - start;
        this.stats.incLong(updateTimeId, total);
      }
    } else {
      this.stats.incInt(putsId, 1);
      if (enableClockStats) {
        total = CachePerfStats.getStatTime() - start;
        this.stats.incLong(putTimeId, total);
      }
    }
    this.cachePerfStats.endPut(start, isUpdate);
    return total;
  }

  @Override
  public void endPutAll(long start) {
    this.stats.incInt(putallsId, 1);
    if (enableClockStats) {
      this.stats.incLong(putallTimeId, CachePerfStats.getStatTime() - start);
    }
    this.cachePerfStats.endPutAll(start);
  }

  @Override
  public void endQueryExecution(long executionTime) {
    this.stats.incInt(queryExecutionsId, 1);
    if (enableClockStats) {
      this.stats.incLong(queryExecutionTimeId, executionTime);
    }
    this.cachePerfStats.endQueryExecution(executionTime);
  }

  @Override
  public void endQueryResultsHashCollisionProbe(long start) {
    if (enableClockStats) {
      this.stats
          .incLong(queryResultsHashCollisionProbeTimeId, CachePerfStats.getStatTime() - start);
    }
    this.cachePerfStats.endQueryResultsHashCollisionProbe(start);
  }

  @Override
  public void incQueryResultsHashCollisions() {
    this.stats.incInt(queryResultsHashCollisionsId, 1);
    this.cachePerfStats.incQueryResultsHashCollisions();
  }

  @Override
  public void incTxConflictCheckTime(long delta) {
    this.stats.incLong(txConflictCheckTimeId, delta);
    this.cachePerfStats.incTxConflictCheckTime(delta);
  }

  @Override
  public void txSuccess(long opTime, long txLifeTime, int txChanges) {
    this.stats.incInt(txCommitsId, 1);
    this.stats.incInt(txCommitChangesId, txChanges);
    this.stats.incLong(txCommitTimeId, opTime);
    this.stats.incLong(txSuccessLifeTimeId, txLifeTime);
    this.cachePerfStats.txSuccess(opTime, txLifeTime, txChanges);
  }

  @Override
  public void txFailure(long opTime, long txLifeTime, int txChanges) {
    this.stats.incInt(txFailuresId, 1);
    this.stats.incInt(txFailureChangesId, txChanges);
    this.stats.incLong(txFailureTimeId, opTime);
    this.stats.incLong(txFailedLifeTimeId, txLifeTime);
    this.cachePerfStats.txFailure(opTime, txLifeTime, txChanges);
  }

  @Override
  public void txRollback(long opTime, long txLifeTime, int txChanges) {
    this.stats.incInt(txRollbacksId, 1);
    this.stats.incInt(txRollbackChangesId, txChanges);
    this.stats.incLong(txRollbackTimeId, opTime);
    this.stats.incLong(txRollbackLifeTimeId, txLifeTime);
    this.cachePerfStats.txRollback(opTime, txLifeTime, txChanges);
  }

  @Override
  public void incEventQueueSize(int items) {
    this.stats.incInt(eventQueueSizeId, items);
    this.cachePerfStats.incEventQueueSize(items);
  }

  @Override
  public void incEventQueueThrottleCount(int items) {
    this.stats.incInt(eventQueueThrottleCountId, items);
    this.cachePerfStats.incEventQueueThrottleCount(items);
  }

  @Override
  public void incEventQueueThrottleTime(long nanos) {
    this.stats.incLong(eventQueueThrottleTimeId, nanos);
    this.cachePerfStats.incEventQueueThrottleTime(nanos);
  }

  @Override
  public void incEventThreads(int items) {
    this.stats.incInt(eventThreadsId, items);
    this.cachePerfStats.incEventThreads(items);
  }

  @Override
  public void incEntryCount(int delta) {
    this.stats.incLong(entryCountId, delta);
    this.cachePerfStats.incEntryCount(delta);
  }

  @Override
  public void incRetries() {
    this.stats.incInt(retriesId, 1);
    this.cachePerfStats.incRetries();
  }

  @Override
  public void incDiskTasksWaiting() {
    this.stats.incInt(diskTasksWaitingId, 1);
    this.cachePerfStats.incDiskTasksWaiting();
  }

  @Override
  public void decDiskTasksWaiting() {
    this.stats.incInt(diskTasksWaitingId, -1);
    this.cachePerfStats.decDiskTasksWaiting();
  }

  @Override
  public void decDiskTasksWaiting(int count) {
    this.stats.incInt(diskTasksWaitingId, -count);
    this.cachePerfStats.decDiskTasksWaiting(count);
  }

  @Override
  public void incEvictorJobsStarted() {
    this.stats.incInt(evictorJobsStartedId, 1);
    this.cachePerfStats.incEvictorJobsStarted();
  }

  @Override
  public void incEvictorJobsCompleted() {
    this.stats.incInt(evictorJobsCompletedId, 1);
    this.cachePerfStats.incEvictorJobsCompleted();
  }

  @Override
  public void incEvictorQueueSize(int delta) {
    this.stats.incInt(evictorQueueSizeId, delta);
    this.cachePerfStats.incEvictorQueueSize(delta);
  }

  @Override
  public void incEvictWorkTime(long delta) {
    this.stats.incLong(evictWorkTimeId, delta);
    this.cachePerfStats.incEvictWorkTime(delta);
  }

  @Override
  public void incClearCount() {
    this.stats.incInt(clearsId, 1);
    this.cachePerfStats.incClearCount();
  }

  @Override
  public void incPRQueryRetries() {
    this.stats.incLong(partitionedRegionQueryRetriesId, 1);
    this.cachePerfStats.incPRQueryRetries();
  }

  @Override
  public void incMetaDataRefreshCount() {
    this.stats.incLong(metaDataRefreshCountId, 1);
    this.cachePerfStats.incMetaDataRefreshCount();
  }

  @Override
  public void endImport(long entryCount, long start) {
    this.stats.incLong(importedEntriesCountId, entryCount);
    if (enableClockStats) {
      this.stats.incLong(importTimeId, CachePerfStats.getStatTime() - start);
    }
    this.cachePerfStats.endImport(entryCount, start);
  }

  @Override
  public void endExport(long entryCount, long start) {
    this.stats.incLong(exportedEntriesCountId, entryCount);
    if (enableClockStats) {
      this.stats.incLong(exportTimeId, CachePerfStats.getStatTime() - start);
    }
    this.cachePerfStats.endExport(entryCount, start);
  }

  @Override
  public long startCompression() {
    this.incEntryCompressedCount();
    this.cachePerfStats.incEntryCompressedCount();
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endCompression(long startTime, long startSize, long endSize) {
    if (enableClockStats) {
      long time = CachePerfStats.getStatTime() - startTime;
      this.incEntryCompressedTime(time);
      this.cachePerfStats.incEntryCompressedTime(time);
    }

    this.stats.incLong(compressionPreCompressedBytesId, startSize);
    this.stats.incLong(compressionPostCompressedBytesId, endSize);

    this.cachePerfStats.incPreCompressedBytes(startSize);
    this.cachePerfStats.incPostCompressedBytes(endSize);
  }

  @Override
  public long startDecompression() {
    this.incDecompressedEntryCount();
    this.cachePerfStats.incDecompressedEntryCount();
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endDecompression(long startTime) {
    if (enableClockStats) {
      long time = CachePerfStats.getStatTime() - startTime;
      this.incDecompressionTime(time);
      this.cachePerfStats.incDecompressionTime(time);
    }
  }
}

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

public interface DiskStoreStats extends Stats {
  void close();

  long getWrites();

  long getWriteTime();

  long getBytesWritten();

  long getReads();

  long getReadTime();

  long getBytesRead();

  long getRemoves();

  long getRemoveTime();

  long getQueueSize();

  void setQueueSize(int value);

  void incQueueSize(int delta);

  void incUncreatedRecoveredRegions(int delta);

  long startWrite();

  long startFlush();

  void incWrittenBytes(long bytesWritten, boolean async);

  long endWrite(long start);

  void endFlush(long start);

  long getFlushes();

  long startRead();

  long endRead(long start, long bytesRead);

  long startRecovery();

  long startCompaction();

  long startOplogRead();

  void endRecovery(long start, long bytesRead);

  void endCompaction(long start);

  void endOplogRead(long start, long bytesRead);

  void incRecoveredEntryCreates();

  void incRecoveredEntryUpdates();

  void incRecoveredEntryDestroys();

  void incRecoveryRecordsSkipped();

  void incRecoveredValuesSkippedDueToLRU();

  long startRemove();

  long endRemove(long start);

  void incOplogReads();

  void incOplogSeeks();

  void incInactiveOplogs(int delta);

  void incCompactableOplogs(int delta);

  void endCompactionDeletes(int count, long delta);

  void endCompactionInsert(long start);

  void endCompactionUpdate(long start);

  long getStatTime();

  void incOpenOplogs();

  void decOpenOplogs();

  void startBackup();

  void endBackup();

  Statistics getStats();
}

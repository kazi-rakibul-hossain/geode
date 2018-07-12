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
package org.apache.geode.stats.common.internal.cache.tier.sockets;

import org.apache.geode.stats.common.Stats;

public interface CacheClientNotifierStats extends Stats {
  void close();

  int getEvents();

  long getEventProcessingTime();

  long startTime();

  void endEvent(long start);

  void endClientRegistration(long start);

  void endCqProcessing(long start);

  void incClientRegisterRequests();

  int getClientRegisterRequests();

  int get_durableReconnectionCount();

  int get_queueDroppedCount();

  int get_eventEnqueuedWhileClientAwayCount();

  long getCqProcessingTime();

  long getCompiledQueryCount();

  long getCompiledQueryUsedCount();

  void incDurableReconnectionCount();

  void incQueueDroppedCount();

  void incEventEnqueuedWhileClientAwayCount();

  void incClientUnRegisterRequests();

  void incCompiledQueryCount(long count);

  void incCompiledQueryUsedCount(long count);

  int getClientUnRegisterRequests();
}

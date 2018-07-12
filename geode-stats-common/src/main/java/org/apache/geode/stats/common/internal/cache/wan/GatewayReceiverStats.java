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
package org.apache.geode.stats.common.internal.cache.wan;

import org.apache.geode.stats.common.internal.cache.tier.sockets.CacheServerStats;

public interface GatewayReceiverStats extends CacheServerStats {
  void incDuplicateBatchesReceived();

  int getDuplicateBatchesReceived();

  void incOutoforderBatchesReceived();

  int getOutoforderBatchesReceived();

  void incEarlyAcks();

  int getEarlyAcks();

  void incEventsReceived(int delta);

  int getEventsReceived();

  void incCreateRequest();

  int getCreateRequest();

  void incUpdateRequest();

  int getUpdateRequest();

  void incDestroyRequest();

  int getDestroyRequest();

  void incUnknowsOperationsReceived();

  int getUnknowsOperationsReceived();

  void incExceptionsOccurred();

  int getExceptionsOccurred();

  void incEventsRetried();

  int getEventsRetried();
}

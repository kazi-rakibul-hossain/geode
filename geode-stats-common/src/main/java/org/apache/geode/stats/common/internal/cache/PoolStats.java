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

public interface PoolStats extends Stats {
  static long getStatTime() {
    return System.nanoTime();
  }

  void close();

  long startTime();

  void setInitialContacts(int ic);

  void setServerCount(int sc);

  void setSubscriptionCount(int qc);

  void setLocatorCount(int lc);

  long getLocatorRequests();

  void incLocatorRequests();

  void incLocatorResponses();

  void setLocatorRequests(long rl);

  void setLocatorResponses(long rl);

  // public void incConCount(int delta) {
  // this._stats.incInt(conCountId, delta);
  // }
  void incConnections(int delta);

  void incPoolConnections(int delta);

  int getPoolConnections();

  int getConnects();

  int getDisconnects();

  void incPrefillConnect();

  int getLoadConditioningCheck();

  void incLoadConditioningCheck();

  int getLoadConditioningExtensions();

  void incLoadConditioningExtensions();

  void incIdleCheck();

  int getLoadConditioningConnect();

  void incLoadConditioningConnect();

  int getLoadConditioningReplaceTimeouts();

  void incLoadConditioningReplaceTimeouts();

  int getLoadConditioningDisconnect();

  void incLoadConditioningDisconnect();

  int getIdleExpire();

  void incIdleExpire(int delta);

  long beginConnectionWait();

  void endConnectionWait(long start);

  void startClientOp();

  void endClientOpSend(long duration, boolean failed);

  void endClientOp(long duration, boolean timedOut, boolean failed);
}

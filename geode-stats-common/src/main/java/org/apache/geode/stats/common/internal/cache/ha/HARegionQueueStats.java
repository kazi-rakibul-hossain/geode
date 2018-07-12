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
package org.apache.geode.stats.common.internal.cache.ha;

import org.apache.geode.stats.common.Stats;

public interface HARegionQueueStats extends Stats {
  /** Name of the events queued statistic */
  String EVENTS_QUEUED = "eventsQueued";

  void close();

  long getEventsEnqued();

  void incEventsEnqued();

  long getEventsConflated();

  void incEventsConflated();

  long getMarkerEventsConflated();

  void incMarkerEventsConflated();

  long getEventsRemoved();

  void incEventsRemoved();

  long getEventsTaken();

  void incEventsTaken();

  long getEventsExpired();

  void incEventsExpired();

  long getEventsRemovedByQrm();

  void incEventsRemovedByQrm();

  int getThreadIdentiferCount();

  void incThreadIdentifiers();

  void decThreadIdentifiers();

  long getEventsDispatched();

  void incEventsDispatched();

  long getNumVoidRemovals();

  void incNumVoidRemovals();

  long getNumSequenceViolated();

  void incNumSequenceViolated();

  boolean isClosed();
}

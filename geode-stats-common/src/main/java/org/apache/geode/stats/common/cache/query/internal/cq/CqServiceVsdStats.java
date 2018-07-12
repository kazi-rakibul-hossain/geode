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
package org.apache.geode.stats.common.cache.query.internal.cq;

import org.apache.geode.stats.common.Stats;

public interface CqServiceVsdStats extends Stats {
  void close();

  long getNumCqsCreated();

  void incCqsCreated();

  long getNumCqsActive();

  void incCqsActive();

  void decCqsActive();

  long getNumCqsStopped();

  void incCqsStopped();

  void decCqsStopped();

  long getNumCqsClosed();

  void incCqsClosed();

  long getNumCqsOnClient();

  void incCqsOnClient();

  void decCqsOnClient();

  long getNumClientsWithCqs();

  void incClientsWithCqs();

  void decClientsWithCqs();

  long startCqQueryExecution();

  void endCqQueryExecution(long start);

  long getCqQueryExecutionTime();

  void incUniqueCqQuery();

  void decUniqueCqQuery();
}

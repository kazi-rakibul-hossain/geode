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
package org.apache.geode.stats.common.internal.cache.execute;

import org.apache.geode.stats.common.Stats;

public interface FunctionStats extends Stats {

  void close();

  int getFunctionExecutionsCompleted();

  void incFunctionExecutionsCompleted();

  long getFunctionExecutionCompleteProcessingTime();

  int getFunctionExecutionsRunning();

  void incFunctionExecutionsRunning();

  int getResultsSentToResultCollector();

  void incResultsReturned();

  int getResultsReceived();

  void incResultsReceived();

  int getFunctionExecutionCalls();

  void incFunctionExecutionCalls();

  int getFunctionExecutionHasResultCompleteProcessingTime();

  int getFunctionExecutionHasResultRunning();

  void incFunctionExecutionHasResultRunning();

  int getFunctionExecutionExceptions();

  void incFunctionExecutionExceptions();

  long startTime();

  void startFunctionExecution(boolean haveResult);

  void endFunctionExecution(long start, boolean haveResult);

  void endFunctionExecutionWithException(boolean haveResult);
}

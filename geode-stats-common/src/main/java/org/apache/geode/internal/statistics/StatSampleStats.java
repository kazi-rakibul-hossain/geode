package org.apache.geode.internal.statistics;

import org.apache.geode.stats.common.Stats;

public interface StatSampleStats extends Stats {
  void incJvmPauses();

  void incSampleCallbackErrors(int delta);

  void setSampleCallbacks(int count);

  void incSampleCallbackDuration(long delta);

  int getSampleCount();

  long getSampleTime();

  int getDelayDuration();

  int getStatResources();

  int getJvmPauses();
}

package com.example.demo.searchsystem.strategy;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class PerformanceMeasurer {
  public static <T> T measureExecutionTime(Supplier<T> task, TimeResult timeResult) {
        long startTime = System.nanoTime();
        T result = task.get();
        long endTime = System.nanoTime();
        timeResult.setTime(TimeUnit.NANOSECONDS.toMicros(endTime - startTime));
        return result;
    }
    
    public static class TimeResult {
        private long time;
        public void setTime(long time) { this.time = time; }
        public long getTime() { return time; }
    }
}

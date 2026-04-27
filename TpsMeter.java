package com.eugamehost.metrics.publicsource;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Tracks recent server ticks and calculates rolling TPS averages.
 */
public final class TpsMeter {
    private static final int MAX_TPS = 20;
    private static final long MAX_WINDOW_MILLIS = 15L * 60L * 1000L;

    private final Deque<Long> ticks = new ArrayDeque<>();

    public synchronized void recordTick(long nowMillis) {
        ticks.addLast(nowMillis);

        long oldestAllowed = nowMillis - MAX_WINDOW_MILLIS;
        while (!ticks.isEmpty() && ticks.peekFirst() < oldestAllowed) {
            ticks.removeFirst();
        }
    }

    public synchronized double tps(long nowMillis, int windowSeconds) {
        long windowMillis = windowSeconds * 1000L;
        long windowStart = nowMillis - windowMillis;
        int tickCount = 0;

        for (Long tickTime : ticks) {
            if (tickTime >= windowStart) {
                tickCount++;
            }
        }

        double measured = tickCount / (windowMillis / 1000.0D);
        return Math.min(MAX_TPS, Math.max(0, round(measured)));
    }

    private static double round(double value) {
        return Math.round(value * 100.0D) / 100.0D;
    }
}


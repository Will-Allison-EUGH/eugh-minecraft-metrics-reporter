package com.eugamehost.metrics.publicsource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Shared reporter loop used by the Bukkit, Fabric, and Forge builds.
 */
public final class TpsReporter {
    private static final long FAILURE_LOG_INTERVAL_MILLIS = 15L * 60L * 1000L;

    private final TpsMeter meter = new TpsMeter();
    private final MetricsPoster poster = new MetricsPoster();
    private final AtomicBoolean posting = new AtomicBoolean(false);

    private ReporterConfig config;
    private long lastPostMinute = System.currentTimeMillis() / 60000L;
    private long lastFailureLogMillis = 0L;

    public TpsReporter(ReporterConfig config) {
        this.config = config;
    }

    public void updateConfig(ReporterConfig config) {
        this.config = config;
    }

    public void onServerTick() {
        long now = System.currentTimeMillis();
        meter.recordTick(now);

        ReporterConfig current = config;
        if (!current.isReady()) {
            return;
        }

        long currentMinute = now / 60000L;
        int intervalMinutes = Math.max(1, (int) Math.ceil(current.intervalSeconds() / 60.0D));

        if (currentMinute == lastPostMinute) {
            return;
        }

        if (currentMinute % intervalMinutes != 0) {
            return;
        }

        if (!posting.compareAndSet(false, true)) {
            return;
        }

        lastPostMinute = currentMinute;

        long sampleTimestampMillis = currentMinute * 60000L;
        double tps1m = meter.tps(now, 60);
        double tps5m = meter.tps(now, 60 * 5);
        double tps15m = meter.tps(now, 60 * 15);

        CompletableFuture.runAsync(() -> {
            try {
                boolean posted = poster.post(current, sampleTimestampMillis, tps1m, tps5m, tps15m);
                if (!posted && shouldLogFailure()) {
                    logWarning("TPS metrics post failed. Further failures will be quiet for 15 minutes.");
                }
            } finally {
                posting.set(false);
            }
        });
    }

    private synchronized boolean shouldLogFailure() {
        long now = System.currentTimeMillis();
        if (now - lastFailureLogMillis < FAILURE_LOG_INTERVAL_MILLIS) {
            return false;
        }

        lastFailureLogMillis = now;
        return true;
    }

    private void logWarning(String message) {
        System.out.println("[EUGHTpsReporter] WARN: " + message);
    }
}


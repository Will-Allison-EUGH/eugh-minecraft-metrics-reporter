package com.eugamehost.metrics.publicsource;

/**
 * Runtime settings written by the panel installer.
 *
 * The real reporter config includes panel-specific connection details.
 * Those values are deliberately not shown here.
 */
public final class ReporterConfig {
    private final boolean enabled;
    private final int intervalSeconds;
    private final int timeoutMillis;
    private final boolean panelConnectionConfigured;

    public ReporterConfig(
        boolean enabled,
        int intervalSeconds,
        int timeoutMillis,
        boolean panelConnectionConfigured
    ) {
        this.enabled = enabled;
        this.intervalSeconds = intervalSeconds;
        this.timeoutMillis = timeoutMillis;
        this.panelConnectionConfigured = panelConnectionConfigured;
    }

    public boolean isReady() {
        return enabled && panelConnectionConfigured && intervalSeconds > 0 && timeoutMillis > 0;
    }

    public int intervalSeconds() {
        return intervalSeconds;
    }

    public int timeoutMillis() {
        return timeoutMillis;
    }
}


package com.eugamehost.metrics.publicsource;

/**
 * Forge-family entry point shape.
 */
public final class ForgeEntry {
    private TpsReporter reporter;

    public ForgeEntry() {
        ReporterConfig config = loadConfigWrittenByPanel();
        reporter = new TpsReporter(config);

        /*
         * The real Forge build supports multiple Forge event bus shapes so it
         * can work across modern Forge and NeoForge-style versions.
         */
        registerServerTickListener(() -> reporter.onServerTick());
    }

    private ReporterConfig loadConfigWrittenByPanel() {
        return new ReporterConfig(true, 60, 5000, true);
    }

    private void registerServerTickListener(Runnable task) {
        // Loader-specific registration details are omitted from this public source view.
    }
}


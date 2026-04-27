package com.eugamehost.metrics.publicsource;

/**
 * Fabric entry point shape.
 */
public final class FabricEntry {
    private TpsReporter reporter;

    public void onInitialize() {
        ReporterConfig config = loadConfigWrittenByPanel();
        reporter = new TpsReporter(config);

        /*
         * The real Fabric build registers against the server tick lifecycle.
         */
        registerEndServerTick(() -> reporter.onServerTick());
    }

    private ReporterConfig loadConfigWrittenByPanel() {
        return new ReporterConfig(true, 60, 5000, true);
    }

    private void registerEndServerTick(Runnable task) {
        // Loader-specific registration details are omitted from this public source view.
    }
}


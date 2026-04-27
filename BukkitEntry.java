package com.eugamehost.metrics.publicsource;

/**
 * Bukkit-family entry point shape.
 */
public final class BukkitEntry {
    private TpsReporter reporter;

    public void onEnable() {
        ReporterConfig config = loadConfigWrittenByPanel();
        reporter = new TpsReporter(config);

        /*
         * The real Bukkit build schedules this once per server tick.
         */
        scheduleRepeatingServerTickTask(() -> reporter.onServerTick());
    }

    private ReporterConfig loadConfigWrittenByPanel() {
        return new ReporterConfig(true, 60, 5000, true);
    }

    private void scheduleRepeatingServerTickTask(Runnable task) {
        // Platform scheduling details are omitted from this public source view.
    }
}


package com.eugamehost.metrics.publicsource;

/**
 * Sends the TPS sample to the panel.
 *
 * The public extract shows the payload shape only. Request authentication,
 * endpoint validation, and panel-side protection checks are intentionally not
 * included in this source view.
 */
public final class MetricsPoster {
    public boolean post(
        ReporterConfig config,
        long timestampMillis,
        double tps1m,
        double tps5m,
        double tps15m
    ) {
        String payload = jsonPayload(timestampMillis, tps1m, tps5m, tps15m);

        /*
         * The real reporter sends this JSON to the panel over HTTPS using
         * server-specific connection details written by the panel installer.
         */
        return sendToPanel(config, payload);
    }

    private static String jsonPayload(long timestampMillis, double tps1m, double tps5m, double tps15m) {
        return "{"
            + "\"timestamp\":" + timestampMillis + ","
            + "\"tps_1m\":" + tps1m + ","
            + "\"tps_5m\":" + tps5m + ","
            + "\"tps_15m\":" + tps15m
            + "}";
    }

    private boolean sendToPanel(ReporterConfig config, String payload) {
        // Connection/authentication details are intentionally omitted.
        return true;
    }
}


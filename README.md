# EUGameHost Minecraft TPS Reporter

This is the public inspection copy for our Minecraft TPS reporter.

We use this small plugin/mod to power the TPS graph in the EUGameHost panel. The files here show the parts customers usually care about:

- it listens for Minecraft server ticks
- it calculates 1 minute, 5 minute, and 15 minute TPS averages
- it reports once per clock minute
- it stays quiet if network posts keep failing
- it sends only TPS values and a timestamp

Some panel-facing bits are not included here, mainly request authentication, endpoint validation, and other server-side checks. Those are part of the hosted panel integration rather than the Minecraft-side TPS calculation.

This is here so you can see what the reporter does. It is not a full buildable plugin release.

## Files

```text
README.md          This overview
TpsMeter.java      Tick tracking and rolling TPS calculation
TpsReporter.java   Minute-aligned reporting loop
ReporterConfig.java
MetricsPoster.java
BukkitEntry.java
FabricEntry.java
ForgeEntry.java
```

## What It Sends

The reporter sends a small HTTPS update with TPS values and a timestamp.

Example payload:

```json
{
  "timestamp": 1777253100000,
  "tps_1m": 20.0,
  "tps_5m": 20.0,
  "tps_15m": 20.0
}
```

It does not send:

- chat messages
- console commands
- world files
- server files
- player IP addresses
- player inventories
- plugin lists
- mod lists
- environment variables
- database credentials

## Where It Goes

The panel picks the right reporter type automatically.

```text
Bukkit / Paper / Spigot / Purpur: plugins/EUGHTpsReporter.jar
Fabric:                           mods/EUGHTpsReporter.jar
Forge / NeoForge-style:            mods/EUGHTpsReporter.jar
```

The panel also writes the config for you. That config contains the panel endpoint, server-specific reporting details, interval, and timeout.

## How It Works

Minecraft normally aims for 20 ticks per second. The reporter records recent tick times in memory and uses those to calculate rolling averages.

It reports around normal clock minute boundaries:

```text
12:01
12:02
12:03
```

It does not wait exactly 60 seconds from when the server started. That keeps graph samples lined up cleanly in the panel.

## Logging

The reporter is meant to be quiet.

It may log a short startup message. If posting metrics keeps failing, warnings are rate-limited so the console does not get flooded.

## Compatibility

The reporter is aimed at modern Minecraft server software:

- Bukkit-family servers, including Bukkit, Spigot, Paper, Purpur, and plugin-capable hybrids
- Fabric
- Forge and NeoForge-style installs

The practical target is Minecraft 1.19 and newer.

On hybrid servers that support both plugins and mods, the panel may prefer the Bukkit/Paper-style reporter because that API is usually the most stable option for this sort of lightweight monitoring.

## Removing It

To stop TPS monitoring:

1. Stop the Minecraft server.
2. Delete `plugins/EUGHTpsReporter.jar` or `mods/EUGHTpsReporter.jar`.
3. Delete the reporter config if present.
4. Start the server again.

No new TPS samples are sent after the reporter is removed. Old graph data may stay visible until it ages out of the panel metrics history.

## Checksums

If the panel shows checksums for the reporter jars, you can compare the installed file with the panel value.

Example:

```bash
sha256sum plugins/EUGHTpsReporter.jar
```

The hash should match the value shown in the panel for that reporter build.

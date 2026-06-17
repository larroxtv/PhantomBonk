package dev.larrox.phantomBonk.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Typed, immutable view of config.yml.
 *
 * <p>Rebuilt from scratch on every {@code /phantombonk reload} via {@link #load}.
 * Nobody outside this class touches a raw {@link ConfigurationSection} for
 * spawn-rule data — that keeps the parsing quirks (case sensitivity, unknown
 * enum values, missing sections) in exactly one place.
 */
public final class PhantomConfig {

    /**
     * Rule for a single world. {@code disabled} exempts the world entirely
     * (phantoms always spawn, regardless of cause). {@code causeOverrides}
     * overrides individual spawn causes on top of the global defaults.
     */
    public record WorldRule(boolean disabled, Map<SpawnReason, Boolean> causeOverrides) {
    }

    private final boolean debug;
    private final Map<SpawnReason, Boolean> defaultCauses;
    private final Map<String, WorldRule> worldOverrides;

    private PhantomConfig(boolean debug,
                           Map<SpawnReason, Boolean> defaultCauses,
                           Map<String, WorldRule> worldOverrides) {
        this.debug = debug;
        this.defaultCauses = defaultCauses;
        this.worldOverrides = worldOverrides;
    }

    public static PhantomConfig load(FileConfiguration cfg) {
        boolean debug = cfg.getBoolean("settings.debug", false);
        Map<SpawnReason, Boolean> defaultCauses = parseCauseMap(cfg, "default-rules.causes");
        Map<String, WorldRule> worldOverrides = new HashMap<>();

        ConfigurationSection worldsSection = cfg.getConfigurationSection("world-overrides");
        if (worldsSection != null) {
            for (String worldName : worldsSection.getKeys(false)) {
                String base = "world-overrides." + worldName;
                boolean disabled = cfg.getBoolean(base + ".disabled", false);
                Map<SpawnReason, Boolean> causeOverrides = parseCauseMap(cfg, base + ".causes");
                worldOverrides.put(worldName.toLowerCase(), new WorldRule(disabled, causeOverrides));
            }
        }

        return new PhantomConfig(debug, defaultCauses, worldOverrides);
    }

    private static Map<SpawnReason, Boolean> parseCauseMap(FileConfiguration cfg, String path) {
        Map<SpawnReason, Boolean> result = new EnumMap<>(SpawnReason.class);
        ConfigurationSection section = cfg.getConfigurationSection(path);
        if (section == null) return result;

        for (String key : section.getKeys(false)) {
            parseReason(key).ifPresent(reason -> result.put(reason, cfg.getBoolean(path + "." + key)));
        }
        return result;
    }

    private static Optional<SpawnReason> parseReason(String raw) {
        try {
            return Optional.of(SpawnReason.valueOf(raw.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public boolean isDebug() {
        return debug;
    }

    /**
     * Resolves whether a phantom spawn should be cancelled for the given world + cause.
     * Per-world overrides win; unset causes fall back to the global default (false if
     * the cause isn't configured anywhere).
     */
    public boolean isCancelled(String worldName, SpawnReason reason) {
        WorldRule rule = worldOverrides.get(worldName.toLowerCase());

        if (rule != null) {
            if (rule.disabled()) return false;

            Boolean override = rule.causeOverrides().get(reason);
            if (override != null) return override;
        }

        return defaultCauses.getOrDefault(reason, false);
    }
}

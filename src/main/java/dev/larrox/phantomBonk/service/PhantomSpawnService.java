package dev.larrox.phantomBonk.service;

import dev.larrox.phantomBonk.PhantomBonk;
import dev.larrox.phantomBonk.config.PhantomConfig;
import org.bukkit.World;
import org.bukkit.entity.Phantom;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * All phantom-spawn decision logic lives here. The listener only triggers,
 * the command only reloads — neither touches config sections directly.
 */
public final class PhantomSpawnService {

    private final PhantomBonk plugin;

    public PhantomSpawnService(PhantomBonk plugin) {
        this.plugin = plugin;
    }

    public boolean shouldCancel(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Phantom)) return false;

        World world = event.getLocation().getWorld();
        if (world == null) return false;

        SpawnReason reason = event.getSpawnReason();
        PhantomConfig config = plugin.getPhantomConfig();
        boolean cancel = config.isCancelled(world.getName(), reason);

        if (config.isDebug()) {
            plugin.getLogger().info("[debug] phantom spawn world=" + world.getName()
                    + " reason=" + reason + " cancel=" + cancel);
        }

        return cancel;
    }
}

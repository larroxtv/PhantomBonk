package dev.larrox.phantomBonk.listener;

import dev.larrox.phantomBonk.service.PhantomSpawnService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public final class CreatureSpawnListener implements Listener {

    private final PhantomSpawnService spawnService;

    public CreatureSpawnListener(PhantomSpawnService spawnService) {
        this.spawnService = spawnService;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (spawnService.shouldCancel(event)) {
            event.setCancelled(true);
        }
    }
}

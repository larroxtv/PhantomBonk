package dev.larrox.phantomBonk.listener;

import dev.larrox.phantomBonk.PhantomBonk;
import org.bukkit.World;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

public class CreatureSpawn implements Listener {

    private final PhantomBonk plugin;

    public CreatureSpawn(PhantomBonk plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        if (!(event.getEntity() instanceof Phantom)) return;

        World world = event.getLocation().getWorld();
        List<String> disabledWorlds = plugin.getConfig().getStringList("disabled-worlds");

        if (world != null && disabledWorlds.contains(world.getName())) {
            return;
        }

        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (plugin.getConfig().getBoolean("cause." + reason.name(), false)) {
            event.setCancelled(true);
        }
    }
}
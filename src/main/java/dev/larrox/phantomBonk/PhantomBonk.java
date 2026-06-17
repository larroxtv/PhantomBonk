package dev.larrox.phantomBonk;

import dev.larrox.phantomBonk.command.ReloadCommand;
import dev.larrox.phantomBonk.config.PhantomConfig;
import dev.larrox.phantomBonk.listener.CreatureSpawnListener;
import dev.larrox.phantomBonk.service.PhantomSpawnService;
import dev.larrox.phantomBonk.util.MessageUtil;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhantomBonk extends JavaPlugin {

    private MessageUtil messages;
    private PhantomConfig phantomConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.messages = new MessageUtil(this);
        this.phantomConfig = PhantomConfig.load(getConfig());

        PhantomSpawnService spawnService = new PhantomSpawnService(this);
        getServer().getPluginManager().registerEvents(new CreatureSpawnListener(spawnService), this);

        ReloadCommand reloadCommand = new ReloadCommand(this, messages);
        PluginCommand command = getCommand("phantombonk");
        if (command != null) {
            command.setExecutor(reloadCommand);
            command.setTabCompleter(reloadCommand);
        } else {
            getLogger().warning("Command 'phantombonk' is missing from plugin.yml — reload command disabled.");
        }
    }

    public void reloadPlugin() {
        reloadConfig();
        this.phantomConfig = PhantomConfig.load(getConfig());
    }

    public MessageUtil getMessages() {
        return messages;
    }

    public PhantomConfig getPhantomConfig() {
        return phantomConfig;
    }
}

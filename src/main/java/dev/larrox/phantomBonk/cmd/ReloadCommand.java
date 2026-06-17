package dev.larrox.phantomBonk.cmd;

import dev.larrox.phantomBonk.PhantomBonk;
import dev.larrox.phantomBonk.util.MessageUtil;
import org.bukkit.command.*;

import java.util.Collections;
import java.util.List;

public class ReloadCommand implements TabExecutor {

    private final PhantomBonk plugin;
    private final MessageUtil messages;

    public ReloadCommand(PhantomBonk plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("phantombonk.reload") && !sender.isOp()) {
            sender.sendMessage(messages.format("no-permission"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(messages.format("reloaded"));
            return true;
        }

        sender.sendMessage(messages.format("usage"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 &&
                (sender.hasPermission("phantombonk.reload") || sender.isOp())) {
            return List.of("reload");
        }

        return Collections.emptyList();
    }
}
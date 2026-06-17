package dev.larrox.phantomBonk.util;

import dev.larrox.phantomBonk.PhantomBonk;
import org.bukkit.ChatColor;

public final class MessageUtil {

    private final PhantomBonk plugin;

    public MessageUtil(PhantomBonk plugin) {
        this.plugin = plugin;
    }

    public String format(String path) {
        return formatRaw(getMessage(path));
    }

    public String formatRaw(String message) {
        return color(message.replace("%prefix%", getPrefix()));
    }

    public String getMessage(String path) {
        return plugin.getConfig().getString("messages." + path, "&cMissing message: " + path);
    }

    public String getPrefix() {
        return color(plugin.getConfig().getString("messages.prefix", ""));
    }

    private String color(String msg) {
        if (msg == null) return "";
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

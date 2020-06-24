package me.minidigger.minimessage.plugin;

import net.kyori.adventure.platform.bukkit.BukkitPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.minidigger.minimessage.text.MiniMessageParser;
import me.minidigger.minimessage.text.ParseException;

public final class MiniMessagePlugin extends JavaPlugin {

    private BukkitPlatform platform;

    @Override
    public void onEnable() {
        platform = BukkitPlatform.of(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Component component = MiniMessageParser.parseFormat(String.join(" ", args));
            platform.audience(sender).sendMessage(component);
        } catch (ParseException ex) {
            sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
        }
        return true;
    }
}

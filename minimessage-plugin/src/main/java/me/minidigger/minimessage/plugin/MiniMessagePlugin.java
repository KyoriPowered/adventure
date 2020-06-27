package me.minidigger.minimessage.plugin;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.minidigger.minimessage.text.MiniMessageParser;
import me.minidigger.minimessage.text.ParseException;

public final class MiniMessagePlugin extends JavaPlugin implements Listener {

    private BukkitPlatform platform;

    private BukkitTask task;
    private int progress = 0;
    private BossBar bar;
    private Audience audience;

    @Override
    public void onEnable() {
        platform = BukkitPlatform.of(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] a) {
        String args = String.join(" ", a);
        switch (command.getName()) {
            case "minimessage":
                minimessage(sender, args);
                break;
            case "minibroadcast":
                minibroadcast(sender, args);
                break;
            case "minibossbar":
                minibossbar(sender);
                break;
            case "minispam":
                minispam(sender);
                break;
            default:
                return false;
        }
        return true;
    }

    private void minispam(CommandSender sender) {
        if (audience == null) {
            audience = Audience.of(platform.audience(sender));
        } else {
            audience = Audience.of(audience, platform.audience(sender));
        }
        if (task != null) {
            task.cancel();
            task = null;
            audience.hideBossBar(bar);
            audience.clearTitle();
        } else {
            bar = BossBar.of(getWithProgress(0), 1, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
            task = Bukkit.getScheduler().runTaskTimer(this, () -> {
                Component message = getWithProgress(progress++);
                bar.name(message);
                audience.sendActionBar(message);
                audience.sendMessage(message);
                audience.showTitle(Title.of(message, message, Duration.ZERO, Duration.of(50, ChronoUnit.MILLIS), Duration.ZERO));
            }, 1, 1);
            audience.showBossBar(bar);
        }
    }

    private void minibossbar(CommandSender sender) {
        Audience audience = platform.audience(sender);
        if (task != null) {
            task.cancel();
            audience.hideBossBar(bar);
        } else {
            bar = BossBar.of(getWithProgress(0), 1, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
            task = Bukkit.getScheduler().runTaskTimer(this, () -> {
                bar.name(getWithProgress(progress++));
            }, 1, 1);
            audience.showBossBar(bar);
        }
    }

    private void minimessage(CommandSender sender, String args) {
        try {
            Component component = MiniMessageParser.parseFormat(args);
            platform.audience(sender).sendMessage(component);
        } catch (ParseException ex) {
            sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
        }
    }

    private void minibroadcast(CommandSender sender, String args) {
        try {
            Component component = MiniMessageParser.parseFormat(args);
            platform.players().sendMessage(component);
        } catch (ParseException ex) {
            sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
        }
    }

    private Component getWithProgress(int phase) {
        return MiniMessageParser.parseFormat("<rainbow:" + phase + ">|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||</rainbow>");
    }
}

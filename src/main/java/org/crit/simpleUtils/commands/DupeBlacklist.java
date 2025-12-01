package org.crit.simpleUtils.commands;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.logging.Logger;


import java.util.List;
import java.util.UUID;


public class DupeBlacklist implements CommandExecutor {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DupeBlacklist.class);
    ///  LANGUAGE

    private final JavaPlugin plugin;
    public DupeBlacklist(JavaPlugin plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender executor, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        FileConfiguration config = plugin.getConfig();
        List<String> currentList = config.getStringList("Blacklisted_Players_dupe");
        String player_name = args[0];
        Player Blacklistee = Bukkit.getPlayer(player_name);
        String Prefix = config.getString("prefix");

        if (Blacklistee == null) {
            executor.sendMessage(Prefix + "Player " + player_name + " is not online.");
            return false;
        }

        UUID uuid = Blacklistee.getUniqueId();
        TextComponent actionBar = Component.text(Objects.requireNonNull(config.getString("RUN_AGAIN_DUPE")));

        if (!currentList.contains(uuid.toString())) {
            currentList.add(uuid.toString());
            executor.sendMessage(Prefix + "Added " + player_name + " to the blacklist!");
            Blacklistee.sendActionBar(actionBar);
        } else {
            currentList.remove(uuid.toString());
            executor.sendMessage(Prefix + "Removed " + player_name + " from the blacklist!");
        }

        config.set("Blacklisted_Players_dupe", currentList);
        plugin.saveConfig();

        log.warn("Updated blacklist: " + currentList);

        return true;
    }

}

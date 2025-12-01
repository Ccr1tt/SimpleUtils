package org.crit.simpleUtils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.print.attribute.standard.MediaSize;
import java.util.HashSet;
import java.util.Set;


public class VanishCommand implements CommandExecutor {
    ///  LANGUAGE

    private final JavaPlugin plugin;
    private static final Set<Player> vanishedPlayers = new HashSet<>();
    public VanishCommand(JavaPlugin plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender executor, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // CORE
        FileConfiguration config = plugin.getConfig();
        String nopermission = config.getString("no_permission");
        String Prefix = config.getString("prefix");
        String Vanished = config.getString("vanished");
        String Unvanished = config.getString("unvanished");

        Player player = (Player) executor;

        if (!executor.hasPermission("vanish")) {
            executor.sendMessage(Prefix + nopermission);
            return true;
        }

        Player target_player = null;
        if (args.length > 0) {
            target_player = Bukkit.getPlayer(args[0]);
            if (target_player == null) {
                executor.sendMessage(Prefix + "Player not found: " + args[0]);
                return false;
            }
        }

        if (target_player == null) {
            target_player = player;
            executor.sendMessage(Prefix + "Changed vanish status for " + target_player.getName());
        }

        if (vanishedPlayers.contains(target_player)) {
            vanishedPlayers.remove(target_player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, target_player);
                target_player.sendMessage(Prefix + Unvanished);
            }
        } else {
            vanishedPlayers.add(target_player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(plugin, target_player);
                target_player.sendMessage(Prefix + Vanished);
            }
        }

        return true;
    }

}

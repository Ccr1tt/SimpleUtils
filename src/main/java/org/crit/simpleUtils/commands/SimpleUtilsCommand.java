package org.crit.simpleUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.crit.simpleUtils.Util.Statistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class SimpleUtilsCommand implements CommandExecutor, TabCompleter {
    ///  LANGUAGE

    private final JavaPlugin plugin;
    public SimpleUtilsCommand(JavaPlugin plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender executor, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // CORE
        FileConfiguration config = plugin.getConfig();
        String noPermission = config.getString("no_permission", "You have no perms for this!");
        String mainArgument = args[0];
        if (mainArgument.isEmpty()) {
            executor.sendMessage("hi :D");
            return true;
        }
        if (mainArgument.equalsIgnoreCase("stats")) {
            if (!executor.hasPermission("serverstats_SimpleUtil")) {
                executor.sendMessage(noPermission);
                return true;
            }
            List<Long> serverStats = Statistics.GetMemory();
            Component discordMessage = Component.text(
                    "Want more stats here? Click me to ask!"
            ).clickEvent(ClickEvent.openUrl("https://discord.gg/tJJDqhg8hH"));

            executor.sendMessage("§a--- Server Stats ---");
            executor.sendMessage("§2• Memory");
            executor.sendMessage("  §2• Maximum: " + String.valueOf(serverStats.get(0)));
            executor.sendMessage("  §2• Free: " + String.valueOf(serverStats.get(1)));
            executor.sendMessage("  §2• In use: " + String.valueOf(serverStats.get(2)));
            executor.sendMessage("§2• Players");
            executor.sendMessage("  §2• Online: " + String.valueOf(Statistics.GetPlayers()));
            executor.sendMessage("§a|--------------------|");
            executor.sendMessage(discordMessage);
        } else if (mainArgument.equalsIgnoreCase("info")) {
            executor.sendMessage("Well, This is terrible, I didint code this yet.");
        }
        // CONSTANTS
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("stats");
            completions.add("info");
            completions.removeIf(s -> !s.toLowerCase().startsWith(args[0].toLowerCase()));
        }

        return completions;
    }
}

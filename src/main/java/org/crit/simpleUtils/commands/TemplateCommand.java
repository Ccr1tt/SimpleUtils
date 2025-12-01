package org.crit.simpleUtils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public class TemplateCommand implements CommandExecutor {
    ///  LANGUAGE

    private final JavaPlugin plugin;
    public TemplateCommand(JavaPlugin plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender executor, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // CORE
        FileConfiguration config = plugin.getConfig();

        // CONSTANTS
        return true;
    }
}

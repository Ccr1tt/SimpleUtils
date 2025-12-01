package org.crit.simpleUtils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    public FlyCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender executor, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        FileConfiguration config = plugin.getConfig();

        // Constants
        String NO_PERMISSION = config.getString("not_a_player");
        String FLIGHT_ON = config.getString("toggle_true_fly");
        String FLIGHT_OFF = config.getString("toggle_false_fly");
        String PREFIX = config.getString("prefix");

        if (!(executor instanceof Player)) {
            assert NO_PERMISSION != null;
            executor.sendMessage(NO_PERMISSION);
            return true;
        }

        Player player = (Player) executor;

        if (!player.getAllowFlight()) {
            player.setAllowFlight(true);
            player.setFlying(true);
            assert FLIGHT_ON != null;
            executor.sendMessage(PREFIX + FLIGHT_ON);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
            assert FLIGHT_OFF != null;
            executor.sendMessage(PREFIX + FLIGHT_OFF);
        }

        return true;
    }
}

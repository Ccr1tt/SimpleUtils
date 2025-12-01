package org.crit.simpleUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.crit.simpleUtils.commands.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import java.util.Objects;


public final class SimpleUtils extends JavaPlugin implements Listener {


    @Override
    public void onLoad() {
        System.out.println("Loaded config and enabling plugin!");
        saveDefaultConfig();
        getLogger().info("Loading Commands..");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        Bukkit.getPluginManager().registerEvents(this,this);
        registerCommands();
        getLogger().info("Registered commands!");
        System.out.println("Enabled!");
        getLogger().warning("""
                 $$$$$$\\  $$\\                         $$\\           $$\\   $$\\   $$\\     $$\\ $$\\\s
                $$  __$$\\ \\__|                        $$ |          $$ |  $$ |  $$ |    \\__|$$ |
                $$ /  \\__|$$\\ $$$$$$\\$$$$\\   $$$$$$\\  $$ | $$$$$$\\  $$ |  $$ |$$$$$$\\   $$\\ $$ |
                \\$$$$$$\\  $$ |$$  _$$  _$$\\ $$  __$$\\ $$ |$$  __$$\\ $$ |  $$ |\\_$$  _|  $$ |$$ |
                 \\____$$\\ $$ |$$ / $$ / $$ |$$ /  $$ |$$ |$$$$$$$$ |$$ |  $$ |  $$ |    $$ |$$ |
                $$\\   $$ |$$ |$$ | $$ | $$ |$$ |  $$ |$$ |$$   ____|$$ |  $$ |  $$ |$$\\ $$ |$$ |
                \\$$$$$$  |$$ |$$ | $$ | $$ |$$$$$$$  |$$ |\\$$$$$$$\\ \\$$$$$$  |  \\$$$$  |$$ |$$ |
                 \\______/ \\__|\\__| \\__| \\__|$$  ____/ \\__| \\_______| \\______/    \\____/ \\__|\\__|
                                            $$ |                                               \s
                                            $$ |                                               \s
                                            \\__|  \s
                               My first minecraft plugin twin, Don't expect much!
                """);
    }
    public void registerCommands() {

        getCommand("dupe").setExecutor(new DupeCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("dupeblacklist").setExecutor(new DupeBlacklist(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("enchant").setExecutor(new EnchantCommand(this));
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        Plugin essentials = getServer().getPluginManager().getPlugin("Essentials");

        if (essentials != null) {
            PluginCommand fly = getCommand("fly");

            if (fly != null) {
                getLogger().warning("""
                        EssentialsX Detected disabled the following commands: 
                            - /fly
                        """);
                fly.setExecutor((sender, cmd, label, args) -> {
                    if (args.length > 0) {
                        String otherPlayer = args[0];
                        Bukkit.dispatchCommand(sender, "essentials:fly " + otherPlayer);
                    } else {
                        Bukkit.dispatchCommand(sender, "essentials:fly");
                    }
                    return true;
                });
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down!");
    }

    @EventHandler
    public void PlayerAdded(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String joinMessage = getConfig().getString("join_message", "Welcome to the server {PLAYER}!");
        joinMessage = joinMessage.replace("{PLAYER}", player.getName());
        assert joinMessage != null;
        player.sendMessage(joinMessage);
        event.setJoinMessage(
                getConfig().getString(
                        "public_join_message",
                        "[+] {PLAYER").replace("{PLAYER}", player.getName())
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        String leaveMessage = getConfig().getString("leave_message", "{PLAYER} has left the game.");
        leaveMessage = leaveMessage.replace("{PLAYER}", playerName);
        event.setQuitMessage(null);
        Bukkit.broadcastMessage(leaveMessage);
    }


}

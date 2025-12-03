package org.crit.simpleUtils.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DupeBlacklist implements CommandExecutor, TabCompleter {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DupeBlacklist.class);
    private final JavaPlugin plugin;

    public DupeBlacklist(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender executor,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String[] args) {

        FileConfiguration config = plugin.getConfig();
        List<String> currentList = config.getStringList("Blacklisted_Players_dupe");
        String prefix = Objects.requireNonNullElse(config.getString("prefix"), "");


        if (args.length == 0) {
            executor.sendMessage(prefix + " Usage: /dupeblacklist <player|hand> [playerName]");
            return true;
        }

        String target = args[0];


        if (target.equalsIgnoreCase("hand")) {

            if (!(executor instanceof Player player)) {
                executor.sendMessage(prefix + " Only players can perform this command.");
                return true;
            }

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand == null || mainHand.getType().isAir()) {
                player.sendMessage(prefix + " You must hold an item!");
                return true;
            }

            var meta = mainHand.getItemMeta();
            if (meta == null) {
                player.sendMessage(prefix + " This item cannot have meta!");
                return true;
            }

            List<Component> lore = new ArrayList<>();
            String loreString = Objects.requireNonNullElse(config.getString("dupebanned_lore"), "DUPEBANNED");
            lore.add(Component.text(loreString));

            meta.lore(lore);
            mainHand.setItemMeta(meta);

            player.sendMessage(prefix + " Your held item is now dupebanned.");
            return true;
        }

        if (target.equalsIgnoreCase("player")) {

            if (args.length < 2) {
                executor.sendMessage(prefix + " You must specify a player!");
                return true;
            }

            String playerName = args[1];
            Player targetPlayer = Bukkit.getPlayer(playerName);

            if (targetPlayer == null) {
                executor.sendMessage(prefix + " That player is not online!");
                return true;
            }

            UUID uuid = targetPlayer.getUniqueId();
            String message = Objects.requireNonNullElse(config.getString("RUN_AGAIN_DUPE"), "You have been dupe-blacklisted!");

            if (!currentList.contains(uuid.toString())) {
                currentList.add(uuid.toString());
                executor.sendMessage(prefix + " Added " + playerName + " to the dupe blacklist.");
                targetPlayer.sendActionBar(Component.text(message));
            } else {
                currentList.remove(uuid.toString());
                executor.sendMessage(prefix + " Removed " + playerName + " from the dupe blacklist.");
            }

            config.set("Blacklisted_Players_dupe", currentList);
            plugin.saveConfig();

            log.warn("Updated dupe blacklist: " + currentList);
            return true;
        }

        executor.sendMessage(prefix + " Unknown subcommand.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("player");
            completions.add("hand");

            completions.removeIf(c -> !c.toLowerCase().startsWith(args[0].toLowerCase()));
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
            completions.removeIf(c -> !c.toLowerCase().startsWith(args[1].toLowerCase()));
        }

        return completions;
    }
}

package org.crit.simpleUtils.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;

    public EnchantCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // CORE
        FileConfiguration config = plugin.getConfig();
        String Prefix = config.getString("prefix", "");
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only a player can run this!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("Usage: /enchant <enchantment> <level>");
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = inventory.getItemInMainHand();
        ItemMeta meta = mainHand.getItemMeta();

        if (meta == null) {
            player.sendMessage("You must be holding an item to enchant it!");
            return true;
        }

        String enchantName = args[0].toLowerCase();
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantName));

        if (enchantment == null) {
            player.sendMessage(Prefix + "Invalid enchantment: " + enchantName);
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Prefix + "Invalid level: " + args[1]);
            return true;
        }

        meta.addEnchant(enchantment, level, true);
        mainHand.setItemMeta(meta);
        String Enchantmessage = config.getString("enchanted_item", "Added {ENCHANT} to {ITEM_NAME}!");
        Enchantmessage = Enchantmessage.replace("{ENCHANT}", args[0] + "|" + args[1]);
        Enchantmessage = Enchantmessage.replace("{ITEM_NAME}", mainHand.getType().name());
        player.sendMessage(Prefix + Enchantmessage);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.getKey().getKey().startsWith(input)) {
                    completions.add(enchantment.getKey().getKey());
                }
            }
        }
        else if (args.length == 2) {
            for (int i = 1; i <= 5; i++) {
                completions.add(String.valueOf(i));
            }
        }

        return completions;
    }
}

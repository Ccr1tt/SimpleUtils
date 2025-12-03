package org.crit.simpleUtils.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class DupeCommand implements CommandExecutor {
    private static final String INVALID_ARGUMENTS_MESSAGE = "§c§lPlease enter a valid number!";
    private final JavaPlugin plugin;

    public DupeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only a player can run this!");
            return true;
        }
        final Player player = (Player) commandSender;

        FileConfiguration config = plugin.getConfig();
        PlayerInventory inventory = player.getInventory();
        String Prefix = config.getString("prefix");
        ItemStack mainHand = inventory.getItemInMainHand();
        List<String> lore = mainHand.getLore();




        List<String> blacklist = config.getStringList("BLACKLIST");
        String dupeMessage = config.getString("DUPED_ITEM");
        assert dupeMessage != null;
        List<String> dupe_blacklist = config.getStringList("Blacklisted_Players_dupe");
        UUID id = ((Player) commandSender).getUniqueId();
        if (dupe_blacklist.contains(String.valueOf(id))) {
            commandSender.sendMessage(Prefix + config.getString("BLACKLISTED_FROM_DUPING"));
            return true;
        }

        if (mainHand.getAmount() == 0) {
            player.sendMessage(Prefix + "§c§lYou must be holding something!");
            return true;
        }
        for (String value : lore) {
            if (value == config.getString("dupebanned_lore", "DUPEBANNED")) {
                player.sendMessage(Prefix + "This item is not dupeable!");
                return true;
            }
        }
        String itemName = mainHand.getType().name();
        if (blacklist.contains(itemName)) {
            player.sendMessage(Prefix + "§c§lYou cannot dupe " + itemName + "!");
            return true;
        }
        String amount = "1";
        if (args.length > 0) {
            amount = args[0];
        }
        try {
            if (Integer.parseInt(amount) == 0) {
                amount = "1";
            }
        } catch (Exception e) {
            amount = "1";
        }

        if (!StringUtils.isNumeric(amount)) {
            commandSender.sendMessage(INVALID_ARGUMENTS_MESSAGE);
            return true;
        }

        int loop = Integer.parseInt(amount);

        if (loop > Integer.parseInt(config.getString("max_dupe"))) {
            player.sendMessage(Prefix + "§c§lYou can only duplicate " + Integer.parseInt(Objects.requireNonNull(config.getString("max_dupe"))) + " items!");
            return true;
        }
        for (int i = 0; i < loop; i++) {
            inventory.addItem(mainHand.clone());
            if (config.getBoolean("dupe_sound_enabled")) {
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1.0f, 1.0f);
            }
        }
        dupeMessage = dupeMessage.replace("{ITEM_NAME}", itemName);
        dupeMessage = dupeMessage.replace("{AMOUNT}", amount);
        player.sendMessage(Prefix + dupeMessage);
        return true;
    };

}

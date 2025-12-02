package com.gtmc.seasonalbundle.commands;

import com.gtmc.seasonalbundle.SeasonalBundlePlugin;
import com.gtmc.seasonalbundle.data.SeasonDataManager;
import com.gtmc.seasonalbundle.util.BundleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SeasonCommand implements CommandExecutor, TabCompleter {

    private final SeasonalBundlePlugin plugin;
    private final SeasonDataManager dataManager;

    public SeasonCommand(SeasonalBundlePlugin plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getSeasonDataManager();

        // Register commands
        plugin.getCommand("seasonend").setExecutor(this);
        plugin.getCommand("seasonstart").setExecutor(this);
        plugin.getCommand("bundlereload").setExecutor(this);

        plugin.getCommand("seasonend").setTabCompleter(this);
        plugin.getCommand("seasonstart").setTabCompleter(this);
        plugin.getCommand("bundlereload").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("seasonalbundle.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "seasonend":
                return handleSeasonEnd(sender);
            case "seasonstart":
                return handleSeasonStart(sender);
            case "bundlereload":
                return handleBundleReload(sender);
            default:
                return false;
        }
    }

    private boolean handleSeasonEnd(org.bukkit.command.CommandSender sender) {
        sender.sendMessage("§6Ending season " + dataManager.getCurrentSeason() + "...");

        // Collect items from all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack bundle = findBundleInInventory(player);
            if (bundle != null) {
                ItemStack bundleItem = extractItemFromBundle(bundle);
                if (bundleItem != null) {
                    dataManager.savePlayerItem(player.getUniqueId(), bundleItem);
                    player.sendMessage("§6Your bundle item has been saved for Season " + (dataManager.getCurrentSeason() + 1) + "!");
                } else {
                    player.sendMessage("§cYour bundle was empty. No item saved.");
                }
            } else {
                player.sendMessage("§cNo seasonal bundle found in your inventory.");
            }
        }

        dataManager.endSeason();
        sender.sendMessage("§aSeason " + (dataManager.getCurrentSeason() - 1) + " ended successfully!");
        Bukkit.broadcastMessage("§6§lSeason " + (dataManager.getCurrentSeason() - 1) + " has ended!");

        return true;
    }

    private boolean handleSeasonStart(org.bukkit.command.CommandSender sender) {
        int oldSeason = dataManager.getCurrentSeason();
        dataManager.startNewSeason();
        sender.sendMessage("§aNew season started! (Season " + dataManager.getCurrentSeason() + ")");
        Bukkit.broadcastMessage("§6§lSeason " + dataManager.getCurrentSeason() + " has begun!");

        return true;
    }

    private boolean handleBundleReload(org.bukkit.command.CommandSender sender) {
        plugin.reloadConfig();
        dataManager.load();
        sender.sendMessage("§aConfiguration reloaded!");
        return true;
    }

    /**
     * Finds a seasonal bundle in player's inventory
     */
    private ItemStack findBundleInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType().name().equals("BUNDLE")) {
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : "";
                if (displayName.contains("Seasonal Bundle") || displayName.contains("Bundle")) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Extracts the item from a bundle
     */
    private ItemStack extractItemFromBundle(ItemStack bundle) {
        if (bundle == null) {
            return null;
        }

        // Get items from bundle meta
        if (bundle.hasItemMeta() && bundle.getItemMeta() instanceof org.bukkit.inventory.meta.BundleMeta) {
            org.bukkit.inventory.meta.BundleMeta bundleMeta = (org.bukkit.inventory.meta.BundleMeta) bundle.getItemMeta();
            if (!bundleMeta.getItems().isEmpty()) {
                // Return first (and should be only) item in bundle
                ItemStack item = bundleMeta.getItems().get(0).clone();
                item.setAmount(1); // Ensure it's just 1 item
                return item;
            }
        }

        return null;
    }

    @Override
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender, Command cmd, String alias, String[] args) {
        return new ArrayList<>();
    }
}

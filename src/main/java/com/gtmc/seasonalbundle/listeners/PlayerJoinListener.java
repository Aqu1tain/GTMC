package com.gtmc.seasonalbundle.listeners;

import com.gtmc.seasonalbundle.SeasonalBundlePlugin;
import com.gtmc.seasonalbundle.data.SeasonDataManager;
import com.gtmc.seasonalbundle.util.BundleUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {

    private final SeasonalBundlePlugin plugin;
    private final SeasonDataManager dataManager;

    public PlayerJoinListener(SeasonalBundlePlugin plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getSeasonDataManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Check if player has a seasonal item from previous season
        ItemStack seasonalItem = dataManager.getPlayerItem(player.getUniqueId());

        if (seasonalItem != null) {
            // Give rare bundle with the previous season's item
            ItemStack rareBundle = BundleUtil.createRareBundle(seasonalItem, dataManager.getCurrentSeason() - 1);

            // Try to add to inventory, drop if full
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(rareBundle);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), rareBundle);
            }

            player.sendMessage("§6§lWelcome to Season " + dataManager.getCurrentSeason() + "!");
            player.sendMessage("§7You have received your §c§lSEASON " + (dataManager.getCurrentSeason() - 1) + " RELIC§7!");
        } else {
            // New player or first join of season - give regular bundle
            ItemStack bundle = BundleUtil.createBundle(dataManager.getCurrentSeason());

            // Try to add to inventory, drop if full
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(bundle);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), bundle);
            }

            player.sendMessage("§6§lWelcome to Season " + dataManager.getCurrentSeason() + "!");
            player.sendMessage("§7You have received a §6Seasonal Bundle§7!");
            player.sendMessage("§8Store one item in it for the next season!");
        }
    }
}

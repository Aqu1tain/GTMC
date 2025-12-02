package com.gtmc.seasonalbundle.listeners;

import com.gtmc.seasonalbundle.util.BundleUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

public class ItemValidationListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if it's a bundle inventory
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        // Check if clicking in bundle view
        ItemStack cursor = event.getCursor();
        ItemStack clicked = event.getCurrentItem();

        // If player is dragging an item to put in bundle
        if (event.getView().getTitle().contains("Bundle") && cursor != null && cursor.getType().name().equals("BUNDLE")) {
            // Check if trying to put invalid item in bundle
            if (cursor.getAmount() > 1) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage("§cBundles can only hold 1 item!");
                return;
            }

            if (!BundleUtil.isValidBundleItem(cursor)) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage("§cYou cannot put this item in a bundle!");
                return;
            }

            if (BundleUtil.isBundleAtCapacity(clicked)) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage("§cThis bundle is at maximum capacity!");
            }
        }
    }
}

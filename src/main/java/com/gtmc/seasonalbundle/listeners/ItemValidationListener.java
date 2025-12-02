package com.gtmc.seasonalbundle.listeners;

import com.gtmc.seasonalbundle.util.BundleUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

public class ItemValidationListener implements Listener {

    /**
     * Prevent invalid items from being placed in bundles via click
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack cursor = event.getCursor();
        ItemStack clicked = event.getCurrentItem();

        // Check if player is trying to put something in a bundle
        if (clicked != null && isBundleItem(clicked) && cursor != null && !cursor.getType().name().equals("AIR")) {
            // Prevent placing items with stack amount > 1
            if (cursor.getAmount() > 1) {
                event.setCancelled(true);
                player.sendMessage("§cBundles can only hold 1 item, not stacks!");
                return;
            }

            // Prevent invalid items
            if (!BundleUtil.isValidBundleItem(cursor.getType())) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot put this item in a bundle!");
                return;
            }

            // Prevent putting items in already full bundles
            if (BundleUtil.isBundleAtCapacity(clicked)) {
                event.setCancelled(true);
                player.sendMessage("§cThis bundle is already full (1 item max)!");
                return;
            }
        }

        // Prevent moving a stack into a bundle via shift+click
        if (event.getView().getTitle().contains("Bundle")) {
            ItemStack current = event.getCurrentItem();
            if (current != null && current.getAmount() > 1 && !current.getType().name().equals("BUNDLE")) {
                event.setCancelled(true);
                player.sendMessage("§cBundles can only hold 1 item!");
            }
        }
    }

    /**
     * Prevent invalid items from being dragged into bundles
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack cursor = event.getCursor();

        // Check if dragging into bundle inventory
        if (event.getView().getTitle().contains("Bundle") && cursor != null) {
            if (cursor.getAmount() > 1) {
                event.setCancelled(true);
                player.sendMessage("§cBundles can only hold 1 item!");
                return;
            }

            if (!BundleUtil.isValidBundleItem(cursor.getType())) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot put this item in a bundle!");
            }
        }
    }

    /**
     * Check if an ItemStack is a seasonal bundle
     */
    private boolean isBundleItem(ItemStack item) {
        if (item == null || !item.getType().name().equals("BUNDLE")) {
            return false;
        }

        if (!item.hasItemMeta()) {
            return false;
        }

        String displayName = item.getItemMeta().getDisplayName();
        return displayName != null && (displayName.contains("Seasonal Bundle") || displayName.contains("RELIC"));
    }
}

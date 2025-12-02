package com.gtmc.seasonalbundle.listeners;

import com.gtmc.seasonalbundle.util.BundleUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

import java.util.List;

public class ItemValidationListener implements Listener {

    /**
     * Validate bundle contents when player closes the bundle inventory
     * Remove invalid items and enforcing 1-item limit
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();

        // Check if any seasonal bundle is in their inventory and validate it
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isBundleItem(item)) {
                validateAndCleanBundle(player, item);
            }
        }
    }

    /**
     * Validate bundle contents and remove invalid items or excess items
     */
    private void validateAndCleanBundle(Player player, ItemStack bundle) {
        if (bundle.getItemMeta() == null || !(bundle.getItemMeta() instanceof BundleMeta)) {
            return;
        }

        BundleMeta bundleMeta = (BundleMeta) bundle.getItemMeta();
        List<ItemStack> items = bundleMeta.getItems();

        boolean changed = false;

        // If more than 1 item, remove extras and give back to player
        if (items.size() > 1) {
            ItemStack firstItem = items.get(0);
            bundleMeta.setItems(java.util.Collections.singletonList(firstItem));
            changed = true;

            // Give back the extra items
            for (int i = 1; i < items.size(); i++) {
                ItemStack extra = items.get(i);
                player.getWorld().dropItemNaturally(player.getLocation(), extra);
            }

            player.sendMessage("§cBundle reduced to 1 item max. Extra items dropped!");
        }

        // Check if any items are invalid and remove them
        for (ItemStack item : items) {
            if (!BundleUtil.isValidBundleItem(item.getType())) {
                bundleMeta.setItems(java.util.Collections.emptyList());
                changed = true;

                player.sendMessage("§cInvalid item removed from bundle: §7" + item.getType().name());
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                break; // Only one item per bundle, so stop here
            }
        }

        if (changed) {
            bundle.setItemMeta(bundleMeta);
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

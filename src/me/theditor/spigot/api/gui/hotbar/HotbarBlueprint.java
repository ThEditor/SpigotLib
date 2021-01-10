package me.theditor.spigot.api.gui.hotbar;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public class HotbarBlueprint {

    private HashMap<Integer, HotbarItem> items;
    private ItemStack filler;

    public HotbarBlueprint() {
        items = new HashMap<>();
        filler = null;
    }

    public HotbarItem item(int slot, ItemStack item) {
        HotbarItem i = new HotbarItem(slot, item);
        items.put(slot, i);
        return i;
    }

    public HotbarItem item(int slot, ItemStack item, Consumer<PlayerInteractEvent> handler) {
        HotbarItem i = new HotbarItem(slot, item, handler);
        items.put(slot, i);
        return i;
    }

    public HotbarBlueprint fill(ItemStack filler) {
        this.filler = filler;
        return this;
    }

    public HotbarBlueprint click(PlayerInteractEvent e, int slot) {
        HotbarItem item = items.get(slot);
        if(item != null) {
            if(item.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(e.getItem().getItemMeta().getDisplayName()) &&
                    item.getHandler() != null) item.getHandler().accept(e);
        }
        return this;
    }

    public ItemStack[] buildHotbar() {
        ItemStack[] inv = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            HotbarItem invItem = items.get(i);
            if(invItem != null) inv[i] = invItem.getItem();
            else if(filler != null) inv[i] = filler;
        }
        return inv;
    }
}

package me.theditor.spigot.api.gui.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public class InventoryBlueprint {

    private HashMap<Integer, InventoryItem> items;
    private ItemStack filler;

    public InventoryBlueprint() {
        items = new HashMap<>();
        filler = null;
    }

    public InventoryItem item(int slot, ItemStack item) {
        InventoryItem i = new InventoryItem(slot, item);
        items.put(slot, i);
        return i;
    }

    public InventoryItem item(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        InventoryItem i = new InventoryItem(slot, item, handler);
        items.put(slot, i);
        return i;
    }

    public InventoryBlueprint fill(ItemStack filler) {
        this.filler = filler;
        return this;
    }

    public InventoryBlueprint click(InventoryClickEvent e, int slot) {
        InventoryItem item = items.get(slot);
        if(item != null) {
            if(item.getHandler() != null) item.getHandler().accept(e);
        }
        return this;
    }

    public ItemStack[] buildInventory(int size) {
        ItemStack[] inv = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            InventoryItem invItem = items.get(i);
            if(invItem != null) inv[i] = invItem.getItem();
            else if(filler != null) inv[i] = filler;
        }
        return inv;
    }
}

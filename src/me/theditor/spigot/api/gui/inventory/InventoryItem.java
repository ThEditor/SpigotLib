package me.theditor.spigot.api.gui.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class InventoryItem {

    private int slot;
    private ItemStack item;
    private Consumer<InventoryClickEvent> handler;

    public InventoryItem(int slot, ItemStack item) {
        this(slot, item, null);
    }

    public InventoryItem(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        this.slot = slot;
        this.item = item;
        this.handler = handler;
    }

    public void handler(Consumer<InventoryClickEvent> handler) {
        this.handler = handler;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public Consumer<InventoryClickEvent> getHandler() {
        return handler;
    }
}

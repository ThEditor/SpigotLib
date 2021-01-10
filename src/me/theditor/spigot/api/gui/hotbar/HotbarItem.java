package me.theditor.spigot.api.gui.hotbar;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class HotbarItem {

    private int slot;
    private ItemStack item;
    private Consumer<PlayerInteractEvent> handler;

    public HotbarItem(int slot, ItemStack item) {
        this(slot, item, null);
    }

    public HotbarItem(int slot, ItemStack item, Consumer<PlayerInteractEvent> handler) {
        this.slot = slot;
        this.item = item;
        this.handler = handler;
    }

    public void handler(Consumer<PlayerInteractEvent> handler) {
        this.handler = handler;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public Consumer<PlayerInteractEvent> getHandler() {
        return handler;
    }
}

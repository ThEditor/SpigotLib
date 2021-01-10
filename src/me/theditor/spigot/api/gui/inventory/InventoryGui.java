package me.theditor.spigot.api.gui.inventory;

import me.theditor.spigot.api.listeners.CoreListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryGui {

    private JavaPlugin plugin;
    private Inventory gui;
    private int rows;
    private InventoryBlueprint blueprint;

    public InventoryGui(JavaPlugin plugin, String title, InventoryBlueprint blueprint) {
        this(plugin, title, null, 1, blueprint);
    }

    public InventoryGui(JavaPlugin plugin, String title, int rows, InventoryBlueprint blueprint) {
        this(plugin, title, null, rows, blueprint);
    }

    public InventoryGui(JavaPlugin plugin, String title, InventoryHolder holder, int rows, InventoryBlueprint blueprint) {
        if(rows < 1) rows = 1;
        this.plugin = plugin;
        this.rows = rows;
        this.gui = plugin.getServer().createInventory(holder, rows*9, ChatColor.translateAlternateColorCodes('&', title));
        this.blueprint = blueprint;
        new CoreListener<>(InventoryClickEvent.class, plugin, this::click);
    }

    private void click(InventoryClickEvent e) {
        if(e.getInventory() == null) return;
        if(!e.getInventory().getTitle().equalsIgnoreCase(gui.getTitle())) return;
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        this.blueprint.click(e, e.getSlot());
    }

    public InventoryGui open(Player player) {
        if(blueprint == null) throw new NullPointerException("Blueprint is null, assign a blueprint before calling this function");
        gui.setContents(blueprint.buildInventory(this.rows*9));
        player.openInventory(gui);
        return this;
    }

    public void setBlueprint(InventoryBlueprint blueprint) {
        this.blueprint = blueprint;
    }
}

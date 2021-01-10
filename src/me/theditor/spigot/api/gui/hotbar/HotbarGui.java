package me.theditor.spigot.api.gui.hotbar;

import me.theditor.spigot.api.listeners.CoreListener;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HotbarGui {

    private JavaPlugin plugin;
    private String bypassPerm;
    private HotbarBlueprint blueprint;

    public HotbarGui(JavaPlugin plugin) {
        this(plugin,"");
    }

    public HotbarGui(JavaPlugin plugin, String bypassPerm) {
        this(plugin, bypassPerm, null);
    }

    public HotbarGui(JavaPlugin plugin, HotbarBlueprint blueprint) {
        this(plugin, "", blueprint);
    }

    public HotbarGui(JavaPlugin plugin, String bypassPerm, HotbarBlueprint blueprint) {
        this.plugin = plugin;
        this.bypassPerm = bypassPerm;
        this.blueprint = blueprint;
        new CoreListener<>(PlayerDropItemEvent.class, plugin, (e) -> {
            Player player = e.getPlayer();
            if(bypassPerm.isEmpty())
                e.setCancelled(true);
            else if(!player.hasPermission(bypassPerm))
                e.setCancelled(true);
        });
        new CoreListener<>(InventoryClickEvent.class, plugin, (e) -> {
            Player player = (Player) e.getWhoClicked();
            if(e.getInventory().equals(player.getInventory())) {
                if(bypassPerm.isEmpty())
                    e.setCancelled(true);
                else if(!player.hasPermission(bypassPerm))
                    e.setCancelled(true);
            }
        });
        new CoreListener<>(PlayerInteractEvent.class , plugin, this::click);
    }

    private void click(PlayerInteractEvent e) {
        if((e.getAction() != Action.RIGHT_CLICK_AIR) && (e.getAction() != Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getItem() == null) return;
        if(bypassPerm.isEmpty())
            e.setCancelled(true);
        else if(!e.getPlayer().hasPermission(bypassPerm))
            e.setCancelled(true);
        this.blueprint.click(e, e.getPlayer().getInventory().getHeldItemSlot());
    }

    public HotbarGui giveClearInventory(Player player) {
        if(blueprint == null) throw new NullPointerException("Blueprint is null, assign a blueprint before calling this function");
        player.getInventory().setContents(blueprint.buildHotbar());
        return this;
    }

    public HotbarGui give(Player player) {
        if(blueprint == null) throw new NullPointerException("Blueprint is null, assign a blueprint before calling this function");
        ItemStack[] hotbar = blueprint.buildHotbar();
        for (int i = 0; i < 9; i++) {
            if(hotbar[i] != null)
                player.getInventory().setItem(i, hotbar[i]);
        }
        return this;
    }

    public void setBlueprint(HotbarBlueprint blueprint) {
        this.blueprint = blueprint;
    }
}

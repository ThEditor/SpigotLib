package me.tigerhix.lib.scoreboard;

import me.tigerhix.lib.scoreboard.type.Sidebar;
import me.tigerhix.lib.scoreboard.type.SimpleSidebar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SidebarLib extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getPluginInstance() {
        return instance;
    }

    public static void setPluginInstance(Plugin instance) {
        if (SidebarLib.instance != null) return;
        SidebarLib.instance = instance;
    }

    public static Sidebar createScoreboard(Player holder) {
        return new SimpleSidebar(holder);
    }

    @Override
    public void onEnable() {
        setPluginInstance(this);
    }

}
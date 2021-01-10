package me.theditor.spigot.api.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {
    private static JavaPlugin plugin;
    private FileConfiguration config;
    private File file;

    public Config(JavaPlugin plugin, String name) {
        this(plugin, name, true);
    }

    public Config(JavaPlugin plugin, String name, boolean copyDefaults){
        Config.plugin = plugin;
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        this.file = new File(plugin.getDataFolder(),name);

        if(!this.file.exists()){
            try {
                if(copyDefaults){
                    plugin.saveResource(name, false);
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                plugin.getLogger().severe(String.format("[%s] - Failed to create config file!", plugin.getDescription().getName()));
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe(String.format("[%s] - Failed to save config file!", plugin.getDescription().getName()));
        }
    }

    public void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }
}

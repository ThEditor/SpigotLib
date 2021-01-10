package me.theditor.spigot.api.builders.item;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoreBuilder {

    private List<String> lore;

    public LoreBuilder(String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
    }

    public LoreBuilder line(String line) {
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        return this;
    }

    public LoreBuilder blank() {
        return this.line("");
    }

    public List<String> build() {
        return lore;
    }

}

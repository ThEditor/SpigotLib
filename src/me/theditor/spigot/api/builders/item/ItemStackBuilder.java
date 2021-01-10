package me.theditor.spigot.api.builders.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemStackBuilder {

    private Material material;
    private int amount;
    private short durability;

    private String name;
    private LoreBuilder loreBuilder;

    private boolean unbreakable;

    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;

    public ItemStackBuilder() {
        this(Material.AIR);
    }

    public ItemStackBuilder(Material material) {
        this.material = material;
        this.amount = 1;
        this.durability = 0;
        this.unbreakable = false;
    }

    public ItemStackBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder data(short data) {
        this.durability = data;
        return this;
    }

    public ItemStackBuilder data(int data) {
        return data((short) data);
    }

    public ItemStackBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemStackBuilder lore(LoreBuilder loreBuilder) {
        this.loreBuilder = loreBuilder;
        return this;
    }

    public ItemStackBuilder unbreakable() {
        this.unbreakable = true;
        return this;
    }

    public ItemStackBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemStackBuilder enchantment(Enchantment enchantment, int level) {
        if (enchantments == null) {
            this.enchantments = new HashMap<>();
        }

        enchantments.put(enchantment, level);
        return this;
    }

    public ItemStackBuilder itemFlags(List<ItemFlag> flags) {
        this.itemFlags = new ArrayList<>(flags);
        return this;
    }

    public ItemStackBuilder itemFlags(ItemFlag... flags) {
        return itemFlags(Arrays.asList(flags));
    }

    public SkullBuilder skull() {
        return new SkullBuilder(this);
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);

        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(ItemStackBuilder.parseColor(name));
        }

        if(loreBuilder != null) {
            itemMeta.setLore(loreBuilder.build());
        }

        if (enchantments != null && !enchantments.isEmpty()) {
            enchantments.forEach((ench, lvl) -> itemMeta.addEnchant(ench, lvl, true));
        }

        if (itemFlags != null && !itemFlags.isEmpty()) {
            itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[itemFlags.size()]));
        }

        if (unbreakable) itemMeta.spigot().setUnbreakable(true);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static String parseColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public class SkullBuilder {

        private ItemStackBuilder stackBuilder;
        private String owner;

        private SkullBuilder(ItemStackBuilder stackBuilder) {
            this.stackBuilder = stackBuilder;
        }

        public SkullBuilder owner(String ownerName) {
            this.owner = ownerName;
            return this;
        }

        public ItemStack build() {
            ItemStack skull = stackBuilder
                    .material(Material.SKULL_ITEM)
                    .data(3)
                    .build();

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(owner);
            skull.setItemMeta(meta);

            return skull;
        }
    }
}
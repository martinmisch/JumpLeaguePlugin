package de.martin.jumpleaguegym.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class CreateItem {
    private ItemStack is;
    private ItemMeta im;
    private SkullMeta sm;

    public CreateItem(String displayname, Material material, int amount) {
        this.is = new ItemStack(material, amount);
        this.im = this.is.getItemMeta();
        this.im.setDisplayName(displayname);
    }

    public CreateItem(String displayname, Material material, int amount, Enchantment enchantment) {
        this.is = new ItemStack(material, amount);
        this.im = this.is.getItemMeta();
        this.im.setDisplayName(displayname);
        this.im.addEnchant(enchantment, 10, true);
        this.is.setItemMeta(this.im);
    }

    public CreateItem(String displayname, String skullOwner) {
        this.is = new ItemStack(Material.valueOf("PLAYER_HEAD"), 1, (short) 3);
        this.sm = (SkullMeta) this.is.getItemMeta();
        this.sm.setDisplayName(displayname);
        this.sm.setOwner(skullOwner);
    }

    public ItemStack getItemStack() {
        this.is.setItemMeta(this.im);
        return this.is;
    }

    public ItemStack getSkullItemStack() {
        this.is.setItemMeta(this.sm);
        return this.is;
    }
}

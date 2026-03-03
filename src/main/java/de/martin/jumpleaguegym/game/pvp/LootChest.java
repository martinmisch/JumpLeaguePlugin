package de.martin.jumpleaguegym.game.pvp;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class LootChest {
    private Location location;
    private ItemStack[] items;
    private int taken;

    public LootChest(Location l, ItemStack[] items) {
        this.location = l;
        this.items = items;
        this.taken = 0;
    }

    public int getTaken() {
        return this.taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public Location getLocation() {
        return this.location;
    }

    public ItemStack[] getItems() {
        return this.items;
    }
}

package de.martin.jumpleaguegym.game.create;

import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChestItems implements Listener {
    private Inventory inv;

    public ChestItems() {
    }

    public void openInv(Player p, ModulSchwierigkeit ms) {
        this.inv = Bukkit.createInventory((InventoryHolder) null, 54, ms.name());
        this.inv.setContents(Main.getPlugin().getChM().getItemList(ms));
        p.openInventory(this.inv);
    }

    public ItemStack[] getRandomItems(ModulSchwierigkeit ms, int anzahl) {
        Random r = new Random();
        ItemStack[] temp = new ItemStack[27];

        int anzahlInChest = anzahl - r.nextInt(2);
        // Create list 0–26 and 0-53
        List<Integer> numbers27 = new ArrayList<>();
        List<Integer> numbers54 = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            numbers27.add(i);
        }
        for (int i = 0; i < 54; i++) {
            numbers54.add(i);
        }

        // Shuffle
        Collections.shuffle(numbers27, r);
        Collections.shuffle(numbers54, r);

        // Take first anzahlInChest
        int[] chestPositions = new int[anzahlInChest];
        int[] savedPositions = new int[anzahlInChest];
        for (int i = 0; i < anzahlInChest; i++) {
            chestPositions[i] = numbers27.get(i);
            savedPositions[i] = numbers54.get(i);
        }

        ItemStack[] items = Main.getPlugin().getChM().getItemList(ms);

        for (int i = 0; i < anzahlInChest; i++) {
            temp[chestPositions[i]] = items[savedPositions[i]];
        }

        return temp;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(ModulSchwierigkeit.LEICHT.name())) {
            Main.getPlugin().getChM().saveItems(e.getInventory().getContents(), ModulSchwierigkeit.LEICHT);
        } else if (e.getView().getTitle().equals(ModulSchwierigkeit.MITTEL.name())) {
            Main.getPlugin().getChM().saveItems(e.getInventory().getContents(), ModulSchwierigkeit.MITTEL);
        } else if (e.getView().getTitle().equals(ModulSchwierigkeit.SCHWER.name())) {
            Main.getPlugin().getChM().saveItems(e.getInventory().getContents(), ModulSchwierigkeit.SCHWER);
        }

    }
}

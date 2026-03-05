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

    public void openInv(Player p, ModulSchwierigkeit ms, int nummer) {
        this.inv = Bukkit.createInventory((InventoryHolder) null, 54, ms.name() + " " + nummer);
        ItemStack[] stack = Main.getPlugin().getChM().getItemList(ms, nummer);
        for (int i = 0; i < 10; i++) {
            System.out.println("i: " + stack[i].getType().toString());
        }
        this.inv.setContents(Main.getPlugin().getChM().getItemList(ms, nummer));
        p.openInventory(this.inv);
    }

    public ItemStack[] getRandomItems(ModulSchwierigkeit ms, int anzahl) {
        Random r = new Random();
        ItemStack[] temp = new ItemStack[27];

        int anzahlInChest = anzahl - r.nextInt(2);
        // Create list 0–26 and 0-108
        List<Integer> numbers27 = new ArrayList<>();
        List<Integer> numbers108 = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            numbers27.add(i);
        }
        for (int i = 0; i < 108; i++) {
            numbers108.add(i);
        }

        // Shuffle
        Collections.shuffle(numbers27, r);
        Collections.shuffle(numbers108, r);

        // Take first anzahlInChest
        int[] chestPositions = new int[anzahlInChest];
        int[] savedPositions = new int[anzahlInChest];
        for (int i = 0; i < anzahlInChest; i++) {
            chestPositions[i] = numbers27.get(i);
            savedPositions[i] = numbers108.get(i);
        }

        ItemStack[] items = Main.getPlugin().getChM().getItemList(ms);

        for (int i = 0; i < anzahlInChest; i++) {
            temp[chestPositions[i]] = items[savedPositions[i]];
        }

        return temp;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().contains(ModulSchwierigkeit.LEICHT.name())) {
            Main.getPlugin().getChM().saveItems(e.getInventory().getContents(), ModulSchwierigkeit.LEICHT, Integer.parseInt("" + e.getView().getTitle().charAt(e.getView().getTitle().length() - 1)));
        } else if (e.getView().getTitle().contains(ModulSchwierigkeit.MITTEL.name())) {
            Main.getPlugin().getChM().saveItems(e.getInventory().getContents(), ModulSchwierigkeit.MITTEL, Integer.parseInt("" + e.getView().getTitle().charAt(e.getView().getTitle().length() - 1)));
        } else if (e.getView().getTitle().contains(ModulSchwierigkeit.SCHWER.name())) {
            Main.getPlugin().getChM().saveItems(e.getInventory().getContents(), ModulSchwierigkeit.SCHWER, Integer.parseInt("" + e.getView().getTitle().charAt(e.getView().getTitle().length() - 1)));
        }

    }
}

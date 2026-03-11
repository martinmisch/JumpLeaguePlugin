package de.martin.jumpleaguegym.game.create;

import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestItems implements Listener {
    private Inventory inv;

    public void openInv(Player p, ModulSchwierigkeit ms, int nummer) {
        this.inv = Bukkit.createInventory(null, 54, ms.name() + " " + nummer);
        ItemStack[] stack = Main.getPlugin().getChM().getItemList(ms, nummer);
        this.inv.setContents(Main.getPlugin().getChM().getItemList(ms, nummer));
        p.openInventory(this.inv);
    }

    public ItemStack[] getRandomItems(ModulSchwierigkeit ms, int anzahl) {
        Random r = new Random();
        ItemStack[] returnInv = new ItemStack[27];
        int anzahlInChest = anzahl - r.nextInt(2);

        List<ItemStack> items = new ArrayList<>(Arrays.stream(Main.getPlugin().getChM().getItemList(ms)).toList());
        Collections.shuffle(items, r);
        Set<Material> seen = new HashSet<>();
        items.removeIf(item -> !seen.add(item.getType()));
        anzahlInChest = Math.min(anzahlInChest, items.size());

        // Create list 0–26
        List<Integer> numbers27 = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            numbers27.add(i);
        }
        Collections.shuffle(numbers27, r);

        // Take first anzahlInChest
        int[] chestPositions = new int[anzahlInChest];
        for (int i = 0; i < anzahlInChest; i++) {
            chestPositions[i] = numbers27.get(i);
        }

        for (int i = 0; i < anzahlInChest; i++) {
            returnInv[chestPositions[i]] = items.get(i);
        }

        return returnInv;
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

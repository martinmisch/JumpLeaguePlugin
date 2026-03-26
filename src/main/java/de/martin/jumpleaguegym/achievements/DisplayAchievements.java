package de.martin.jumpleaguegym.achievements;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DisplayAchievements {
    public static Inventory getAchievementInv(String playerName) {
        Game game = Main.getPlugin().getGame();
        List<Achievement> achievements = Main.getPlugin().getAm().getAchievements(playerName);

        Inventory inv = Bukkit.createInventory(null, 27, "Erfolge von " + playerName);
        for (int i = 0; i < achievements.size(); i++) {
            Achievement a = achievements.get(i);
            ItemStack is = new ItemStack(Material.GRAY_DYE);
            if (a.isFullfilled()) {
                is = new ItemStack(Material.GREEN_DYE);
            }
            List<String> lore = new ArrayList<>();
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName("§b" + a.getName());
            if (a.isFullfilled()) {
                lore.add("§f" + a.getDescription());
                lore.add("§fAnzahl: §7" + a.getCount());
            } else {
                lore.add("§c?????");
            }
            meta.setLore(lore);
            is.setItemMeta(meta);

            inv.setItem(i, is);
        }
        return inv;
    }

}

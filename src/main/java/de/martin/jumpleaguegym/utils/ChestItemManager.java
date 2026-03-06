package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ChestItemManager {
    private static YamlConfiguration cfg;
    private static File file;

    public ChestItemManager(Main main) {
        file = new File(main.getDataFolder(), "ChestItems.yml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void saveItems(ItemStack[] items, ModulSchwierigkeit ms, int nummer) {

        for (int i = 0; i < 54; i++) {
            if (items[i] == null) {
                items[i] = new ItemStack(Material.AIR);
            }
        }

        try {
            String serialized = Base64Encoder.arrayToBase64(items);
            cfg.set(ms.name() + nummer, serialized);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cfg.save(file);
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public ItemStack[] getItemList(ModulSchwierigkeit ms) {
        ItemStack[] items = new ItemStack[108];
        ItemStack[] items1 = getItemList(ms, 1);
        ItemStack[] items2 = getItemList(ms, 2);

        System.arraycopy(items1, 0, items, 0, items1.length);
        System.arraycopy(items2, 0, items, items1.length, items2.length);

        return items;
    }

    public ItemStack[] getItemList(ModulSchwierigkeit ms, int nummer) {
        String serialized = cfg.getString(ms.name() + nummer);
        ItemStack[] items = null;
        if (serialized != null) {
            try {
                items = Base64Encoder.arrayFromBase64(serialized, ItemStack.class);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (items == null) {
            items = new ItemStack[54];
            Arrays.fill(items, new ItemStack(Material.AIR));
        }
        return items;
    }


}

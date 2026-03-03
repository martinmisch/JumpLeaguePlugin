package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChestItemManager {
    private static YamlConfiguration cfg;
    private static File file;

    public ChestItemManager(Main main) {
        file = new File(main.getDataFolder(), "items.yml");
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

    public void saveItems(ItemStack[] items, ModulSchwierigkeit ms) {
        List<String> list = new ArrayList();
        List<String> amount = new ArrayList();

        for(int i = 0; i < items.length; ++i) {
            if (items[i] != null) {
                list.add(items[i].getType().toString());
                amount.add(String.valueOf(items[i].getAmount()));
            }
        }

        cfg.set(ms.name(), list);
        cfg.set(ms.name() + "A", amount);

        try {
            cfg.save(file);
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    public ItemStack[] getItemList(ModulSchwierigkeit ms) {
        List<String> list = getCfg().getStringList(ms.name());
        List<String> amount = getCfg().getStringList(ms.name() + "A");

        ItemStack[] items = new ItemStack[54];

        //Wenn keine Chest-Items gesetzt sind
        if(list.size() == 0 || list == null) {
            for(int k = 0; k < 54; k++) {
                items[k] = new ItemStack(Material.STONE, 1);
            }
            return items;
        }

        if (((List)amount).size() != list.size()) {
            amount = new ArrayList();

            for(int i = 0; i < list.size(); ++i) {
                ((List)amount).add("1");
            }
        }

        for(int i = 0; i < list.size(); ++i) {
            items[i] = new ItemStack(Material.valueOf((String)list.get(i)), Integer.parseInt((String)((List)amount).get(i)));
        }

        return items;
    }

    public static YamlConfiguration getCfg() {
        return cfg;
    }
}

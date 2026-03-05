package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class ChestItemManager {
    private static YamlConfiguration cfg;
    private static File file;

    public ChestItemManager(Main main) {
        file = new File(main.getDataFolder(), "items1.yml");
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
            String serialized = toBase64(items);
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
                items = fromBase64(serialized);
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

    public static String toBase64(ItemStack[] items) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(items.length);

        for (ItemStack item : items) {
            dataOutput.writeObject(item);
        }

        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static ItemStack[] fromBase64(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(Base64.getDecoder().decode(data));

        BukkitObjectInputStream dataInput =
                new BukkitObjectInputStream(inputStream);

        int size = dataInput.readInt();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            items[i] = (ItemStack) dataInput.readObject();
        }

        dataInput.close();
        return items;
    }
}

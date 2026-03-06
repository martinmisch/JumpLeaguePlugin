package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ModulRekorde {

    private static YamlConfiguration cfg;
    private static File file;

    public ModulRekorde(Main main) {
        file = new File(main.getDataFolder(), "Modulrekorde.yml");
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

    public void saveModulRekord(String playerName, int modul, int millis) {
        cfg.set(playerName + "?" + modul, millis);
        try {
            cfg.save(file);
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public int getModulRekord(String playerName, int modul) {
        int zeit = -1;
        try {
            zeit = Integer.parseInt(Objects.requireNonNull(cfg.getString(playerName + "?" + modul)));
        } catch (NumberFormatException | NullPointerException ignored) {
        }
        return zeit;
    }
}

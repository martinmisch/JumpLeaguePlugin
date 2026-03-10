package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        String s = cfg.getString("playerNames");
        if (s == null) {
            List<String> names = new ArrayList<>();
            names.add(playerName);
            try {
                cfg.set("playerNames", Base64Encoder.listToBase64(names));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            List<String> names = null;
            try {
                names = Base64Encoder.listFromBase64(cfg.getString("playerNames"), String.class);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (!names.contains("playerName")) {
                names.add(playerName);
            }
            try {
                cfg.set("playerNames", Base64Encoder.listToBase64(names));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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

    public String getModulRekordAll(int modul) {
        List<String> allPlayerNames = null;
        try {
            allPlayerNames = Base64Encoder.listFromBase64(cfg.getString("playerNames"), String.class);
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            allPlayerNames = new ArrayList<>();
        }
        if (allPlayerNames.isEmpty()) {
            return "---";
        }
        return allPlayerNames.stream().filter(p -> getModulRekord(p, modul) != -1).min(Comparator.comparingInt(p -> getModulRekord(p, modul))).orElse("---");
    }
}

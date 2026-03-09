package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeleportManager {
    private static YamlConfiguration cfg;
    private static File file;

    public TeleportManager(Main main) {
        file = new File(main.getDataFolder(), "mapLocations.yml");
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

    public boolean saveLocation(String mapName, String type, int number, Location location) {
        if (!getMaps().contains(mapName)) {
            return false;
        }
        String path = mapName + "#" + type + "#" + number;
        cfg.set(path, location);

        try {
            cfg.save(file);
        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
        return true;
    }

    public Location getLocation(String mapName, String type, int number) {
        String path = mapName + "#" + type + "#" + number;
        if (!cfg.contains(path)) {
            return null;
        }
        return cfg.getLocation(path).clone();
    }

    public Location getDfLocation(String mapName, int number) {
        if (number >= 1 && number <= 10) {
            return getLocation(mapName, "spawnpoint", number);
        }
        return null;
    }

    public Location getEndPoints(String mapName, int number) {
        if (number >= 1 && number <= 3) {
            return getLocation(mapName, "endpoint", number);
        }
        return null;
    }

    public Location getBounds(String mapName, int number) {
        if (number >= 1 && number <= 2) {
            return getLocation(mapName, "bound", number);
        }
        return null;
    }

    public boolean saveMap(String mapname) {
        List<String> maps;
        if (!cfg.contains("maps")) {
            maps = new ArrayList<>();
        } else {
            try {
                maps = Base64Encoder.listFromBase64(cfg.getString("maps"), String.class);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (maps == null) {
            maps = new ArrayList<String>();
            maps.add(mapname);
            try {
                cfg.set("maps", Base64Encoder.listToBase64(maps));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        if (maps.contains(mapname)) {
            return false;
        }
        maps.add(mapname);
        try {
            cfg.set("maps", Base64Encoder.listToBase64(maps));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            cfg.save(file);
        } catch (IOException var7) {
            var7.printStackTrace();
        }
        return true;
    }

    public boolean deleteMap(String mapname) {

        List<String> maps;
        try {
            maps = Base64Encoder.listFromBase64(cfg.getString("maps"), String.class);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        boolean deleted = maps.remove(mapname);

        try {
            cfg.set("maps", Base64Encoder.listToBase64(maps));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            cfg.save(file);
        } catch (IOException var7) {
            var7.printStackTrace();
        }
        return deleted;
    }

    public List<String> getMaps() {
        if (!cfg.contains("maps")) {
            return new ArrayList<>();
        }
        List<String> maps;
        try {
            maps = Base64Encoder.listFromBase64(cfg.getString("maps"), String.class);
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }

        if (maps == null) {
            return new ArrayList<>();
        }
        return maps;
    }

    public int getMapAnzahl() {
        List<String> maps = getMaps();
        int size = maps.size();

        Main.getPlugin().getLogger().info("Anzahl Deathmatch-Maps: " + size);
        return size;
    }

    public static YamlConfiguration getCfg() {
        return cfg;
    }
}

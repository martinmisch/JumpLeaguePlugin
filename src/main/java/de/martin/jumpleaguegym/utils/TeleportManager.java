package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
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
        file = new File(main.getDataFolder(), "locations.yml");
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

    public void saveEndPoint(String name, int map, Location location) {
        String s = name + ":" + location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ();

        Object list;
        try {
            list = cfg.getStringList("EndPoints" + map);
        } catch (Exception var8) {
            list = new ArrayList();
        }

        for(int i = 0; i < ((List)list).size(); ++i) {
            if (((String)((List)list).get(i)).startsWith(name)) {
                ((List)list).remove(i);
            }
        }

        ((List)list).add(s);
        cfg.set("EndPoints" + map, list);

        try {
            cfg.save(file);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public Location getEndPoints(int map, int nummer) {
        if (nummer > 0 && nummer <= 3) {
            List<String> list = getCfg().getStringList("EndPoints" + map);

            for(int k = 0; k < list.size(); ++k) {
                if (((String)list.get(k)).startsWith("end" + nummer)) {
                    Location loc = new Location(Bukkit.getWorld(((String)list.get(k)).split(":")[1]), Double.valueOf(((String)list.get(k)).split(":")[2]), Double.valueOf(((String)list.get(k)).split(":")[3]), Double.valueOf(((String)list.get(k)).split(":")[4]));
                    return loc;
                }
            }
        }

        return null;
    }

    public void saveDfLocation(String name, int map, Location location) {
        String s = name + ":" + location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();

        Object list;
        try {
            list = cfg.getStringList("DfLocs" + map);
        } catch (Exception var8) {
            list = new ArrayList();
        }

        for(int i = 0; i < ((List)list).size(); ++i) {
            if (((String)((List)list).get(i)).startsWith(name)) {
                ((List)list).remove(i);
            }
        }

        ((List)list).add(s);
        cfg.set("DfLocs" + map, list);

        try {
            cfg.save(file);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public Location getDfLocation(int map, int nummer) {
        if (nummer > 0 && nummer <= 10) {
            List<String> list = getCfg().getStringList("DfLocs" + map);

            for(int k = 0; k < list.size(); ++k) {
                if (((String)list.get(k)).startsWith("df" + nummer)) {
                    Location loc = new Location(Bukkit.getWorld(((String)list.get(k)).split(":")[1]), Double.valueOf(((String)list.get(k)).split(":")[2]), Double.valueOf(((String)list.get(k)).split(":")[3]), Double.valueOf(((String)list.get(k)).split(":")[4]), Float.valueOf(((String)list.get(k)).split(":")[5]), Float.valueOf(((String)list.get(k)).split(":")[6]));
                    return loc;
                }
            }
        }

        return null;
    }

    public int getMapAnzahl() {
        int i = 0;

        for(int k = 1; k <= 100; ++k) {
            List<String> list = getCfg().getStringList("DfLocs" + k);
            if (list == null || list.size() <= 0) {
                break;
            }

            ++i;
        }

        System.out.println("Anzahl maps: " + i);
        return i;
    }

    public static YamlConfiguration getCfg() {
        return cfg;
    }
}

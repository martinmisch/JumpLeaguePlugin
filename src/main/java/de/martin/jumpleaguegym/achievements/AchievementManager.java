package de.martin.jumpleaguegym.achievements;

import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.Base64Encoder;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AchievementManager {

    private static YamlConfiguration cfg;
    private static File file;

    public AchievementManager(Main main) {
        file = new File(main.getDataFolder(), "Achievements.yml");
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

    public void saveAchievement(String playerName, Achievement am) {
        List<Achievement> achievements = getAchievements(playerName);

        if (!achievements.stream().map(Achievement::getType).toList().contains(am.getType())) {
            achievements.add(am);
        } else {
            int index = IntStream.range(0, achievements.size())
                    .filter(i -> am.getType().equals(achievements.get(i).getType()))
                    .findFirst()
                    .orElse(-1);
            achievements.set(index, am);
        }
        try {
            cfg.set(playerName, Base64Encoder.listToBase64(achievements));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            cfg.save(file);
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    public List<Achievement> getAchievements(String playerName) {
        List<Achievement> list = null;
        try {
            list = Base64Encoder.listFromBase64(cfg.getString(playerName), Achievement.class);
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            list = new ArrayList<>();
        }
        List<AchievementType> achievementNames = list.stream().map(Achievement::getType).toList();
        if (!achievementNames.contains(AchievementType.REIN_IN_DIE_OLGA)) {
            list.add(new Achievement(AchievementType.REIN_IN_DIE_OLGA, "Rein in die Olga", playerName, "Erreiche das Ziel."));
        }
        if (!achievementNames.contains(AchievementType.GROSSMEISTER)) {
            list.add(new Achievement(AchievementType.GROSSMEISTER, "Großmeister", playerName, "Erreiche das Ziel ohne Fail."));
        }
        if (!achievementNames.contains(AchievementType.DABEI_SEIN_IST_ALLES)) {
            list.add(new Achievement(AchievementType.DABEI_SEIN_IST_ALLES, "Dabei sein ist alles", playerName, "Mache 150 Fails in einer Runde."));
        }
        if (!achievementNames.contains(AchievementType.ZU_FRUEH_GEKOMMEN)) {
            list.add(new Achievement(AchievementType.ZU_FRUEH_GEKOMMEN, "Zu früh gekommen", playerName, "Erreiche das Ziel in 4 Minuten"));
        }

        return list;
    }

    public void achievementZielErreicht() {
        if (Main.getPlugin().getGame().getAnzahlSchwer() < 3 || Main.getPlugin().getGame().getAnzahlMittel() < 3 || Main.getPlugin().getGame().getAnzahlSchwer() + Main.getPlugin().getGame().getAnzahlMittel() + Main.getPlugin().getGame().getAnzahlLeicht() < 10) {
            return;
        }
        List<JlPlayer> players = Main.getPlugin().getGame().getPlayers();

        for (JlPlayer p : players) {
            if (p.isZielErreicht()) {
                List<Achievement> a = getAchievements(p.getPlayer().getName());
                Achievement ach = a.stream().filter(ac -> ac.getType() == AchievementType.REIN_IN_DIE_OLGA).findFirst().orElse(null);
                assert ach != null;
                ach.setCount(ach.getCount() + 1);
                ach.setFullfilled(true);
                saveAchievement(p.getPlayer().getName(), ach);
            }
        }

        for (JlPlayer p : players) {
            if (p.isZielErreicht() && p.getJumpFails() == 0) {
                List<Achievement> a = getAchievements(p.getPlayer().getName());
                Achievement ach = a.stream().filter(ac -> ac.getType() == AchievementType.GROSSMEISTER).findFirst().orElse(null);
                assert ach != null;
                ach.setCount(ach.getCount() + 1);
                ach.setFullfilled(true);
                saveAchievement(p.getPlayer().getName(), ach);
            }
        }

        for (JlPlayer p : players) {
            if (p.getJumpFails() >= 150) {
                List<Achievement> a = getAchievements(p.getPlayer().getName());
                Achievement ach = a.stream().filter(ac -> ac.getType() == AchievementType.DABEI_SEIN_IST_ALLES).findFirst().orElse(null);
                assert ach != null;
                ach.setCount(ach.getCount() + 1);
                ach.setFullfilled(true);
                saveAchievement(p.getPlayer().getName(), ach);
            }
        }
    }

    public void achievementZielInTime() {
        if (Main.getPlugin().getGame().getAnzahlSchwer() < 3 || Main.getPlugin().getGame().getAnzahlMittel() < 3 || Main.getPlugin().getGame().getAnzahlSchwer() + Main.getPlugin().getGame().getAnzahlMittel() + Main.getPlugin().getGame().getAnzahlLeicht() < 10) {
            return;
        }
        List<JlPlayer> players = Main.getPlugin().getGame().getPlayers();
        for (JlPlayer p : players) {
            if (p.isZielErreicht() && (Main.getPlugin().getGame().getJumpZeit() * 60 - Main.getPlugin().getGame().getJp().getCountdown() <= 240)) {
                List<Achievement> a = getAchievements(p.getPlayer().getName());
                Achievement ach = a.stream().filter(ac -> ac.getType() == AchievementType.ZU_FRUEH_GEKOMMEN).findFirst().orElse(null);
                assert ach != null;
                ach.setCount(ach.getCount() + 1);
                ach.setFullfilled(true);
                saveAchievement(p.getPlayer().getName(), ach);
            }
        }
    }
}

package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class ScoreBoardSort {
    public ScoreBoardSort() {
    }

    public static List<JlPlayer> getSortedProzent(List<JlPlayer> players) {
        return players.stream().sorted(Comparator.comparingDouble(p -> getProzent(p.getPlayer()))).toList();
    }

    public static List<JlPlayer> getSortedLeben(List<JlPlayer> players) {
        return players.stream().filter(p -> p.getDeathCount() <= 2).sorted(Comparator.comparingInt(p -> 3 - p.getDeathCount())).toList();
    }

    public static int getProzent(Player p) {
        Game game = Main.getPlugin().getGame();
        if (game.containsPlayer(p)) {
            double jLenght = (double) (game.getCj().getEndX() - game.getCj().getJumpStartX());
            double playerProgress = p.getLocation().getX() - (double) game.getCj().getJumpStartX();
            int prozent = (int) (playerProgress / jLenght * 100.0);
            if (prozent > 100) {
                prozent = 100;
            }

            return prozent;
        } else {
            return -1;
        }
    }
}

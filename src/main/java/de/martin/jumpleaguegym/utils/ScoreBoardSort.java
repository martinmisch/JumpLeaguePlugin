package de.martin.jumpleaguegym.utils;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ScoreBoardSort {
    public ScoreBoardSort() {
    }

    public static ArrayList<JlPlayer> getSortedProzent(JlPlayer[] players) {
        ArrayList<JlPlayer> sorted = new ArrayList();
        sorted.clear();

        for(int i = 0; i < 100; ++i) {
            for(int k = 0; k < players.length; ++k) {
                if (players[k] != null && getProzent(players[k].getPlayer()) == i && !sorted.contains(players[k])) {
                    sorted.add(players[k]);
                }
            }
        }

        return sorted;
    }

    public static ArrayList<JlPlayer> getSortedLeben(JlPlayer[] players) {
        ArrayList<JlPlayer> sorted = new ArrayList();
        sorted.clear();

        for(int i = 0; i <= 3; ++i) {
            for(int k = players.length - 1; k >= 0; --k) {
                if (players[k] != null && 3 - players[k].getDeathCount() == i && !sorted.contains(players[k])) {
                    sorted.add(players[k]);
                }
            }
        }

        return sorted;
    }

    public static int getProzent(Player p) {
        Game game = Main.getPlugin().getGame();
        if (game.containsPlayer(p)) {
            double jLenght = (double)(game.getCj().getEndX() - game.getCj().getJumpStartX());
            double playerProgress = p.getLocation().getX() - (double)game.getCj().getJumpStartX();
            int prozent = (int)(playerProgress / jLenght * 100.0);
            if (prozent > 100) {
                prozent = 100;
            }

            return prozent;
        } else {
            return -1;
        }
    }
}

package de.martin.jumpleaguegym.game.practise;

import org.bukkit.entity.Player;

public class PractiseModePlayer {
    private Player p;
    private int modul;

    public PractiseModePlayer(Player p, int modul) {
        this.p = p;
        this.modul = modul;
    }

    public Player getPlayer() {
        return this.p;
    }

    public int getModul() {
        return this.modul;
    }
}

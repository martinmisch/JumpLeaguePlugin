package de.martin.jumpleaguegym.game.practise;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PractiseMode {
    private ArrayList<PractiseModePlayer> players = new ArrayList();
    private Game game;

    public PractiseMode() {
    }

    public boolean contains(Player p) {
        for(int i = 0; i < this.players.size(); ++i) {
            if (((PractiseModePlayer)this.players.get(i)).getPlayer().equals(p)) {
                return true;
            }
        }

        return false;
    }

    public int getIndex(Player p) {
        for(int i = 0; i < this.players.size(); ++i) {
            if (((PractiseModePlayer)this.players.get(i)).getPlayer().equals(p)) {
                return i;
            }
        }

        return -1;
    }

    public void join(Player p, int modul) {
        this.game = Main.getPlugin().getGame();

        for(int i = 0; i < this.players.size(); ++i) {
            if (((PractiseModePlayer)this.players.get(i)).getPlayer().equals(p)) {
                p.sendMessage("§e[JLG] §fVerlasse erst den Practise-Mode (/practise leave).");
                return;
            }
        }

        this.players.add(new PractiseModePlayer(p, modul));
        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 51.5, (double)(((PractiseModePlayer)this.game.getPractise().getPlayers().get(this.game.getPractise().getIndex(p))).getModul() * 50), 270.0F, 15.0F));
        p.sendMessage("§e[JLG] §fDu übst nun Modul " + modul + ".");
    }

    public void leave(Player p) {
        for(int i = 0; i < this.players.size(); ++i) {
            if (((PractiseModePlayer)this.players.get(i)).getPlayer().equals(p)) {
                this.players.remove(i);
                p.sendMessage("§e[JLG] §fDu bist nun nicht mehr im Practise-Mode.");
                return;
            }
        }

        p.sendMessage("§e[JLG] §fDu befindest dich nicht im Practise-Mode.");
    }

    public ArrayList<PractiseModePlayer> getPlayers() {
        return this.players;
    }
}

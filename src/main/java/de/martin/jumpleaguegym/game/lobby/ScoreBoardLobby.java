package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class ScoreBoardLobby {
    private Scoreboard scB;
    private Game game;
    private Objective sideBar;
    private String[] prePlayer;
    private String playerMax;
    private JlPlayer[] players;

    public ScoreBoardLobby() {
    }

    public void setScoreBoard() {
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        this.scB = Bukkit.getScoreboardManager().getNewScoreboard();
        this.prePlayer = new String[this.players.length];
        Arrays.fill(this.prePlayer, "");
        this.sideBar = this.scB.registerNewObjective("time", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.sideBar.setDisplayName("§c[JLG] §f");
        this.playerMax = "Spieler: ";

        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i] != null) {
                this.prePlayer[i] = this.players[i].getPlayer().getName();
            }
        }

    }

    public void update() {
        this.sideBar.getScoreboard().resetScores(this.playerMax);
        this.playerMax = "§7Spieler: (§a" + this.game.getJoinedPlayers() + "/" + this.game.getAnzahlSpieler() + "§7)";
        this.sideBar.getScore(this.playerMax).setScore(15);

        for(int i = 0; i < this.players.length; ++i) {
            if (this.prePlayer[i] != "") {
                this.sideBar.getScoreboard().resetScores(this.prePlayer[i]);
                this.prePlayer[i] = "";
            }

            if (this.players[i] != null) {
                this.prePlayer[i] = "§f" + this.players[i].getPlayer().getName();
                this.sideBar.getScore(this.prePlayer[i]).setScore(i);
            }
        }

    }

    public Scoreboard getScB() {
        return this.scB;
    }
}

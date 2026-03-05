package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.LinkedList;
import java.util.List;

public class ScoreBoardLobby {
    private Scoreboard scB;
    private Game game;
    private Objective sideBar;
    private List<String> prePlayers;
    private String playerMax;
    private List<JlPlayer> players;

    public ScoreBoardLobby() {
    }

    public void setScoreBoard() {
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        this.scB = Bukkit.getScoreboardManager().getNewScoreboard();
        this.prePlayers = new LinkedList<>();
        this.sideBar = this.scB.registerNewObjective("time", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.sideBar.setDisplayName("§c[JLG] §f");
        this.playerMax = "Spieler: ";

        prePlayers.addAll(players.stream().map(p -> p.getPlayer().getName()).toList());
    }

    public void update() {
        this.sideBar.getScoreboard().resetScores(this.playerMax);
        this.playerMax = "§7Spieler: (§a" + this.game.getJoinedPlayers() + "§7)";
        this.sideBar.getScore(this.playerMax).setScore(15);

        for (String s : prePlayers) {
            this.sideBar.getScoreboard().resetScores(s);
        }

        prePlayers.clear();
        prePlayers.addAll(players.stream().map(p -> "§f" + p.getPlayer().getName()).toList());

        for (int i = 0; i < prePlayers.size(); i++) {
            this.sideBar.getScore(this.prePlayers.get(i)).setScore(i);
        }
    }

    public Scoreboard getScB() {
        return this.scB;
    }
}

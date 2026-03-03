package de.martin.jumpleaguegym.game.pvp;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.ScoreBoardSort;
import de.martin.jumpleaguegym.utils.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class ScoreBoardPvp {
    private Scoreboard scB;
    private Game game;
    private Objective sideBar;
    private String preTime;
    private String[] preLeben;
    private ArrayList<JlPlayer> sortedPlayers;
    private JlPlayer[] players;

    public ScoreBoardPvp() {
    }

    public void setScoreBoard() {
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        this.scB = Bukkit.getScoreboardManager().getNewScoreboard();
        this.preLeben = new String[this.players.length];
        this.sideBar = this.scB.registerNewObjective("time", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.sideBar.setDisplayName("§c[JLG] §f");
        this.preTime = "§rZeit";
        this.sideBar.getScore(this.preTime).setScore(15);
        this.sideBar.getScore("").setScore(14);

        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i] != null) {
                this.preLeben[i] = this.players[i].getPlayer().getName() + " (§c" + (3 - this.players[i].getDeathCount()) + "§f)";
                this.players[i].getPlayer().setScoreboard(this.scB);
            }
        }

    }

    public void update(int time) {
        this.sortedPlayers = ScoreBoardSort.getSortedLeben(this.game.getPlayers());
        this.sideBar.getScoreboard().resetScores(this.preTime);
        this.preTime = "§7Zeit: §a" + TimeFormat.getTime(time);
        this.sideBar.getScore(this.preTime).setScore(15);

        for(int i = 0; i < this.sortedPlayers.size(); ++i) {
            if (this.sortedPlayers.get(i) != null) {
                this.sideBar.getScoreboard().resetScores(this.preLeben[i]);
                if (((JlPlayer)this.sortedPlayers.get(i)).isAlive()) {
                    this.preLeben[i] = "§f" + ((JlPlayer)this.sortedPlayers.get(i)).getPlayer().getName() + " (§c" + (3 - ((JlPlayer)this.sortedPlayers.get(i)).getDeathCount()) + "§f)";
                    this.sideBar.getScore(this.preLeben[i]).setScore(i);
                }
            }
        }

    }
}

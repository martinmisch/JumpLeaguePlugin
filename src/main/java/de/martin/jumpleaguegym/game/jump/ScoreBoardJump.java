package de.martin.jumpleaguegym.game.jump;

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

public class ScoreBoardJump {
    private Scoreboard scB;
    private Game game;
    private Objective sideBar;
    ArrayList<JlPlayer> sortedPlayers;
    private String preTime;
    private String[] preProzent;

    public ScoreBoardJump() {
    }

    public void setScoreBoard() {
        this.game = Main.getPlugin().getGame();
        this.scB = Bukkit.getScoreboardManager().getNewScoreboard();
        this.preProzent = new String[this.game.getPlayers().length];
        this.sideBar = this.scB.registerNewObjective("time", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.sideBar.setDisplayName("§c[JLG] §f");
        this.sideBar.getScore("").setScore(14);
        this.preTime = "§7Zeit: ";
        this.sideBar.getScore(this.preTime).setScore(15);

        for(int i = 0; i < this.game.getPlayers().length; ++i) {
            if (this.game.getPlayers()[i] == null) {
                this.preProzent[i] = "";
            } else {
                this.preProzent[i] = "§f" + this.game.getPlayers()[i].getPlayer().getName() + " §7(0%)";
                this.game.getPlayers()[i].getPlayer().setScoreboard(this.scB);
            }
        }

    }

    public void update(int time) {
        this.sortedPlayers = ScoreBoardSort.getSortedProzent(this.game.getPlayers());
        this.sideBar.getScoreboard().resetScores(this.preTime);
        this.preTime = "§7Zeit: §a" + TimeFormat.getTime(time);
        this.sideBar.getScore(this.preTime).setScore(15);

        for(int i = 0; i < this.sortedPlayers.size(); ++i) {
            this.sideBar.getScoreboard().resetScores(this.preProzent[i]);
            if (this.sortedPlayers.get(i) != null) {
                this.preProzent[i] = "§f" + ((JlPlayer)this.sortedPlayers.get(i)).getPlayer().getName() + " §7(" + ScoreBoardSort.getProzent(((JlPlayer)this.sortedPlayers.get(i)).getPlayer()) + "%)";
                this.sideBar.getScore(this.preProzent[i]).setScore(i);
            }
        }

    }
}

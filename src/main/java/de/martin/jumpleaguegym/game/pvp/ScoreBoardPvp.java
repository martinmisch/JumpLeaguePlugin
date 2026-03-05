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

import java.util.LinkedList;
import java.util.List;

public class ScoreBoardPvp {
    private Scoreboard scB;
    private Game game;
    private Objective sideBar;
    private String preTime;
    private List<String> preLeben;
    private List<JlPlayer> players;

    public ScoreBoardPvp() {
    }

    public void setScoreBoard() {
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        this.scB = Bukkit.getScoreboardManager().getNewScoreboard();
        this.preLeben = new LinkedList<>();
        this.sideBar = this.scB.registerNewObjective("time", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.sideBar.setDisplayName("§c[JLG] §f");
        this.sideBar.getScore("").setScore(14);
        this.preTime = "§rZeit";
        this.sideBar.getScore(this.preTime).setScore(15);

        preLeben.addAll(players.stream().map(p -> p.getPlayer().getName() + " (§c" + (3 - p.getDeathCount()) + "§f)").toList());
        players.forEach(p -> p.getPlayer().setScoreboard(this.scB));

    }

    public void update(int time) {
        this.sideBar.getScoreboard().resetScores(this.preTime);
        this.preTime = "§7Zeit: §a" + TimeFormat.getTime(time);
        this.sideBar.getScore(this.preTime).setScore(15);

        for (String s : preLeben) {
            this.sideBar.getScoreboard().resetScores(s);
        }
        preLeben.clear();

        List<JlPlayer> sortedPlayers = ScoreBoardSort.getSortedLeben(players);
        preLeben.addAll(sortedPlayers.stream().map(p -> "§f" + p.getPlayer().getName() + " (§c" + (3 - p.getDeathCount()) + "§f)").toList());


        for (int i = 0; i < preLeben.size(); i++) {
            this.sideBar.getScore(preLeben.get(i)).setScore(i);
        }
    }
}

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

import java.util.LinkedList;
import java.util.List;

public class ScoreBoardJump {
    private Scoreboard scB;
    private Game game;
    private Objective sideBar;
    private String preTime;
    private List<String> preProzent;

    private List<JlPlayer> players;


    public ScoreBoardJump() {
    }

    public void setScoreBoard() {
        this.game = Main.getPlugin().getGame();
        this.players = game.getPlayers();
        this.scB = Bukkit.getScoreboardManager().getNewScoreboard();
        this.preProzent = new LinkedList<>();
        this.sideBar = this.scB.registerNewObjective("time", "dummy");
        this.sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.sideBar.setDisplayName("§c[JLG] §f");
        this.sideBar.getScore("").setScore(14);
        this.preTime = "§7Zeit: ";
        this.sideBar.getScore(this.preTime).setScore(15);

        preProzent.addAll(players.stream().map(p -> "§f" + p.getPlayer().getName() + " §7(0%)" + "§f - §e" + (p.getPlayerCheckpointsNumber() + 1)).toList());
        players.forEach(p -> p.getPlayer().setScoreboard(this.scB));
    }

    public void update(int time) {
        this.sideBar.getScoreboard().resetScores(this.preTime);
        this.preTime = "§7Zeit: §a" + TimeFormat.getTimeSM(time);
        this.sideBar.getScore(this.preTime).setScore(15);

        for (String s : preProzent) {
            this.sideBar.getScoreboard().resetScores(s);
        }
        preProzent.clear();

        List<JlPlayer> sortedPlayers = ScoreBoardSort.getSortedProzent(players);
        preProzent.addAll(sortedPlayers.stream().map(p -> "§f" + p.getPlayer().getName() + " §7(" + ScoreBoardSort.getProzent(p.getPlayer()) + "%)" + "§f - §e" + (p.getPlayerCheckpointsNumber() + 1)).toList());


        for (int i = 0; i < preProzent.size(); i++) {
            this.sideBar.getScore(preProzent.get(i)).setScore(i);
        }
    }
}

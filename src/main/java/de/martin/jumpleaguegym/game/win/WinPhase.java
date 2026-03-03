package de.martin.jumpleaguegym.game.win;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WinPhase {
    private Game game = Main.getPlugin().getGame();
    private JlPlayer[] players;
    private int taskID;

    public WinPhase() {
    }

    public void startWin() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            int i = 11;

            public void run() {
                WinPhase.this.game = Main.getPlugin().getGame();
                if (this.i == 10) {
                    WinPhase.this.players = WinPhase.this.game.getPlayers();
                    String s = "Niemand";

                    int i;
                    for(i = 0; i < WinPhase.this.players.length; ++i) {
                        if (WinPhase.this.players[i] != null && WinPhase.this.players[i].isAlive()) {
                            if (s == "Niemand") {
                                s = WinPhase.this.players[i].getPlayer().getName();
                            } else {
                                s = s + " & " + WinPhase.this.players[i].getPlayer().getName();
                            }
                        }
                    }

                    Bukkit.broadcastMessage("§c[JLG] §f" + s + " hat die Runde gewonnen.");

                    for(i = 0; i < WinPhase.this.players.length; ++i) {
                        if (WinPhase.this.players[i] != null) {
                            Player p = WinPhase.this.players[i].getPlayer();
                            p.teleport(new Location(Bukkit.getWorld("world"), -100.0, 51.0, 0.0, 180.0F, 15.0F));
                            p.setGameMode(GameMode.ADVENTURE);
                            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                            p.getInventory().clear();
                            p.getInventory().setArmorContents((ItemStack[])null);
                            p.setHealth(20.0);
                            p.setFoodLevel(20);
                            p.sendTitle("§a" + s, "§ahat die Runde gewonnen.");
                        }
                    }

                    WinPhase.this.game.opPlayers();
                }

                Bukkit.broadcastMessage("§c[JLG] §fDie Lobby startet in " + this.i + "s neu.");
                if (this.i <= 0) {
                    WinPhase.this.game.reset();
                    Bukkit.getScheduler().cancelTask(WinPhase.this.taskID);
                }

                --this.i;
            }
        }, 20L, 20L);
    }
}

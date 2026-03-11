package de.martin.jumpleaguegym.game.win;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.NoSuchElementException;

public class WinPhase {
    private int taskID;

    public WinPhase() {
    }

    public void startWin() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            int i = 11;

            public void run() {
                Game game = Main.getPlugin().getGame();
                List<JlPlayer> players = game.getPlayers();
                if (this.i == 10) {
                    String s = "";
                    try {
                        s = players.stream().filter(JlPlayer::isAlive).toList().getFirst().getPlayer().getName();
                    } catch (NoSuchElementException e) {
                        s = "Niemand";
                    }

                    Bukkit.broadcastMessage("§c[JLG] §f" + s + " hat die Runde gewonnen.");

                    for (Player p : players.stream().map(JlPlayer::getPlayer).toList()) {
                        p.teleport(new Location(Bukkit.getWorld("world"), -100.0, 51.0, 0.0, 180.0F, 15.0F));
                        p.setGameMode(GameMode.ADVENTURE);
                        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                        p.getInventory().clear();
                        p.getInventory().setArmorContents((ItemStack[]) null);
                        p.setHealth(20.0);
                        p.setFoodLevel(20);
                        p.sendTitle("§a" + s, "§ahat die Runde gewonnen.");
                    }
                }

                Bukkit.broadcastMessage("§c[JLG] §fDie Lobby startet in " + this.i + "s neu.");
                if (this.i == 0) {
                    game.kickPlayers();
                }
                if (this.i == -1) {
                    Bukkit.getScheduler().cancelTask(WinPhase.this.taskID);
                    Main.getPlugin().resetAndCreate();
                }

                --this.i;
            }
        }, 20L, 20L);
    }
}

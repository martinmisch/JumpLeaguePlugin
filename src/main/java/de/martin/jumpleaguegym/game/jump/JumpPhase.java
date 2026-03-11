package de.martin.jumpleaguegym.game.jump;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JumpPhase {
    private int taskID;
    private Game game;
    private ScoreBoardJump scJ;
    private boolean zielErreicht;
    private List<JlPlayer> players;

    public JumpPhase() {
    }

    public void startGame() {
        this.zielErreicht = false;
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        World world = Bukkit.getServer().getWorld("world");
        this.scJ = new ScoreBoardJump();
        this.scJ.setScoreBoard();

        for (JlPlayer jp : players) {
            Player p = jp.getPlayer();
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)});
            p.getInventory().setItem(0, new ItemStack(Material.WOODEN_AXE));
            jp.setPlayerCheckPointLocation(new Location(world, (double) this.game.getCj().getJumpStartX() + 0.5, (double) (this.game.getCj().getJumpStartY() + 1), (double) (this.game.getCj().getJumpStartZ() + 50 * jp.getPlayerIndex()) + 0.5, 270.0F, 15.0F));
            p.teleport(new Location(world, (double) this.game.getCj().getJumpStartX() + 0.5, (double) Main.getPlugin().getGame().getCj().getJumpStartY() + 1.5, (double) Main.getPlugin().getGame().getCj().getJumpStartZ() + 0.5 + (double) (50 * jp.getPlayerIndex()), 270.0F, 15.0F));
            p.getPlayer().getInventory().setItem(8, (new CreateItem("Zuruecksetzen", Material.BARRIER, 1)).getItemStack());
            p.getPlayer().setFoodLevel(20);
        }

        Game.setGs(GameStates.JUMPCOUNT);
        this.countdown();
    }

    public void reset() {
        this.zielErreicht = false;
    }

    public void countdown() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            int countdown = 5;

            public void run() {
                if (this.countdown <= 0) {
                    Game.setGs(GameStates.JUMP);
                    Bukkit.getScheduler().cancelTask(JumpPhase.this.taskID);
                    JumpPhase.this.jumpCountdown();
                } else {
                    for (JlPlayer p : players) {
                        p.getPlayer().resetTitle();
                        p.getPlayer().sendTitle("§a" + this.countdown, "");
                        p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                    }
                }
                --this.countdown;
            }
        }, 0L, 20L);
    }

    public void jumpCountdown() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            int countdown;

            {
                this.countdown = JumpPhase.this.game.getJumpZeit() * 60;
            }

            public void run() {

                if (this.countdown == JumpPhase.this.game.getJumpZeit() * 60) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie Jumpphase beginnt.");

                    players.forEach(p -> {
                        p.getPlayer().resetTitle();
                        p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                        p.setSystemTimeLastCheckpoint(System.currentTimeMillis());
                    });
                }

                JumpPhase.this.scJ.update(this.countdown);
                if (JumpPhase.this.win()) {
                    Bukkit.getScheduler().cancelTask(JumpPhase.this.taskID);
                    game.kickPlayers();
                    Main.getPlugin().resetAndCreate();
                }

                if (JumpPhase.this.zielErreicht && this.countdown > 10) {
                    this.countdown = 10;
                }

                if (this.countdown <= 10) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie Jumpphase endet in " + this.countdown + " Sekunden.");
                    players.forEach(p -> p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.7F));
                }

                if (this.countdown <= 0) {
                    players.forEach(p -> p.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR)));
                    Game.setGs(GameStates.PVP);
                    Main.getPlugin().getGame().getPp().startGame();
                    Bukkit.getScheduler().cancelTask(JumpPhase.this.taskID);
                }

                --this.countdown;
            }
        }, 0L, 20L);
    }

    public boolean win() {
        //TODO hier muss <= 1 gesetzt werden, damit die Runde endet, wenn während der Jumpphhase nur noch ein Spieler lebt
        return players.stream().filter(JlPlayer::isAlive).toList().size() <= 0;
    }

    public boolean isZielErreicht() {
        return this.zielErreicht;
    }

    public void setZielErreicht(boolean zielErreicht) {
        this.zielErreicht = zielErreicht;
    }
}

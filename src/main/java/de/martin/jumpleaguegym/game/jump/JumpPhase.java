package de.martin.jumpleaguegym.game.jump;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;

public class JumpPhase {
    private int taskID;
    private Game game;
    private ScoreBoardJump scJ;
    private boolean zielErreicht;
    private JlPlayer[] players;

    public JumpPhase() {
    }

    public void startGame() {
        this.zielErreicht = false;
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        this.game.deOpPlayers();
        World world = Bukkit.getServer().getWorld("world");
        this.scJ = new ScoreBoardJump();
        this.scJ.setScoreBoard();
        System.out.println("countdown start");

        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i] != null && this.game.containsPlayer(this.players[i].getPlayer())) {
                this.players[i].getPlayer().setGameMode(GameMode.ADVENTURE);
                this.players[i].getPlayer().getInventory().clear();
                this.players[i].getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)});
                this.players[i].getPlayer().getInventory().setItem(0, new ItemStack(Material.WOODEN_AXE));
                this.players[i].setPlayerCheckPointLocation(new Location(world, (double)this.game.getCj().getJumpStartX() + 0.5, (double)(this.game.getCj().getJumpStartY() + 1), (double)(this.game.getCj().getJumpStartZ() + 50 * i) + 0.5, 270.0F, 15.0F));
                this.players[i].getPlayer().teleport(new Location(world, (double)this.game.getCj().getJumpStartX() + 0.5, (double)Main.getPlugin().getGame().getCj().getJumpStartY() + 1.5, (double)Main.getPlugin().getGame().getCj().getJumpStartZ() + 0.5 + (double)(50 * i), 270.0F, 15.0F));
                this.players[i].getPlayer().getInventory().setItem(8, (new CreateItem("Zuruecksetzen", Material.BARRIER, 1)).getItemStack());
                this.players[i].getPlayer().setFoodLevel(20);
            }
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
                    JlPlayer[] var4;
                    int var3 = (var4 = JumpPhase.this.game.getPlayers()).length;

                    for(int var2 = 0; var2 < var3; ++var2) {
                        JlPlayer p = var4[var2];
                        if (p != null) {
                            p.getPlayer().resetTitle();
                            p.getPlayer().sendTitle("§a" + this.countdown, "");
                            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                        }
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
                JlPlayer p;
                int var2;
                int var3;
                JlPlayer[] var4;
                if (this.countdown == JumpPhase.this.game.getJumpZeit() * 60) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie Jumpphase beginnt.");
                    var3 = (var4 = JumpPhase.this.players).length;

                    for(var2 = 0; var2 < var3; ++var2) {
                        p = var4[var2];
                        if (p != null) {
                            p.getPlayer().resetTitle();
                            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                        }
                    }
                }

                JumpPhase.this.scJ.update(this.countdown);
                if (JumpPhase.this.win()) {
                    JumpPhase.this.game.reset();
                    Bukkit.getScheduler().cancelTask(JumpPhase.this.taskID);
                }

                if (JumpPhase.this.zielErreicht && this.countdown > 10) {
                    this.countdown = 10;
                }

                if (this.countdown <= 10) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie Jumpphase endet in " + this.countdown + " Sekunden.");
                    var3 = (var4 = JumpPhase.this.players).length;

                    for(var2 = 0; var2 < var3; ++var2) {
                        p = var4[var2];
                        if (p != null) {
                            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.7F);
                        }
                    }
                }

                if (this.countdown <= 0) {
                    var3 = (var4 = JumpPhase.this.players).length;

                    for(var2 = 0; var2 < var3; ++var2) {
                        p = var4[var2];
                        if (p != null) {
                            p.getPlayer().getInventory().setItem(8, new ItemStack(Material.AIR));
                        }
                    }

                    Game.setGs(GameStates.PVP);
                    Main.getPlugin().getGame().getPp().startGame();
                    Bukkit.getScheduler().cancelTask(JumpPhase.this.taskID);
                }

                --this.countdown;
            }
        }, 0L, 20L);
    }

    public boolean win() {
        int aliveCount = 0;

        for(int i = 0; i < this.players.length; ++i) {
            if (this.players[i] != null && this.players[i].isAlive()) {
                ++aliveCount;
            }
        }

        if (aliveCount <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isZielErreicht() {
        return this.zielErreicht;
    }

    public void setZielErreicht(boolean zielErreicht) {
        this.zielErreicht = zielErreicht;
    }
}

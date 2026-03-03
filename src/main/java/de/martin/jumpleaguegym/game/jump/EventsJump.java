package de.martin.jumpleaguegym.game.jump;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventsJump implements Listener {
    private JumpPhase jp = Main.getPlugin().getGame().getJp();
    private JlPlayer[] players;
    private Game game;

    public EventsJump() {
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT)) {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT)) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        this.players = Main.getPlugin().getGame().getPlayers();
        Player p = e.getPlayer();
        this.game = Main.getPlugin().getGame();
        if (this.players != null) {
            if (this.game.containsPlayer(e.getPlayer())) {
                if (Game.getGs().equals(GameStates.JUMPCOUNT) && (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())) {
                    this.players[this.game.getPlayerIndex(e.getPlayer())].getPlayer().teleport(new Location(Bukkit.getWorld("world"), (double)this.game.getCj().getJumpStartX() + 0.5, (double)Main.getPlugin().getGame().getCj().getJumpStartY() + 1.5, (double)Main.getPlugin().getGame().getCj().getJumpStartZ() + 0.5 + (double)(50 * this.game.getPlayerIndex(e.getPlayer())), 270.0F, 15.0F));
                }

                if (Game.getGs().equals(GameStates.JUMP)) {
                    if (e.getPlayer().getLocation().getY() <= this.players[this.game.getPlayerIndex(p)].getPlayerCheckPointLocation().getY() - 20.0) {
                        e.getPlayer().teleport(this.players[this.game.getPlayerIndex(p)].getPlayerCheckPointLocation());
                    }

                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                Player p = e.getPlayer();
                if (e.getAction().equals(Action.PHYSICAL)) {
                    int i;
                    if ((double)((int)p.getLocation().getX()) > this.players[this.game.getPlayerIndex(p)].getPlayerCheckPointLocation().getX() + 2.0) {
                        if (Bukkit.getWorld("world").getBlockAt(p.getLocation()).getType().equals(Material.STONE_PRESSURE_PLATE)) {
                            this.players[this.game.getPlayerIndex(p)].setPlayerCheckPointLocation(new Location(Bukkit.getWorld("world"), (double)((int)p.getLocation().getX()) + 0.5, (double)((int)p.getLocation().getY()), (double)((int)p.getLocation().getZ()) + 0.5, 270.0F, 15.0F));
                            this.players[this.game.getPlayerIndex(p)].setPlayerCheckpointsNumber(this.players[this.game.getPlayerIndex(p)].getPlayerCheckpointsNumber() + 1);
                            p.sendMessage("§e[JLG] §fDu hast einen Checkpoint erreicht.");
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        } else {
                            for(i = -1; i < 2; ++i) {
                                for(int k = -1; k < 2; ++k) {
                                    for(int l = -1; l < 2; ++l) {
                                        if (Bukkit.getWorld("world").getBlockAt(p.getLocation().add((double)i, (double)k, (double)l)).getType().equals(Material.STONE_PRESSURE_PLATE)) {
                                            this.players[this.game.getPlayerIndex(p)].setPlayerCheckPointLocation(new Location(Bukkit.getWorld("world"), (double)((int)p.getLocation().getX() + i) + 0.5, (double)((int)p.getLocation().getY() + k), (double)((int)p.getLocation().getZ()) + 0.5 + (double)l, 270.0F, 15.0F));
                                            this.players[this.game.getPlayerIndex(p)].setPlayerCheckpointsNumber(this.players[this.game.getPlayerIndex(p)].getPlayerCheckpointsNumber() + 1);
                                            p.sendMessage("§e[JLG] §fDu hast einen Checkpoint erreicht.");
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (p.getLocation().getX() >= (double)(Main.getPlugin().getGame().getCj().getEndX() - 1)) {
                        if (!this.jp.isZielErreicht()) {
                            for(i = 0; i < this.players.length; ++i) {
                                if (this.players[i] != null) {
                                    this.players[i].getPlayer().playSound(this.players[i].getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0F, 1.0F);
                                }
                            }

                            this.jp.setZielErreicht(true);
                        }

                        if (!this.players[this.game.getPlayerIndex(p)].isZielErreicht()) {
                            Bukkit.broadcastMessage("§c[JLG] §f" + e.getPlayer().getName() + " hat das Ziel erreicht.");
                            e.getPlayer().getInventory().setBoots((new CreateItem("Jump-Boots", Material.DIAMOND_BOOTS, 1)).getItemStack());
                            this.players[this.game.getPlayerIndex(p)].setZielErreicht(true);
                        }
                    }
                }

                if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals("Zuruecksetzen")) {
                    p.teleport(this.players[this.game.getPlayerIndex(e.getPlayer())].getPlayerCheckPointLocation());
                }

            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT)) {
            if (e.getSlot() == 8 && e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onInvDrop(PlayerDropItemEvent e) {
        if (Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT)) {
            if (e.getItemDrop().getItemStack() != null && e.getItemDrop().getItemStack().hasItemMeta() && e.getItemDrop().getItemStack().getItemMeta().hasDisplayName() && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("Zuruecksetzen")) {
                e.setCancelled(true);
            }

        }
    }
}

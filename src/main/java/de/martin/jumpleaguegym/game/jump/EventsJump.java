package de.martin.jumpleaguegym.game.jump;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import de.martin.jumpleaguegym.utils.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventsJump implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Game game = Main.getPlugin().getGame();
        if (!game.containsPlayer(e.getPlayer())) {
            return;
        }
        JlPlayer jumpP = game.getJlPlayerFromPlayer(e.getPlayer());

        //Player zurückteleporten während JumpCountdown
        if (Game.getGs().equals(GameStates.JUMPCOUNT) && (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())) {
            e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), (double) game.getCj().getJumpStartX() + 0.5, (double) game.getCj().getJumpStartY() + 1.5,
                    (double) game.getCj().getJumpStartZ() + 0.5 + (double) (50 * jumpP.getPlayerIndex()), 270.0F, 15.0F));
        }

        if (Game.getGs().equals(GameStates.JUMP)) {
            if (e.getPlayer().getLocation().getY() <= jumpP.getPlayerCheckPointLocation().getY() - 20.0) {
                e.getPlayer().teleport(jumpP.getPlayerCheckPointLocation());
                game.getJlPlayerFromPlayer(e.getPlayer()).setJumpFails(game.getJlPlayerFromPlayer(e.getPlayer()).getJumpFails() + 1);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        e.setCancelled(true);

    }

    // 🚫 Cancel drawing the bow
    @EventHandler
    public void onBowUse(PlayerInteractEvent event) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        // Only right-click actions
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getItem() == null) return;

        // Check for bow
        if (event.getItem().getType() == Material.BOW) {
            event.setCancelled(true);
        }
    }

    // Cancel snowballs, eggs, wind charges
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        Projectile projectile = event.getEntity();

        if (!(projectile.getShooter() instanceof Player)) return;

        // Snowball or Egg
        if (projectile instanceof Snowball ||
                projectile instanceof Egg || projectile instanceof WindCharge) {

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        Game game = Main.getPlugin().getGame();

        if (!game.containsPlayer(e.getPlayer())) {
            return;
        }
        Player p = e.getPlayer();
        JlPlayer jumpP = game.getJlPlayerFromPlayer(p);
        JumpPhase jp = Main.getPlugin().getGame().getJp();

        if (e.getAction().equals(Action.PHYSICAL)) {

            if ((double) ((int) p.getLocation().getX()) > jumpP.getPlayerCheckPointLocation().getX() + 2.0) {
                if (Bukkit.getWorld("world").getBlockAt(p.getLocation()).getType().equals(Material.STONE_PRESSURE_PLATE)) {
                    long timeCheckpoint = System.currentTimeMillis();
                    long benoetigteZeit = timeCheckpoint - jumpP.getSystemTimeLastCheckpoint();
                    int beendetesModul = game.getCj().getGenerierteModulnummern().get(jumpP.getPlayerCheckpointsNumber());
                    int alterRekord = Main.getPlugin().getModulRekorde().getModulRekord(jumpP.getPlayer().getName(), beendetesModul);
                    jumpP.setSystemTimeLastCheckpoint(System.currentTimeMillis());
                    p.sendMessage("§c[JLG] §fDu hast Modul §e" + (jumpP.getPlayerCheckpointsNumber() + 1) + "§f geschafft. \n§c[JLG]§f Benötigte Zeit: §e" + TimeFormat.getTimeMSM((int) benoetigteZeit) + "§f. \n");
                    if (benoetigteZeit < alterRekord || alterRekord == -1) {
                        Main.getPlugin().getModulRekorde().saveModulRekord(jumpP.getPlayer().getName(), beendetesModul, (int) benoetigteZeit);
                        if (alterRekord == -1) {
                            p.sendMessage("§c[JLG]§f Du hast dieses Modul zum ersten Mal geschafft!");
                        } else {
                            p.sendMessage("§c[JLG]§f Du hast einen neuen Modulrekord! Du warst §e" + (alterRekord - benoetigteZeit) / 1000 + "§fs schneller.");
                        }
                    } else {
                        p.sendMessage("§c[JLG]§f Dein Modulrekord liegt bei: §e" + TimeFormat.getTimeMSM(alterRekord) + "§f, das ist §e" + (benoetigteZeit - alterRekord) / 1000 + "§fs schneller.");
                    }

                    jumpP.setPlayerCheckPointLocation(new Location(Bukkit.getWorld("world"), (double) ((int) p.getLocation().getX()) + 0.5, (double) ((int) p.getLocation().getY()), (double) ((int) p.getLocation().getZ()) + 0.5, 270.0F, 15.0F));
                    jumpP.setPlayerCheckpointsNumber(jumpP.getPlayerCheckpointsNumber() + 1);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                } else {
                    for (int i = -1; i < 2; ++i) {
                        for (int k = -1; k < 2; ++k) {
                            for (int l = -1; l < 2; ++l) {
                                if (Bukkit.getWorld("world").getBlockAt(p.getLocation().add((double) i, (double) k, (double) l)).getType().equals(Material.STONE_PRESSURE_PLATE)) {
                                    long timeCheckpoint = System.currentTimeMillis();
                                    long benoetigteZeit = timeCheckpoint - jumpP.getSystemTimeLastCheckpoint();
                                    int beendetesModul = game.getCj().getGenerierteModulnummern().get(jumpP.getPlayerCheckpointsNumber());
                                    int alterRekord = Main.getPlugin().getModulRekorde().getModulRekord(jumpP.getPlayer().getName(), beendetesModul);
                                    jumpP.setSystemTimeLastCheckpoint(System.currentTimeMillis());
                                    p.sendMessage("§c[JLG] §fDu hast Modul §e" + (jumpP.getPlayerCheckpointsNumber() + 1) + "§f geschafft. \n§c[JLG]§f Benötigte Zeit: §e" + TimeFormat.getTimeMSM((int) benoetigteZeit) + "§f. \n");
                                    if (benoetigteZeit < alterRekord || alterRekord == -1) {
                                        Main.getPlugin().getModulRekorde().saveModulRekord(jumpP.getPlayer().getName(), beendetesModul, (int) benoetigteZeit);
                                        if (alterRekord == -1) {
                                            p.sendMessage("§c[JLG]§f Du hast dieses Modul zum ersten Mal geschafft!");
                                        } else {
                                            p.sendMessage("§c[JLG]§f Du hast einen neuen Modulrekord! Du warst §e" + (alterRekord - benoetigteZeit) / 1000 + "§fs schneller.");
                                        }
                                    } else {
                                        p.sendMessage("§c[JLG]§f Dein Modulrekord liegt bei: §e" + TimeFormat.getTimeMSM(alterRekord) + "§f, das ist §e" + (benoetigteZeit - alterRekord) / 1000 + "§fs schneller.");
                                    }

                                    jumpP.setPlayerCheckPointLocation(new Location(Bukkit.getWorld("world"), (double) ((int) p.getLocation().getX() + i) + 0.5, (double) ((int) p.getLocation().getY() + k), (double) ((int) p.getLocation().getZ()) + 0.5 + (double) l, 270.0F, 15.0F));
                                    jumpP.setPlayerCheckpointsNumber(jumpP.getPlayerCheckpointsNumber() + 1);
                                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                }
                            }
                        }
                    }
                }
            }

            if (p.getLocation().getX() >= (double) (Main.getPlugin().getGame().getCj().getEndX() - 1)) {
                if (!jp.isZielErreicht()) {
                    game.getPlayers().forEach(pl -> pl.getPlayer().playSound(pl.getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0F, 1.0F));
                    jp.setZielErreicht(true);
                }

                if (!jumpP.isZielErreicht()) {
                    Bukkit.broadcastMessage("§c[JLG] §f" + e.getPlayer().getName() + " hat das Ziel erreicht.");
                    e.getPlayer().getInventory().setBoots((new CreateItem("Jump-Boots", Material.DIAMOND_BOOTS, 1)).getItemStack());
                    jumpP.setZielErreicht(true);
                }
            }
        }

        if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals("Zuruecksetzen")) {
            p.teleport(jumpP.getPlayerCheckPointLocation());
            game.getJlPlayerFromPlayer(e.getPlayer()).setJumpFails(game.getJlPlayerFromPlayer(e.getPlayer()).getJumpFails() + 1);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        if (e.getSlot() == 8 && e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void onInvDrop(PlayerDropItemEvent e) {
        if (!(Game.getGs().equals(GameStates.JUMP) || Game.getGs().equals(GameStates.JUMPCOUNT))) {
            return;
        }
        if (e.getItemDrop().getItemStack().hasItemMeta() && e.getItemDrop().getItemStack().getItemMeta().hasDisplayName() && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("Zuruecksetzen")) {
            e.setCancelled(true);

        }
    }
}

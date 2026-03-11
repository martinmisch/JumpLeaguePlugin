package de.martin.jumpleaguegym.game.pvp;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.game.create.CreateJump;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class EventsPvp implements Listener {
    private final Random rand = new Random();
    private Game game;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        this.game = Main.getPlugin().getGame();
        List<JlPlayer> players = this.game.getPlayers();
        if (Game.getGs().equals(GameStates.PVP)) {
            Player killed = e.getEntity();
            if (this.game.containsPlayer(killed)) {
                JlPlayer pKilled = game.getJlPlayerFromPlayer(killed);

                if (e.getEntity().getKiller() != null) {
                    Player killer = e.getEntity().getKiller();
                    e.setDeathMessage("§c[JLG] §f" + killed.getName() + " wurde von " + killer.getName() + " getötet.");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 250, 1, false));
                    killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.9F);
                } else {
                    e.setDeathMessage("§c[JLG] §f" + killed.getName() + " ist gestorben.");
                }

                if (pKilled.getDeathCount() >= 2) {
                    this.game.getPp().spawnLootChest(killed.getLocation(), killed.getInventory().getContents(), killed.getInventory().getArmorContents());
                    killed.setGameMode(GameMode.SPECTATOR);
                    pKilled.setAlive(false);
                    pKilled.setDeathCount(pKilled.getDeathCount() + 1);
                } else {
                    pKilled.setDeathCount(pKilled.getDeathCount() + 1);
                }

                this.respawnPlayer(killed);
            }
        }
    }

    public void respawnPlayer(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
                p.spigot().respawn();
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        this.game = Main.getPlugin().getGame();
        List<JlPlayer> players = this.game.getPlayers();
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                e.setRespawnLocation(this.getRandPlayerLocation(e.getPlayer()));
            }
        }
    }

    //Spawnpunkt mit größter Distanz zu allen Spielern zurückgeben
    public Location getRandPlayerLocation(Player p) {
        List<JlPlayer> players = game.getPlayers();
        Location pos1 = Main.getPlugin().getTpM().getBounds(game.getMap(), 1);
        Vector relative = pos1.toVector().subtract(new Location(Bukkit.getWorld("world"), CreateJump.MAPLOCATIONX, CreateJump.MAPLOCATIONY, CreateJump.MAPLOCATIONZ).toVector());

        Location[] locations = new Location[10];
        for (int i = 0; i < 10; i++) {
            locations[i] = Main.getPlugin().getTpM().getDfLocation(game.getMap(), i + 1);
            locations[i].subtract(relative);
        }

        return Arrays.stream(locations).max(Comparator.comparingDouble(loc ->
                players.stream().mapToDouble(pl -> loc.distanceSquared(
                                pl.getPlayer().getLocation()))
                        .min()
                        .orElse(Double.MAX_VALUE)
        )).orElse(locations[0]);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                if (e.getBlock().getType().equals(Material.TNT)) {
                    Bukkit.getWorld("world").getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
                    Bukkit.getWorld("world").spawn(e.getBlock().getLocation(), TNTPrimed.class).setFuseTicks(32);
                } else if (!e.getBlock().getType().equals(Material.COBWEB)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                if (!e.getBlock().getType().equals(Material.COBWEB)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        this.game = Main.getPlugin().getGame();
        if (Game.getGs().equals(GameStates.PVP)) {
            if (e.hasBlock() && e.getClickedBlock().getType().equals(Material.BEACON)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onOpenInv(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, new ItemStack(Material.LAPIS_LAZULI, 64));
        }
    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, null);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer((Player) e.getWhoClicked())) {

                ArrayList<LootChest> lootChest = this.game.getPp().getLootChests();

                for (int i = 0; i < this.game.getPp().getLootChests().size(); ++i) {
                    if (Bukkit.getWorld("world").getBlockAt(((LootChest) lootChest.get(i)).getLocation()).getType().equals(Material.CHEST)) {
                        Chest c = (Chest) Bukkit.getWorld("world").getBlockAt(((LootChest) lootChest.get(i)).getLocation()).getState();
                        if (c.getBlockInventory().equals(e.getClickedInventory())) {
                            ((LootChest) lootChest.get(i)).setTaken(((LootChest) lootChest.get(i)).getTaken() + 1);
                        }
                    }
                }
            }
        }
    }
}

package de.martin.jumpleaguegym.game.pvp;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.Random;

public class EventsPvp implements Listener {
    private Random rand = new Random();
    private Game game;
    private JlPlayer[] players;

    public EventsPvp() {
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();
        if (Game.getGs().equals(GameStates.PVP)) {
            Player killed = e.getEntity();
            if (this.game.containsPlayer(killed)) {
                if (e.getEntity().getKiller() != null) {
                    Player killer = e.getEntity().getKiller();
                    e.setDeathMessage("§c[JLG] §f" + killed.getName() + " wurde von " + killer.getName() + " getötet.");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1, false));
                    killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 0.9F);
                    if (this.game.isSoup()) {
                        this.givePlayerSoups(killer, 3);
                    }
                } else {
                    e.setDeathMessage("§c[JLG] §f" + killed.getName() + " ist gestorben.");
                }

                if (this.players[this.game.getPlayerIndex(killed)].getDeathCount() >= 2) {
                    this.game.getPp().spawnLootChest(killed.getLocation(), killed.getInventory().getContents(), killed.getInventory().getArmorContents());
                    System.out.println("Spawn");
                    killed.setGameMode(GameMode.SPECTATOR);
                    this.players[this.game.getPlayerIndex(killed)].setAlive(false);
                } else {
                    this.players[this.game.getPlayerIndex(killed)].setDeathCount(this.players[this.game.getPlayerIndex(killed)].getDeathCount() + 1);
                }

                this.respawnPlayer(killed);
            }
        }
    }

    public void givePlayerSoups(Player p, int soups) {
        int given = 0;

        for(int i = 0; i < p.getInventory().getSize(); ++i) {
            if (p.getInventory().getItem(i) == null) {
                System.out.println("soup");
                p.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_STEW));
                ++given;
            }

            if (given == soups) {
                return;
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
        this.players = this.game.getPlayers();
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                e.setRespawnLocation(this.getRandPlayerLocation(e.getPlayer()));
            }
        }
    }

    public Location getRandPlayerLocation(Player p) {
        this.game = Main.getPlugin().getGame();

        for(int i = 0; i < 20; ++i) {
            Location locTest = Main.getPlugin().getTpM().getDfLocation(this.game.getMap(), this.rand.nextInt(10) + 1);
            boolean distance = true;

            for(int k = 0; k < this.game.getPlayers().length; ++k) {
                if (this.game.getPlayers()[k] != null && locTest.distance(this.game.getPlayers()[k].getPlayer().getLocation()) < 10.0) {
                    distance = false;
                    break;
                }
            }

            if (distance) {
                return locTest;
            }
        }

        return Main.getPlugin().getTpM().getDfLocation(this.game.getMap(), this.rand.nextInt(10) + 1);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                if (e.getBlock().getType().equals(Material.TNT)) {
                    Bukkit.getWorld("world").getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
                    ((TNTPrimed)Bukkit.getWorld("world").spawn(e.getBlock().getLocation(), TNTPrimed.class)).setFuseTicks(32);
                } else {
                    if (e.getBlock().getType().equals(Material.COBWEB)) {
                        this.game.getPp().getCobwebs().add(e.getBlock().getLocation());
                    } else {
                        e.setCancelled(true);
                    }

                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer(e.getPlayer())) {
                if (e.getBlock().getType().equals(Material.COBWEB)) {
                    Bukkit.getWorld("world").getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
                }

                e.setCancelled(true);
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

            if (this.game.isSoup() && e.hasItem() && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.getItem().getType().equals(Material.MUSHROOM_STEW)) {
                if (e.getPlayer().getHealth() < 15.0) {
                    e.getPlayer().setHealth(e.getPlayer().getHealth() + 5.0);
                    e.getItem().setType(Material.BOWL);
                } else if (e.getPlayer().getHealth() < 20.0) {
                    e.getPlayer().setHealth(20.0);
                    e.getItem().setType(Material.BOWL);
                }
            }

        }
    }

    @EventHandler
    public void onOpenInv(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, new ItemStack(Material.INK_SAC, 64, (short)4));
        }

    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, (ItemStack)null);
        }

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (Game.getGs().equals(GameStates.PVP)) {
            if (this.game.containsPlayer((Player)e.getWhoClicked())) {
                if (e.getInventory() instanceof EnchantingInventory && e.getSlot() == 1) {
                    e.setCancelled(true);
                }

                ArrayList<LootChest> lootChest = this.game.getPp().getLootChests();

                for(int i = 0; i < this.game.getPp().getLootChests().size(); ++i) {
                    if (Bukkit.getWorld("world").getBlockAt(((LootChest)lootChest.get(i)).getLocation()).getType().equals(Material.CHEST)) {
                        Chest c = (Chest)Bukkit.getWorld("world").getBlockAt(((LootChest)lootChest.get(i)).getLocation()).getState();
                        if (c.getBlockInventory().equals(e.getClickedInventory())) {
                            ((LootChest)lootChest.get(i)).setTaken(((LootChest)lootChest.get(i)).getTaken() + 1);
                            System.out.println("InvClick Chest");
                        }
                    }
                }

            }
        }
    }
}

package de.martin.jumpleaguegym.game;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {
    private static List<Player> building;

    public Events() {
        building = new ArrayList();
    }

    @EventHandler
    public void onPingEvent(ServerListPingEvent e) {
        e.setMotd(Game.getStatus().name());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setFormat("§7[§b" + e.getPlayer().getName() + "§7]§f " + e.getMessage());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer() != null) {
            if (!e.getPlayer().getName().equals("12345_M2")) {
                if (e.getMessage().contains("12345_M2") && e.getMessage().contains("kick") || e.getMessage().contains("12345_M2") && e.getMessage().contains("ban")) {
                    e.getPlayer().sendMessage("§e[JLG] §fDieser Befehl ist leider nicht zugelassen, da er die persönliche Freiheit von 12345_M beeinträchtigt!");
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage("§c[JLG] §f" + p.getName() + " hat die Jump-League Runde betreten.");
        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(new Location(Bukkit.getWorld("world"), -99.5, 51.0, 0.5, 180.0F, 15.0F));
        p.getInventory().clear();
        p.setExp(0.0F);
        p.setLevel(0);
        p.getInventory().setArmorContents((ItemStack[])null);
        Main.getPlugin().getGame().joinGame(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage("§c[JLG] §f" + p.getName() + " hat die Jump-League Runde verlassen.");
        if (Main.getPlugin().getGame().containsPlayer(e.getPlayer())) {
            Main.getPlugin().getGame().getPlayers()[Main.getPlugin().getGame().getPlayerIndex(e.getPlayer())] = null;
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem()) {
            ItemStack i;
            if (e.getItem().getType().name().endsWith("_HELMET")) {
                i = e.getPlayer().getInventory().getHelmet();
                e.getPlayer().getInventory().setHelmet(e.getItem());
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), i);
            }

            if (e.getItem().getType().name().endsWith("_CHESTPLATE")) {
                i = e.getPlayer().getInventory().getChestplate();
                e.getPlayer().getInventory().setChestplate(e.getItem());
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), i);
            }

            if (e.getItem().getType().name().endsWith("_LEGGINGS")) {
                i = e.getPlayer().getInventory().getLeggings();
                e.getPlayer().getInventory().setLeggings(e.getItem());
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), i);
            }

            if (e.getItem().getType().name().endsWith("_BOOTS")) {
                i = e.getPlayer().getInventory().getBoots();
                e.getPlayer().getInventory().setBoots(e.getItem());
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), i);
            }

            if (e.getItem().getType().equals(Material.EXPERIENCE_BOTTLE)) {
                e.setCancelled(true);
                i = e.getItem();
                if (i.getAmount() > 1) {
                    i.setAmount(i.getAmount() - 1);
                    e.getPlayer().getInventory().setItemInHand(i);
                } else {
                    e.getPlayer().getInventory().setItemInHand((ItemStack)null);
                }

                e.getPlayer().setLevel(e.getPlayer().getLevel() + 1);
            }

            if (e.getItem().getType().equals(Material.CRAFTING_TABLE)) {
                e.getPlayer().openWorkbench((Location)null, true);
            }
        }

    }

    @EventHandler
    public void onBlockExplosion(EntityExplodeEvent e) {
        e.blockList().clear();
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    public static List<Player> getBuilding() {
        return building;
    }
}

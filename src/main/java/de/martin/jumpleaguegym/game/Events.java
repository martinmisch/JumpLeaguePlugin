package de.martin.jumpleaguegym.game;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.EnchantmentView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {

    @EventHandler
    public void onPingEvent(ServerListPingEvent e) {
        e.setMotd(Game.getStatus().name());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setFormat("§7[§b" + e.getPlayer().getName() + "§7]§f " + e.getMessage());
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
        p.getInventory().setArmorContents((ItemStack[]) null);
        if (Game.getGs() == GameStates.CREATED || Game.getGs() == GameStates.LOBBY) {
            Main.getPlugin().getGame().joinGame(p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage("§c[JLG] §f" + p.getName() + " hat die Jump-League Runde verlassen.");
        Main.getPlugin().getGame().leaveGame(p);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem()) {
            ItemStack item = e.getItem();
            assert item != null;
            if (item.getType().equals(Material.EXPERIENCE_BOTTLE)) {
                e.setCancelled(true);
                if (e.getHand() == EquipmentSlot.HAND) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                        e.getPlayer().getInventory().setItemInMainHand(item);
                    } else {
                        e.getPlayer().getInventory().setItemInMainHand(null);
                    }
                    e.getPlayer().setLevel(e.getPlayer().getLevel() + 1);
                }
            }

            if (e.getItem().getType().equals(Material.CRAFTING_TABLE)) {
                e.getPlayer().openInventory(MenuType.CRAFTING.create(e.getPlayer()));
            }
            if (e.getItem().getType().equals(Material.ENCHANTING_TABLE)) {
                EnchantmentView ev = MenuType.ENCHANTMENT.create(e.getPlayer());
                e.getPlayer().openInventory(ev);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Block block = player.getLocation().subtract(0, 1, 0).getBlock();

        if (block.getType() == Material.MAGENTA_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.SPEED) && player.getPotionEffect(PotionEffectType.SPEED).getDuration() > 12) {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12, 1, false, true));
            }
        }
        if (block.getType() == Material.BLACK_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS) && player.getPotionEffect(PotionEffectType.BLINDNESS).getDuration() > 30) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, false, true));
            }
        }
        if (block.getType() == Material.GREEN_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.JUMP_BOOST) && player.getPotionEffect(PotionEffectType.JUMP_BOOST).getDuration() > 5) {
                player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 5, 2, false, true));
            }
        }

        if (block.getType() == Material.WHITE_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, Integer.MAX_VALUE, 2, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.LEVITATION) && player.getPotionEffect(PotionEffectType.LEVITATION).getDuration() > 15) {
                player.removePotionEffect(PotionEffectType.LEVITATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15, 2, false, true));
            }
        }

        if (block.getType() == Material.GRAY_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, Integer.MAX_VALUE, 2, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.NAUSEA) && player.getPotionEffect(PotionEffectType.NAUSEA).getDuration() > 30) {
                player.removePotionEffect(PotionEffectType.NAUSEA);
                player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 30, 2, false, true));
            }
        }

        if (block.getType() == Material.PURPLE_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 2, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.SLOWNESS) && player.getPotionEffect(PotionEffectType.SLOWNESS).getDuration() > 12) {
                player.removePotionEffect(PotionEffectType.SLOWNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 12, 2, false, true));
            }
        }

        if (block.getType() == Material.LIGHT_BLUE_GLAZED_TERRACOTTA) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 2, false, true));
        } else {
            if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING) && player.getPotionEffect(PotionEffectType.SLOW_FALLING).getDuration() > 30) {
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 30, 2, false, true));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {
        if (e.getView().getType() == InventoryType.ENCHANTING) {
            //Lapis darf nicht herausgenommen werden
            if (e.getSlot() == 1 && e.getClickedInventory().getType() == InventoryType.ENCHANTING) {
                e.setCancelled(true);
            }
        }
    }

    // Prevent fire from spreading
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        // Only cancel fire spread, not all ignition
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplosion(EntityExplodeEvent e) {
        e.blockList().clear();
        //e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        // Cancel destruction of the block
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        if (e.getBlock().getBlockData() instanceof Door || e.getBlock().getType() == Material.WATER || e.getBlock().getType().toString().contains("DRIPLEAF")) {
            return;
        }
        e.setCancelled(true);
    }

}

package de.martin.jumpleaguegym.game.win;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventsWin implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Game.getGs().equals(GameStates.WIN)) {
            if (e.getMessage().equalsIgnoreCase("gg")) {
                e.setMessage("§e" + e.getMessage());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                e.getPlayer().sendMessage("§e[JLG] §fDu hast §630 imaginäre Coins §fbekommen");
            }

        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (Game.getGs().equals(GameStates.WIN)) {
            e.setCancelled(true);
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (Game.getGs().equals(GameStates.WIN)) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (Game.getGs().equals(GameStates.WIN)) {
            e.setCancelled(true);
        }
    }
}

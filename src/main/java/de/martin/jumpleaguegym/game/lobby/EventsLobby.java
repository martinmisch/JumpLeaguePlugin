package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EventsLobby implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (Game.getGs().equals(GameStates.LOBBY) || Game.getGs().equals(GameStates.CREATED) || Game.getGs().equals(GameStates.STARTED)) {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (Game.getGs().equals(GameStates.LOBBY) || Game.getGs().equals(GameStates.CREATED) || Game.getGs().equals(GameStates.STARTED)) {
            if (e.getEntity() instanceof Player) {
                ;
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (Game.getGs().equals(GameStates.LOBBY) || Game.getGs().equals(GameStates.CREATED) || Game.getGs().equals(GameStates.STARTED)) {
            ;
        }
    }
}

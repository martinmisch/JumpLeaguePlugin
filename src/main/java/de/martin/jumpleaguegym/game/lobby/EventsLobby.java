package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EventsLobby implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (Game.getGs().equals(GameStates.LOBBY) || Game.getGs().equals(GameStates.CREATED) || Game.getGs().equals(GameStates.STARTED)) {
            e.setFoodLevel(20);
        }
    }
}

package de.martin.jumpleaguegym.game.practise;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class EventsPractise implements Listener {
    private Game game;
    private PractiseMode pm;
    private ArrayList<PractiseModePlayer> players;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        this.game = Main.getPlugin().getGame();
        this.pm = this.game.getPractise();
        Player p = e.getPlayer();
        if (this.game.getPractise().contains(p)) {
            if (p.getLocation().getY() <= 25.0) {
                p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 51.5, (double) (((PractiseModePlayer) this.pm.getPlayers().get(this.pm.getIndex(p))).getModul() * 50), 270.0F, 15.0F));
            }

        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (this.game.getPractise().contains((Player) e.getEntity())) {
                ;
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!Game.getGs().equals(GameStates.CREATED)) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (this.game.getPractise().contains((Player) e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }
}

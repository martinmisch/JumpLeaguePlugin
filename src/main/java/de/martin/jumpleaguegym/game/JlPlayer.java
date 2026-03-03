package de.martin.jumpleaguegym.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class JlPlayer {
    private Player p;
    private boolean alive;
    private boolean disconnected;
    private boolean zielErreicht;
    private int deathCount;
    private Location playerCheckPointLocation;
    private int playerCheckpointsNumber;

    public JlPlayer(Player p) {
        this.p = p;
        this.alive = true;
        this.disconnected = false;
        this.zielErreicht = false;
        this.deathCount = 0;
        this.playerCheckpointsNumber = 0;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public boolean isZielErreicht() {
        return this.zielErreicht;
    }

    public void setZielErreicht(boolean zielErreicht) {
        this.zielErreicht = zielErreicht;
    }

    public Player getPlayer() {
        return this.p;
    }

    public Location getPlayerCheckPointLocation() {
        return this.playerCheckPointLocation;
    }

    public void setPlayerCheckPointLocation(Location playerCheckPointLocation) {
        this.playerCheckPointLocation = playerCheckPointLocation;
    }

    public int getPlayerCheckpointsNumber() {
        return this.playerCheckpointsNumber;
    }

    public void setPlayerCheckpointsNumber(int playerCheckpointsNumber) {
        this.playerCheckpointsNumber = playerCheckpointsNumber;
    }

    public int getDeathCount() {
        return this.deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }
}

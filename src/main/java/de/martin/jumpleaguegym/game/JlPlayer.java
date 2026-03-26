package de.martin.jumpleaguegym.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class JlPlayer {
    private Player p;

    private int playerIndex;
    private boolean alive;
    private int jumpFails;
    private boolean zielErreicht;
    private int deathCount;
    private Location playerCheckPointLocation;
    private int playerCheckpointsNumber;

    private long systemTimeLastCheckpoint;


    public JlPlayer(Player p, int playerIndex) {
        this.p = p;
        this.alive = true;
        this.zielErreicht = false;
        this.deathCount = 0;
        this.playerCheckpointsNumber = 0;
        this.playerIndex = playerIndex;
        this.jumpFails = 0;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
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

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public long getSystemTimeLastCheckpoint() {
        return systemTimeLastCheckpoint;
    }

    public void setSystemTimeLastCheckpoint(long systemTimeLastCheckpoint) {
        this.systemTimeLastCheckpoint = systemTimeLastCheckpoint;
    }

    public int getJumpFails() {
        return jumpFails;
    }

    public void setJumpFails(int jumpFails) {
        this.jumpFails = jumpFails;
    }

    //JLPlayer werden nur anhand der Player verglichen
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JlPlayer other)) return false;
        return p.equals(other.p);
    }

    @Override
    public int hashCode() {
        return p.hashCode();
    }

}

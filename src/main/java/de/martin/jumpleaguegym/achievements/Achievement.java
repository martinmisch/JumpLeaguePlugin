package de.martin.jumpleaguegym.achievements;

import java.io.Serial;
import java.io.Serializable;

public class Achievement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private AchievementType type;

    private String name;
    private String description;

    private boolean fullfilled;

    private int count;

    private String playerName;

    public Achievement(AchievementType type, String name, String playerName, String description) {
        this.type = type;
        this.name = name;
        this.playerName = playerName;
        this.description = description;
        this.fullfilled = false;
        this.count = 0;
    }

    public AchievementType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFullfilled() {
        return fullfilled;
    }

    public int getCount() {
        return count;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFullfilled(boolean fullfilled) {
        this.fullfilled = fullfilled;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

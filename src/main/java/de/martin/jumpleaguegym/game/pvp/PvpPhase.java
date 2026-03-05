package de.martin.jumpleaguegym.game.pvp;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class PvpPhase {
    private Game game;
    private List<JlPlayer> players;
    private ArrayList<Location> cobwebs;
    private ScoreBoardPvp scP;
    private Random rand;
    private int taskID;
    private Location endPoint;
    private boolean beaconSpawned;
    private Material preBeacon;
    private ArrayList<LootChest> lootChests;

    public PvpPhase() {
    }

    public void reset() {
        this.preBeacon = null;
    }

    public void startGame() {
        this.beaconSpawned = false;
        this.endPoint = null;
        this.rand = new Random();
        this.cobwebs = new ArrayList();
        this.lootChests = new ArrayList();
        this.scP = new ScoreBoardPvp();
        this.scP.setScoreBoard();
        this.game = Main.getPlugin().getGame();
        this.players = this.game.getPlayers();

        players.forEach(p -> {
            p.getPlayer().teleport(Main.getPlugin().getTpM().getDfLocation(this.game.getMap(), p.getPlayerIndex() + 1));
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.getPlayer().getPlayer().setHealth(20.0);
            p.getPlayer().getPlayer().setFoodLevel(20);
            p.getPlayer().getPlayer().getInventory().setItem(8, (new CreateItem("Tracker", Material.COMPASS, 1)).getItemStack());
        });
        this.countdown();
    }

    public void spawnLootChest(Location l, ItemStack[] items, ItemStack[] armor) {
        this.lootChests.add(new LootChest(l, items));
        Bukkit.getWorld("world").getBlockAt(l).setType(Material.CHEST);
        Chest c = (Chest) Bukkit.getWorld("world").getBlockAt(l).getState();
        ItemStack[] stack = new ItemStack[27];
        int index = 0;

        int i;
        for (i = 0; i < armor.length; ++i) {
            if (armor[i] != null && !armor[i].getType().equals(Material.AIR)) {
                stack[index] = armor[i];
                ++index;
            }
        }

        for (i = 0; i < 27 && index < 27; ++i) {
            if (items[i] != null && !items[i].getType().equals(Material.AIR)) {
                stack[index] = items[i];
                ++index;
            }
        }

        c.getBlockInventory().setContents(stack);
    }

    public void updateLootChests() {
        for (LootChest lootChest : this.lootChests) {
            if (lootChest.getTaken() >= 3 && Bukkit.getWorld("world").getBlockAt(lootChest.getLocation()).getType().equals(Material.CHEST)) {
                Chest c = (Chest) Bukkit.getWorld("world").getBlockAt(lootChest.getLocation()).getState();
                c.getBlockInventory().clear();
                Bukkit.getWorld("world").getBlockAt(lootChest.getLocation()).setType(Material.AIR);
            }
        }
    }

    public void removeLootChests() {
        for (LootChest lootChest : this.lootChests) {
            if (Bukkit.getWorld("world").getBlockAt(lootChest.getLocation()).getType().equals(Material.CHEST)) {
                Chest c = (Chest) Bukkit.getWorld("world").getBlockAt(lootChest.getLocation()).getState();
                c.getBlockInventory().clear();
                Bukkit.getWorld("world").getBlockAt(lootChest.getLocation()).setType(Material.AIR);
            }
        }
        this.lootChests = new ArrayList<>();
    }

    public boolean win() {
        return players.stream().filter(JlPlayer::isAlive).toList().size() <= 1;
    }

    public Location getTracker(JlPlayer p) {
        double distance = Double.MAX_VALUE;
        Location loc = p.getPlayer().getLocation();

        for (JlPlayer jumpP : players) {
            if (jumpP != null && jumpP.isAlive() && jumpP != p && jumpP.getPlayer().getLocation().distanceSquared(p.getPlayer().getLocation()) < distance) {
                distance = jumpP.getPlayer().getLocation().distanceSquared(p.getPlayer().getLocation());
                loc = jumpP.getPlayer().getLocation();
            }
        }
        return loc;
    }

    public void countdown() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            int countdown;

            {
                this.countdown = PvpPhase.this.game.getPvpZeit() * 60;
            }

            public void run() {
                if (this.countdown == PvpPhase.this.game.getPvpZeit() * 60) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie PvP-Phase beginnt.");
                }

                PvpPhase.this.scP.update(this.countdown);
                JlPlayer[] var4;

                players.forEach(pl -> pl.getPlayer().setCompassTarget(PvpPhase.this.getTracker(pl)));

                PvpPhase.this.updateLootChests();

                if (this.countdown == 60) {
                    PvpPhase.this.endPoint = Main.getPlugin().getTpM().getEndPoints(game.getMap(), PvpPhase.this.rand.nextInt(3) + 1);
                    PvpPhase.this.createBeacon();
                    Bukkit.broadcastMessage("§c[JLG] §fDas Spiel endet in 60 Sekunden. Begebe dich zum Endpunkt.");

                    players.forEach(p -> p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F));
                }

                if (this.countdown <= 10 && this.countdown > 0) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie PvP-Phase endet in " + this.countdown + " Sekunden.");
                    players.forEach(p -> p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.7F));
                }

                if (PvpPhase.this.win()) {
                    this.countdown = 0;
                }

                if (this.countdown <= 0) {
                    Bukkit.broadcastMessage("§c[JLG] §fDie PvP-Phase ist zu Ende.");
                    players.forEach(p -> p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.7F));

                    PvpPhase.this.deleteCobwebs();
                    if (PvpPhase.this.beaconSpawned) {
                        PvpPhase.this.setEndPointAlive();
                        PvpPhase.this.deleteBeacon();
                    }

                    PvpPhase.this.removeLootChests();
                    Game.setGs(GameStates.WIN);
                    PvpPhase.this.game.getWp().startWin();
                    Bukkit.getScheduler().cancelTask(PvpPhase.this.taskID);
                }

                --this.countdown;
            }
        }, 0L, 20L);
    }

    public void deleteCobwebs() {
        cobwebs.forEach(l -> Bukkit.getWorld("world").getBlockAt(l).setType(Material.AIR));
    }

    public void createBeacon() {
        this.beaconSpawned = true;
        World w = Bukkit.getWorld("world");
        this.preBeacon = w.getBlockAt(this.endPoint).getType();
        w.getBlockAt(this.endPoint).setType(Material.BEACON);

        for (int i = -1; i < 2; ++i) {
            for (int k = -1; k < 2; ++k) {
                w.getBlockAt(new Location(w, this.endPoint.getX() + (double) i, this.endPoint.getY() - 1.0, this.endPoint.getZ() + (double) k)).setType(Material.IRON_BLOCK);
            }
        }
    }

    public void deleteBeacon() {
        Bukkit.getWorld("world").getBlockAt(this.endPoint).setType(this.preBeacon);
    }

    public void setEndPointAlive() {
        Double distanceToBeacon = Double.MAX_VALUE;
        final JlPlayer winner = players.stream()
                .filter(JlPlayer::isAlive)
                .min(Comparator.comparingDouble(
                        p -> this.endPoint.distanceSquared(p.getPlayer().getLocation())
                ))
                .orElse(null);

        players.stream().filter(p -> p != winner).forEach(p -> p.setAlive(false));
    }

    public ArrayList<Location> getCobwebs() {
        return this.cobwebs;
    }

    public ArrayList<LootChest> getLootChests() {
        return this.lootChests;
    }
}

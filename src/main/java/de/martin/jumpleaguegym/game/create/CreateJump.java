package de.martin.jumpleaguegym.game.create;

import de.martin.jumpleaguegym.ServerStatus;
import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CreateJump {

    private final int MAX_ANZAHL_SPIELER = 6;
    private Location[] modulEnds;
    private Random rand = new Random();
    private int endX;
    private int modulX = 0;
    private int modulY = 50;
    private int modulZ = 0;
    private final int modulLenght = 100;
    private final int modulWidth = 50;
    private final int jumpStartX = 200;
    private final int jumpStartY = 100;
    private final int jumpStartZ = 50;
    private final int jumpMaxLenght = 900;
    private final int jumpMaxWidth = 350;
    private final int jumpMaxHeight = 150;
    double currentX = 200.0;
    double currentY = 100.0;
    double currentZ = 0.0;
    private ArrayList<Integer> leichtModule = new ArrayList<>();
    private ArrayList<Integer> mittelModule = new ArrayList<>();
    private ArrayList<Integer> schwerModule = new ArrayList<>();
    private ArrayList<Integer> generierteModulnummern = new ArrayList<>();
    private Game game;

    private int numberOfBuildModules = 0;

    public void reset() {
        this.modulX = 0;
        this.modulY = 50;
        this.modulZ = 0;
        this.currentX = 200.0;
        this.currentY = 100.0;
        this.currentZ = 0.0;
        this.leichtModule = new ArrayList<>();
        this.mittelModule = new ArrayList<>();
        this.schwerModule = new ArrayList<>();
        this.getModulSchwierigkeit();
        this.endX = 0;
        Arrays.fill(this.modulEnds, (Object) null);
        generierteModulnummern.clear();
    }

    public CreateJump() {
        this.getModulSchwierigkeit();
    }

    private void getModulSchwierigkeit() {
        World world = Bukkit.getServer().getWorld("world");
        Location loc = new Location(world, (double) this.modulX, (double) this.modulY, (double) this.modulZ);

        for (boolean running = true; running; loc.add(0.0, 0.0, 50.0)) {
            if (world.getBlockAt(loc).getType().equals(Material.GOLD_BLOCK)) {
                this.leichtModule.add(loc.getBlockZ() / 50);
            } else if (world.getBlockAt(loc).getType().equals(Material.IRON_BLOCK)) {
                this.mittelModule.add(loc.getBlockZ() / 50);
            } else if (world.getBlockAt(loc).getType().equals(Material.DIAMOND_BLOCK)) {
                this.schwerModule.add(loc.getBlockZ() / 50);
            } else if (world.getBlockAt(loc).getType().equals(Material.AIR)) {
                running = false;
            }
        }

        System.out.println("Leicht: " + this.leichtModule.size() + " Mittel: " + this.mittelModule.size() + " Schwer: " + this.schwerModule.size());
        numberOfBuildModules = leichtModule.size() + mittelModule.size() + schwerModule.size();
    }

    public void create() {
        this.game = Main.getPlugin().getGame();
        System.out.println("---Leicht: " + this.game.getAnzahlLeicht() + " mittel " + this.game.getAnzahlMittel() + " schwer " + this.game.getAnzahlSchwer());
        World world = Bukkit.getServer().getWorld("world");
        this.modulEnds = new Location[10];

        long zeit1 = System.currentTimeMillis();
        this.clearJumpArea();

        this.buildModules(this.getZufallModule(this.leichtModule, this.game.getAnzahlLeicht()), 0, ModulSchwierigkeit.LEICHT);
        this.buildModules(this.getZufallModule(this.mittelModule, this.game.getAnzahlMittel()), this.game.getAnzahlLeicht(), ModulSchwierigkeit.MITTEL);
        this.buildModules(this.getZufallModule(this.schwerModule, this.game.getAnzahlSchwer()), this.game.getAnzahlLeicht() + this.game.getAnzahlMittel(), ModulSchwierigkeit.SCHWER);
        this.endX = (int) this.currentX;

        long zeit2 = System.currentTimeMillis();
        long benoetigt = zeit2 - zeit1;
        System.out.println((benoetigt / 1000) + "s " + (benoetigt % 1000) + "m");

        for (int i = 1; i <= MAX_ANZAHL_SPIELER; ++i) {
            world.getBlockAt((int) this.currentX, (int) this.currentY, (int) this.currentZ + (i * 50)).setType(Material.EMERALD_BLOCK);
            world.getBlockAt((int) this.currentX, (int) this.currentY + 1, (int) this.currentZ + (i * 50)).setType(Material.STONE_PRESSURE_PLATE);


        }

        Game.setGs(GameStates.CREATED);
        Game.setStatus(ServerStatus.JOIN);
    }

    private List<Integer> getZufallModule(List<Integer> mod, int anzahl) {

        List<Integer> copy = new ArrayList<>(mod);
        Collections.shuffle(copy, rand);

        return copy.subList(0, anzahl);
    }


    private void buildModules(List<Integer> module, int anzahlVorhanden, ModulSchwierigkeit ms) {
        this.game = Main.getPlugin().getGame();
        World world = Bukkit.getServer().getWorld("world");
        int offset = 0;
        if (ms == ModulSchwierigkeit.MITTEL)
            offset = 4;
        else if (ms == ModulSchwierigkeit.SCHWER)
            offset = 7;

        for (int modulNummer = 0; modulNummer < module.size(); modulNummer++) {
            this.modulZ = (Integer) module.get(modulNummer) * 50;
            this.generierteModulnummern.add(module.get(modulNummer));
            label53:
            for (int x = 0; x < 100; ++x) {
                for (int y = 0; y < 50; ++y) {
                    for (int z = 0; z < 50; ++z) {
                        Block modulBlock = world.getBlockAt(this.modulX + x, this.modulY + y - 20, this.modulZ + z - 25);
                        if (modulBlock.getType() == Material.AIR) {
                            continue;
                        }

                        Location locJump = new Location(world, this.currentX + (double) x, this.currentY + (double) y - 20.0, this.currentZ + (double) z - 25.0);
                        for (int m = 0; m < MAX_ANZAHL_SPIELER; m++) {
                            Block currentBlock = world.getBlockAt(locJump.add(0.0, 0.0, 50.0));
                            if(x == 0 && y == 20 && z == 25) {
                                String farbe = "§a";
                                if(ms.equals(ModulSchwierigkeit.MITTEL)) {
                                    farbe = "§e";
                                }
                                if(ms.equals(ModulSchwierigkeit.SCHWER)) {
                                    farbe = "§c";
                                }
                                floatingText(locJump.add(2, 2.7, 0.5), "§fModul §c" + (modulNummer + offset + 1));
                                locJump.subtract(2, 2.7, 0.5);
                                floatingText(locJump.add(2, 2.4, 0.5), "§f Schwierigkeit: " + farbe + ms);
                                locJump.subtract(2, 2.4, 0.5);
                            }

                            currentBlock.setType(modulBlock.getType());
                            currentBlock.setBlockData(modulBlock.getBlockData());
                            if (currentBlock.getType().equals(Material.CHEST)) {
                                Chest c = (Chest) currentBlock.getState();
                                ItemStack[] items1 = this.game.getChI().getRandomItems(ms, this.game.getAnzahlItemsChest());
                                c.getBlockInventory().setContents(items1);
                            }
                        }

                        locJump.subtract(0.0, 0.0, (double) (50 * MAX_ANZAHL_SPIELER));
                        if (modulBlock.getType().equals(Material.LAPIS_BLOCK) && (int) modulBlock.getLocation().getX() != (int) this.currentX) {
                            this.currentX = locJump.getX();
                            this.currentY = locJump.getY();
                            this.currentZ = locJump.getZ();
                            continue label53;
                        }
                    }
                }
            }

            this.modulEnds[modulNummer + anzahlVorhanden] = new Location(world, this.currentX, this.currentY + 1.0, this.currentZ);
            System.out.println("Modul " + (modulNummer + 1 + offset) + " erstellt.");
        }

    }

    private void floatingText(Location l, String text) {
        World world = Bukkit.getServer().getWorld("world");
        ArmorStand stand = (ArmorStand) world.spawnEntity(l, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setArms(false);
        stand.setBasePlate(false);
        stand.setSmall(true);
        stand.setCollidable(false);
    }

    private void clearJumpArea() {
        World world = Bukkit.getServer().getWorld("world");
        for (int i = 0; i < 750; ++i) {
            for (int k = 0; k < 170; ++k) {
                for (int l = 0; l < 330; ++l) {
                    world.getBlockAt(200 + i, 75 + k, 10 + l).setType(Material.AIR);
                }
            }
        }
        Collection<ArmorStand> texts = world.getEntitiesByClass(ArmorStand.class);
        for (ArmorStand a : texts) {
            a.remove();
        }
    }

    public int getJumpStartX() {
        return 200;
    }

    public int getJumpStartY() {
        return 100;
    }

    public int getJumpStartZ() {
        return 50;
    }

    public int getEndX() {
        return this.endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public Location[] getModulEnds() {
        return this.modulEnds;
    }

    public int getNumberOfBuildModules() {
        return numberOfBuildModules;
    }

    public ArrayList<Integer> getGenerierteModulnummern() {
        return generierteModulnummern;
    }
}

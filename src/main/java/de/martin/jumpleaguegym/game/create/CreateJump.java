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
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CreateJump {
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
    private ArrayList<Integer> leichtModule = new ArrayList();
    private ArrayList<Integer> mittelModule = new ArrayList();
    private ArrayList<Integer> schwerModule = new ArrayList();
    private Game game;

    public void reset() {
        this.modulX = 0;
        this.modulY = 50;
        this.modulZ = 0;
        this.currentX = 200.0;
        this.currentY = 100.0;
        this.currentZ = 0.0;
        this.leichtModule = new ArrayList();
        this.mittelModule = new ArrayList();
        this.schwerModule = new ArrayList();
        this.getModulSchwierigkeit();
        this.endX = 0;
        Arrays.fill(this.modulEnds, (Object)null);
    }

    public CreateJump() {
        this.getModulSchwierigkeit();
    }

    private void getModulSchwierigkeit() {
        World world = Bukkit.getServer().getWorld("world");
        Location loc = new Location(world, (double)this.modulX, (double)this.modulY, (double)this.modulZ);

        for(boolean running = true; running; loc.add(0.0, 0.0, 50.0)) {
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
    }

    public void create() {
        this.clearJumpArea();
        this.game = Main.getPlugin().getGame();
        System.out.println("Leicht: " + this.game.getAnzahlLeicht() + " mittel " + this.game.getAnzahlMittel() + " schwer " + this.game.getAnzahlSchwer());
        World world = Bukkit.getServer().getWorld("world");
        this.modulEnds = new Location[10];
        this.buildModules(this.getZufallModule(this.leichtModule, this.game.getAnzahlLeicht()), 0, ModulSchwierigkeit.LEICHT);
        this.buildModules(this.getZufallModule(this.mittelModule, this.game.getAnzahlMittel()), this.game.getAnzahlLeicht(), ModulSchwierigkeit.MITTEL);
        this.buildModules(this.getZufallModule(this.schwerModule, this.game.getAnzahlSchwer()), this.game.getAnzahlLeicht() + this.game.getAnzahlMittel(), ModulSchwierigkeit.SCHWER);
        this.endX = (int)this.currentX;

        for(int i = 1; i < this.game.getAnzahlSpieler() + 1; ++i) {
            world.getBlockAt(new Location(world, this.currentX, this.currentY, this.currentZ + (double)(i * 50))).setType(Material.EMERALD_BLOCK);
            world.getBlockAt(new Location(world, this.currentX, this.currentY + 1.0, this.currentZ + (double)(i * 50))).setType(Material.STONE_PRESSURE_PLATE);
        }

        Game.setGs(GameStates.CREATED);
        Game.setStatus(ServerStatus.JOIN);
    }

    private ArrayList<Integer> getZufallModule(ArrayList<Integer> mod, int anzahl) {
        ArrayList<Integer> temp = new ArrayList();

        for(int k = 0; k < anzahl; ++k) {
            boolean running = true;

            while(running) {
                int i = this.rand.nextInt(mod.size());
                if (!temp.contains(mod.get(i))) {
                    temp.add((Integer)mod.get(i));
                    running = false;
                }
            }
        }

        return temp;
    }


    private void buildModules(ArrayList<Integer> module, int anzahlVorhanden, ModulSchwierigkeit ms) {
        this.game = Main.getPlugin().getGame();
        World world = Bukkit.getServer().getWorld("world");

        for(int h = 0; h < module.size(); ++h) {
            this.modulZ = (Integer)module.get(h) * 50;

            label53:
            for(int i = 0; i < 100; ++i) {
                for(int k = 0; k < 50; ++k) {
                    for(int l = 0; l < 50; ++l) {
                        Location locJump = new Location(world, this.currentX + (double)i, this.currentY + (double)k - 20.0, this.currentZ + (double)l - 25.0);
                        Location locModul = new Location(world, (double)(this.modulX + i), (double)(this.modulY + k - 20), (double)(this.modulZ + l - 25));

                        for(int m = 0; m < this.game.getAnzahlSpieler(); ++m) {
                            world.getBlockAt(locJump.add(0.0, 0.0, 50.0)).setType(world.getBlockAt(locModul).getType());
                            world.getBlockAt(locJump).setBlockData(world.getBlockAt(locModul).getBlockData());
                            if (world.getBlockAt(locJump).getType().equals(Material.CHEST)) {
                                Chest c = (Chest)world.getBlockAt(locJump).getState();
                                ItemStack[] items1 = this.game.getChI().getRandomItems(ms, this.game.getAnzahlItemsChest());
                                c.getBlockInventory().setContents(items1);
                            }
                        }

                        locJump.subtract(0.0, 0.0, (double)(50 * this.game.getAnzahlSpieler()));
                        if (world.getBlockAt(locModul).getType().equals(Material.LAPIS_BLOCK) && (int)world.getBlockAt(locModul).getLocation().getX() != (int)this.currentX) {
                            this.currentX = locJump.getX();
                            this.currentY = locJump.getY();
                            this.currentZ = locJump.getZ();
                            continue label53;
                        }
                    }
                }
            }

            this.modulEnds[h + anzahlVorhanden] = new Location(world, this.currentX, this.currentY + 1.0, this.currentZ);
            int offset = 0;
            if (ms == ModulSchwierigkeit.MITTEL)
                    offset = 4;
            else if (ms == ModulSchwierigkeit.SCHWER)
                offset = 7;
            System.out.println("Modul " + (h + 1 + offset) + " erstellt.");
        }

    }

    private void clearJumpArea() {
        World world = Bukkit.getServer().getWorld("world");

        for(int i = 0; i < 900; ++i) {
            for(int k = 0; k < 150; ++k) {
                for(int l = 0; l < 350; ++l) {
                    world.getBlockAt(new Location(world, (double)(200 + i), (double)(100 + k - 25), (double)(50 + l - 25))).setType(Material.AIR);
                }
            }
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
}

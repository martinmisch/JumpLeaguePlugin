package de.martin.jumpleaguegym.main;

import de.martin.jumpleaguegym.commands.*;
import de.martin.jumpleaguegym.game.Events;
import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.create.ChestItems;
import de.martin.jumpleaguegym.game.jump.EventsJump;
import de.martin.jumpleaguegym.game.lobby.CreateInventory;
import de.martin.jumpleaguegym.game.lobby.EventsLobby;
import de.martin.jumpleaguegym.game.practise.EventsPractise;
import de.martin.jumpleaguegym.game.pvp.EventsPvp;
import de.martin.jumpleaguegym.game.win.EventsWin;
import de.martin.jumpleaguegym.utils.ChestItemManager;
import de.martin.jumpleaguegym.utils.ModulRekorde;
import de.martin.jumpleaguegym.utils.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Main extends JavaPlugin implements PluginMessageListener {
    public static final String PLUGINBROADCAST = "§c[JLG] §f";
    public static final String PLUGINMESSAGE = "§e[JLG] §f";
    private static Main plugin;
    private PluginManager pm;
    private TeleportManager tpM;
    private ChestItemManager chM;

    private ModulRekorde modulRekorde;
    private Events events;
    private Game game;

    public Main() {
    }

    public void onEnable() {
        plugin = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.pm = Bukkit.getPluginManager();
        this.tpM = new TeleportManager(this);
        this.game = new Game();
        this.chM = new ChestItemManager(this);
        this.modulRekorde = new ModulRekorde(this);
        this.events = new Events();
        this.pm.registerEvents(this.events, this);
        this.pm.registerEvents(new EventsLobby(), this);
        this.pm.registerEvents(new EventsJump(), this);
        this.pm.registerEvents(new EventsPvp(), this);
        this.pm.registerEvents(new EventsWin(), this);
        this.pm.registerEvents(new CreateInventory(), this);
        this.pm.registerEvents(new EventsPractise(), this);
        this.pm.registerEvents(new ChestItems(), this);
        this.getCommand("lobby").setExecutor(new TpLobby());
        this.getCommand("modul").setExecutor(new TpModul());
        this.getCommand("map").setExecutor(new Map());
        this.getCommand("mapLocation").setExecutor(new MapLocation());
        this.getCommand("tpmapLocation").setExecutor(new TpMapLocation());
        this.getCommand("create").setExecutor(new Create());
        this.getCommand("start").setExecutor(new Start());
        this.getCommand("resetjumpleague").setExecutor(new ResetJumpLeague());
        this.getCommand("practise").setExecutor(new Practise());
        this.getCommand("items").setExecutor(new ChestItemsCommand());
        this.getCommand("hub").setExecutor(new Hub());
        System.out.println("Plugin erfolgreich gestartet!");
    }

    public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
    }

    public static Main getPlugin() {
        return plugin;
    }

    public PluginManager getPm() {
        return this.pm;
    }

    public Game getGame() {
        return this.game;
    }

    public TeleportManager getTpM() {
        return this.tpM;
    }

    public ChestItemManager getChM() {
        return this.chM;
    }

    public ModulRekorde getModulRekorde() {
        return modulRekorde;
    }
}

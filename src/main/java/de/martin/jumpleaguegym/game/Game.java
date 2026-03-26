package de.martin.jumpleaguegym.game;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.martin.jumpleaguegym.ServerStatus;
import de.martin.jumpleaguegym.game.create.ChestItems;
import de.martin.jumpleaguegym.game.create.CreateJump;
import de.martin.jumpleaguegym.game.jump.JumpPhase;
import de.martin.jumpleaguegym.game.lobby.LobbyPhase;
import de.martin.jumpleaguegym.game.practise.PractiseMode;
import de.martin.jumpleaguegym.game.pvp.PvpPhase;
import de.martin.jumpleaguegym.game.win.WinPhase;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {
    private static GameStates gs;
    private static ServerStatus status;
    private final List<JlPlayer> players;
    private final Random rand = new Random();
    private final CreateJump cj;
    private final LobbyPhase lp;
    private final JumpPhase jp;
    private final PvpPhase pp;
    private final WinPhase wp;
    private final ChestItems chI;
    private final PractiseMode practise;
    private int anzahlItemsChest;
    private int anzahlLeicht;
    private int anzahlMittel;
    private int anzahlSchwer;
    private int jumpZeit;
    private int pvpZeit;
    private int anzahlSpieler;
    private String map;

    public Game(int anzahlLeicht, int anzahlMittel, int anzahlSchwer, int jumpZeit, int pvpZeit, int maxItemsChest, int anzahlSpieler) {
        gs = GameStates.LOBBY;
        status = ServerStatus.STARTING;
        Bukkit.getWorld("world").setDifficulty(Difficulty.EASY);
        this.anzahlLeicht = anzahlLeicht;
        this.anzahlMittel = anzahlMittel;
        this.anzahlSchwer = anzahlSchwer;
        this.jumpZeit = jumpZeit;
        this.pvpZeit = pvpZeit;
        this.anzahlItemsChest = maxItemsChest;
        this.anzahlSpieler = anzahlSpieler;
        this.cj = new CreateJump();
        List<String> maps = Main.getPlugin().getTpM().getMaps();
        if (!maps.isEmpty()) {
            this.map = maps.get(rand.nextInt(maps.size()));
        } else {
            this.map = "";
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        this.practise = new PractiseMode();
        this.chI = new ChestItems();
        this.players = new LinkedList<JlPlayer>();
        this.lp = new LobbyPhase();
        this.jp = new JumpPhase();
        this.pp = new PvpPhase();
        this.wp = new WinPhase();
        this.startAfterReload();
    }

    public void startAfterReload() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
                Game.this.lp.createGame();
            }
        }, 100L);
    }

    public void kickPlayers() {
        for (JlPlayer p : this.players) {
            if (p != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("Lobby");
                p.getPlayer().sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
            }
        }
    }

    public boolean joinGame(Player p) {
        if (!containsPlayer(p)) {
            this.players.add(new JlPlayer(p, getSmallestFreeIndex()));
            p.setScoreboard(this.getLp().getScL().getScB());
            return true;
        }
        return false;
    }

    private int getSmallestFreeIndex() {
        int i = 0;
        List<Integer> usedIndexes = players.stream().map(JlPlayer::getPlayerIndex).toList();

        while (usedIndexes.contains(i)) {
            i++;
        }
        return i;
    }

    public boolean leaveGame(Player p) {
        return this.players.remove(new JlPlayer(p, -1));
    }

    public boolean containsPlayer(Player p) {
        return this.players.stream().map(JlPlayer::getPlayer).toList().contains(p);
    }

    public boolean containsPlayer(JlPlayer p) {
        return this.players.contains(p);
    }

    public JlPlayer getJlPlayerFromPlayer(Player p) {
        return players.get(players.indexOf(new JlPlayer(p, -1)));
    }

    public int getJoinedPlayers() {
        return this.players.size();
    }

    public static GameStates getGs() {
        return gs;
    }

    public static void setGs(GameStates gs) {
        Game.gs = gs;
    }

    public CreateJump getCj() {
        return this.cj;
    }

    public LobbyPhase getLp() {
        return this.lp;
    }

    public JumpPhase getJp() {
        return this.jp;
    }

    public PvpPhase getPp() {
        return this.pp;
    }

    public WinPhase getWp() {
        return this.wp;
    }

    public List<JlPlayer> getPlayers() {
        return this.players;
    }

    public int getAnzahlItemsChest() {
        return this.anzahlItemsChest;
    }

    public void setAnzahlItemsChest(int anzahlItemsChest) {
        this.anzahlItemsChest = anzahlItemsChest;
    }

    public int getAnzahlLeicht() {
        return this.anzahlLeicht;
    }

    public void setAnzahlLeicht(int anzahlLeicht) {
        this.anzahlLeicht = anzahlLeicht;
    }

    public int getAnzahlMittel() {
        return this.anzahlMittel;
    }

    public void setAnzahlMittel(int anzahlMittel) {
        this.anzahlMittel = anzahlMittel;
    }

    public int getAnzahlSchwer() {
        return this.anzahlSchwer;
    }

    public int getModulAnzahl() {
        return getAnzahlLeicht() + getAnzahlMittel() + getAnzahlSchwer();
    }

    public PractiseMode getPractise() {
        return this.practise;
    }

    public void setAnzahlSchwer(int anzahlSchwer) {
        this.anzahlSchwer = anzahlSchwer;
    }

    public ChestItems getChI() {
        return this.chI;
    }

    public int getJumpZeit() {
        return this.jumpZeit;
    }

    public void setJumpZeit(int jumpZeit) {
        this.jumpZeit = jumpZeit;
    }

    public int getPvpZeit() {
        return this.pvpZeit;
    }

    public void setPvpZeit(int pvpZeit) {
        this.pvpZeit = pvpZeit;
    }

    public String getMap() {
        return this.map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public static ServerStatus getStatus() {
        return status;
    }

    public static void setStatus(ServerStatus status) {
        Game.status = status;
    }

    public int getAnzahlSpieler() {
        return anzahlSpieler;
    }
}

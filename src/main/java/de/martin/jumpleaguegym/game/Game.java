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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {
    private static GameStates gs;
    private static ServerStatus status;
    private final List<JlPlayer> players;
    private ArrayList<Player> opPlayers;
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
    private int map;

    public Game() {
        gs = GameStates.LOBBY;
        status = ServerStatus.STARTING;
        Bukkit.getWorld("world").setDifficulty(Difficulty.EASY);
        this.opPlayers = new ArrayList<Player>();
        this.anzahlLeicht = 4;
        this.anzahlMittel = 3;
        this.anzahlSchwer = 3;
        this.jumpZeit = 10;
        this.pvpZeit = 5;
        this.anzahlItemsChest = 5;
        this.cj = new CreateJump();
        this.map = this.rand.nextInt(Main.getPlugin().getTpM().getMapAnzahl()) + 1;

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

    public void reset() {
        for (JlPlayer p : this.players) {
            if (p != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("Lobby");
                p.getPlayer().sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
            }
        }

        this.anzahlItemsChest = 5;
        this.anzahlLeicht = 4;
        this.anzahlMittel = 3;
        this.anzahlSchwer = 3;
        this.jumpZeit = 8;
        this.pvpZeit = 5;
        this.map = this.rand.nextInt(2) + 1;
        this.opPlayers = new ArrayList<Player>();
        this.cj.reset();
        this.lp.reset();
        this.jp.reset();
        this.pp.reset();
        gs = GameStates.LOBBY;
        status = ServerStatus.STARTING;
        this.lp.createGame();
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

    public void deOpPlayers() {
        for (Player p : this.players.stream().map(JlPlayer::getPlayer).toList()) {
            if (p.isOp()) {
                p.setOp(false);
                this.opPlayers.add(p);
            }
        }
    }

    public void opPlayers() {
        players.forEach(p -> p.getPlayer().setOp(true));
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

    /**
     * public int getPlayerIndex(Player p) {
     * for (int i = 0; i < this.players.length; ++i) {
     * if (this.players[i] == null) {
     * return -1;
     * }
     * <p>
     * if (this.players[i].getPlayer().equals(p)) {
     * return i;
     * }
     * }
     * <p>
     * return -1;
     * }
     **/

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

    public int getMap() {
        return this.map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public static ServerStatus getStatus() {
        return status;
    }

    public static void setStatus(ServerStatus status) {
        Game.status = status;
    }
}

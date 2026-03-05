package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.ServerStatus;
import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.game.JlPlayer;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class LobbyPhase implements CommandExecutor {
    private int taskID;
    private int taskID1;
    private Game game;
    private ScoreBoardLobby scL = new ScoreBoardLobby();

    public LobbyPhase() {
    }

    public void openCreateInv(Player p) {
        CreateInventory.createInventory();
        CreateInventory.openInventory(p);
    }

    public void createGame() {
        this.game = Main.getPlugin().getGame();
        Bukkit.broadcastMessage("§c[JLG] §fEin Jumpleague-Spiel wird erstellt...");
        this.game.getCj().create();
        Iterator var2 = Bukkit.getWorld("world").getEntities().iterator();

        while (var2.hasNext()) {
            Entity e = (Entity) var2.next();
            if (e instanceof Item) {
                e.remove();
            }
        }

        Bukkit.broadcastMessage("§c[JLG] §fDas Spiel wurde erstellt.");
        this.scL.setScoreBoard();
        this.updateCreated();
    }

    public void updateCreated() {
        this.taskID1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            public void run() {
                if (Game.getGs().equals(GameStates.CREATED)) {
                    LobbyPhase.this.scL.update();
                } else {
                    Bukkit.getScheduler().cancelTask(LobbyPhase.this.taskID1);
                }

            }
        }, 0L, 20L);
    }

    public void startGame(Player p) {
        this.game = Main.getPlugin().getGame();
        if (!Game.getGs().equals(GameStates.CREATED)) {
            Bukkit.broadcastMessage("§c[JLG] §fMomentan kann kein Spiel gestartet werden.");
        } else {
            Game.setGs(GameStates.STARTED);
            Game.setStatus(ServerStatus.INGAME);
            this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
                int countdown = 5;

                public void run() {
                    Bukkit.broadcastMessage("§c[JLG] §fDie Runde startet in " + this.countdown + " Sekunden.");

                    for (JlPlayer p : LobbyPhase.this.game.getPlayers()) {
                        p.getPlayer().setLevel(this.countdown);
                        if (this.countdown == 5) {
                            p.getPlayer().sendTitle("§bJump-League", "");
                        }
                    }

                    LobbyPhase.this.scL.update();
                    if (this.countdown <= 0) {
                        Main.getPlugin().getGame().getJp().startGame();
                        Bukkit.getScheduler().cancelTask(LobbyPhase.this.taskID);
                    }

                    --this.countdown;
                }
            }, 0L, 20L);
        }
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        if (Game.getGs().equals(GameStates.LOBBY) && sender instanceof Player) {
            Player p = (Player) sender;
            CreateInventory.openInventory(p);
        }

        return false;
    }

    public void reset() {
        CreateInventory.createInventory();
    }

    public ScoreBoardLobby getScL() {
        return this.scL;
    }

    public void setScL(ScoreBoardLobby scL) {
        this.scL = scL;
    }
}

package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetJumpLeague implements CommandExecutor {
    public ResetJumpLeague() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player p = (Player)sender;
            if (!p.hasPermission("Reset.Siismonmon")) {
                return false;
            } else {
                Main.getPlugin().getGame();
                Game.setGs(GameStates.LOBBY);
                Main.getPlugin().getGame().getCj().reset();
                Main.getPlugin().getGame().getJp().reset();
                return true;
            }
        }
    }
}

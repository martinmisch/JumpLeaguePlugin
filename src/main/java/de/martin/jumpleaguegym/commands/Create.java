package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Create implements CommandExecutor {
    public Create() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        if (!sender.hasPermission("create")) {
            return false;
        } else if (!(sender instanceof Player)) {
            return false;
        } else {
            if (Game.getGs().equals(GameStates.LOBBY)) {
                Main.getPlugin().getGame().getLp().openCreateInv((Player) sender);
                System.out.println("create");
            } else {
                sender.sendMessage("§e[JLG] §fDu kannst gerade kein Spiel erstellen.");
            }
            return true;
        }
    }
}


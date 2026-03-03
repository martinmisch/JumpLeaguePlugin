package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {
    public Start() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        Main.getPlugin().getGame().getLp().startGame((Player)sender);
        return false;
    }
}

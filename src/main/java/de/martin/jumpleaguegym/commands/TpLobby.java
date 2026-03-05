package de.martin.jumpleaguegym.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpLobby implements CommandExecutor {
    public TpLobby() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("Tp")) {
            return false;
        } else {
            ((Player) sender).teleport(new Location(Bukkit.getWorld("world"), -100.0, 51.0, 0.0, 180.0F, 15.0F));
            return true;
        }
    }
}

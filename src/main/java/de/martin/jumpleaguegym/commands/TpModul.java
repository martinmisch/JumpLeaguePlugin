package de.martin.jumpleaguegym.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpModul implements CommandExecutor {
    public TpModul() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player p = (Player) sender;
            if (!sender.hasPermission("Tp")) {
                return false;
            } else {
                if (args.length == 1) {
                    try {
                        int i = Integer.valueOf(args[0]);
                        p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 51.0, (double) (50 * (i - 1)) + 0.5, 270.0F, 15.0F));
                    } catch (Exception var7) {
                        p.sendMessage("§e[JLG] §f/modul [nummer 1,2,3,...]");
                    }
                } else {
                    p.sendMessage("§e[JLG] §f/modul [nummer 1,2,3,...]");
                }

                return true;
            }
        }
    }
}

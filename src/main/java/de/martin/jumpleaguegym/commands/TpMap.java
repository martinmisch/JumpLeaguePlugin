package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpMap implements CommandExecutor {
    public TpMap() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player p = (Player)sender;
            if (!sender.hasPermission("Tp")) {
                return false;
            } else {
                if (args.length == 1) {
                    try {
                        int i = Integer.valueOf(args[0]);
                        Location loc = Main.getPlugin().getTpM().getDfLocation(i, 1);
                        p.teleport(loc);
                    } catch (Exception var8) {
                        p.sendMessage("§e[JLG] §f/map [nummer 1,2,3,...]");
                    }
                } else {
                    p.sendMessage("§e[JLG] §f/map [nummer 1,2,3,...]");
                }

                return true;
            }
        }
    }
}

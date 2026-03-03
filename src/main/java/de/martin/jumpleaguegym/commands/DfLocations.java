package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DfLocations implements CommandExecutor {
    public DfLocations() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("DfPoint")) {
            return false;
        } else {
            Player p = (Player)sender;
            if (args.length == 2) {
                int nummer;
                int map;
                try {
                    map = Integer.parseInt(args[0]);
                    nummer = Integer.parseInt(args[1]);
                } catch (Exception var9) {
                    p.sendMessage("§e[JLG] §f/dfLocation [map-nr 1-2] [nummer 1 - 10]");
                    return false;
                }

                if (nummer > 0 && nummer <= 10 && map > 0 && map < 100) {
                    Main.getPlugin().getTpM().saveDfLocation("df" + nummer, map, p.getLocation());
                    p.sendMessage("§e[JLG] §fPlayer-Spawn-Location für Spieler " + nummer + " auf Map-Nr. " + map + " gesetzt.");
                } else {
                    p.sendMessage("§e[JLG] §f/dfLocation [map-nr 1-100] [nummer 1 - 10]");
                }
            } else {
                p.sendMessage("§e[JLG] §f/dfLocation [map-nr 1-100] [nummer 1 - 10]");
            }

            return true;
        }
    }
}

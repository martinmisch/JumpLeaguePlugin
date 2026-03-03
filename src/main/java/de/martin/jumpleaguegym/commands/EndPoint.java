package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndPoint implements CommandExecutor {
    public EndPoint() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("EndPoint")) {
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
                    p.sendMessage("§e[JLG] §f/endpoint [map-nr 1-100]  [nummer 1 - 3]");
                    return false;
                }

                if (nummer > 0 && nummer <= 3 && map > 0 && map < 100) {
                    Main.getPlugin().getTpM().saveEndPoint("end" + nummer, map, p.getLocation());
                    p.sendMessage("§e[JLG] §fEnd-Point Location " + nummer + " auf Map-Nr. " + map + " gesetzt.");
                } else {
                    p.sendMessage("§e[JLG] §f/endpoint [map-nr 1-100]  [nummer 1 - 3]");
                }
            } else {
                p.sendMessage("§e[JLG] §f/endpoint [map-nr 1-100]  [nummer 1 - 3]");
            }

            return true;
        }
    }
}

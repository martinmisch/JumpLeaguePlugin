package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDfLocation implements CommandExecutor {
    public TpDfLocation() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("Tp")) {
            return false;
        } else {
            Player p = (Player)sender;
            if (args.length == 2) {
                int nummer;
                int map;
                try {
                    map = Integer.parseInt(args[0]);
                    nummer = Integer.parseInt(args[1]);
                } catch (Exception var11) {
                    p.sendMessage("§e[JLG] §f/dfLocation [map-nr] [nummer 1 - 10]");
                    return false;
                }

                Location loc = Main.getPlugin().getTpM().getDfLocation(map, nummer);

                try {
                    p.teleport(loc);
                    p.sendMessage("§e[JLG] §fZu Df-Location " + nummer + " auf Map-Nr. " + map + " teleportiert.");
                } catch (Exception var10) {
                    p.sendMessage("§e[JLG] §fFehler beim teleportieren.");
                }
            }

            return true;
        }
    }
}

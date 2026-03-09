package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MapLocation implements CommandExecutor {

    // /maplocation [bound, spawnpoint, endpoint] [map-name] [number]
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, String @NotNull [] args) {
        if (!(sender instanceof Player p) || !sender.hasPermission("map")) {
            return false;
        }

        if (args.length != 3) {
            p.sendMessage("§e[JLG] §f/maplocation [bound, spawnpoint, endpoint] [map-name] [number]");
            return false;
        }

        if (args[0].equalsIgnoreCase("bound")) {
            int number = 0;
            try {
                number = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                p.sendMessage("§e[JLG] §fbound kann nur Location 1 und 2 haben.");
                return false;
            }
            if (number < 1 || number > 2) {
                p.sendMessage("§e[JLG] §fbound kann nur Location 1 und 2 haben.");
                return false;
            }
            boolean saved = Main.getPlugin().getTpM().saveLocation(args[1], "bound", number, p.getLocation());
            if (saved) {
                p.sendMessage("§e[JLG] §fLocation wurde gespeichert.");
            } else {
                p.sendMessage("§e[JLG] §fLocation konnte nicht gespeichert werden.");
            }
            return saved;
        } else if (args[0].equalsIgnoreCase("spawnpoint")) {
            int number = 0;
            try {
                number = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                p.sendMessage("§e[JLG] §fspawnpoint kann nur Location 1 bis 10 haben.");
                return false;
            }
            if (number < 1 || number > 10) {
                p.sendMessage("§e[JLG] §fspawnpoint kann nur Location 1 bis 10 haben.");
                return false;
            }
            boolean saved = Main.getPlugin().getTpM().saveLocation(args[1], "spawnpoint", number, p.getLocation());
            if (saved) {
                p.sendMessage("§e[JLG] §fLocation wurde gespeichert.");
            } else {
                p.sendMessage("§e[JLG] §fLocation konnte nicht gespeichert werden.");
            }
            return saved;
        } else if (args[0].equalsIgnoreCase("endpoint")) {
            int number = 0;
            try {
                number = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                p.sendMessage("§e[JLG] §fendpoint kann nur Location 1 bis 3 haben.");
                return false;
            }
            if (number < 1 || number > 3) {
                p.sendMessage("§e[JLG] §fendpoint kann nur Location 1 bis 3 haben.");
                return false;
            }
            boolean saved = Main.getPlugin().getTpM().saveLocation(args[1], "endpoint", number, p.getLocation());
            if (saved) {
                p.sendMessage("§e[JLG] §fLocation wurde gespeichert.");
            } else {
                p.sendMessage("§e[JLG] §fLocation konnte nicht gespeichert werden.");
            }
            return saved;
        }
        return true;
    }
}

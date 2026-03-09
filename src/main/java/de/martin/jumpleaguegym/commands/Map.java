package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Map implements CommandExecutor {

    // /map [create, delete, list] [map-name]
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, String @NotNull [] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("map")) {
            return false;
        }
        Player p = (Player) sender;

        if (args.length == 1 && args[0].equals("list")) {
            List<String> maps = Main.getPlugin().getTpM().getMaps();
            p.sendMessage("§e[JLG] §fFolgende Deathmatch-Maps sind verfügbar: " + maps.toString());
        } else if (args.length == 2 && args[0].equals("create")) {
            boolean created = Main.getPlugin().getTpM().saveMap(args[1]);
            if (created) {
                p.sendMessage("§e[JLG] §fDie Map " + args[1] + " wurde erstellt.");
            } else {
                p.sendMessage("§e[JLG] §fDie Map " + args[1] + " konnte nicht erstellt werden.");
            }
        } else if (args.length == 2 && args[0].equals("delete")) {
            boolean deleted = Main.getPlugin().getTpM().deleteMap(args[1]);
            if (deleted) {
                p.sendMessage("§e[JLG] §fDie Map " + args[1] + " wurde gelöscht.");
            } else {
                p.sendMessage("§e[JLG] §fDie Map " + args[1] + " konnte nicht gelöscht werden.");
            }
        } else {
            p.sendMessage("§e[JLG] §f/map [create, delete, list] [map-name]");
        }
        return true;
    }
}

package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Practise implements CommandExecutor {
    public Practise() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (args[0].equals("leave")) {
                    if (Main.getPlugin().getGame().getPractise().contains((Player) sender)) {
                        Main.getPlugin().getGame().getPractise().leave((Player) sender);
                    }
                } else {
                    try {
                        int n = Integer.parseInt(args[0]);
                        if (!Main.getPlugin().getGame().getPractise().contains((Player) sender)) {
                            if (n > 0 && n <= Main.getPlugin().getGame().getCj().getNumberOfBuildModules()) {
                                Main.getPlugin().getGame().getPractise().join(p, n - 1);
                            } else {
                                p.sendMessage("§e[JLG] Dieses Modul existiert nicht.");
                            }
                        }
                    } catch (Exception var7) {
                        p.sendMessage("§e[JLG] §f/practise leave --> Verlassen");
                        p.sendMessage("§e[JLG] §f/practise [modulnummer] --> Betreten");
                    }
                }
            } else {
                p.sendMessage("§e[JLG] §f/practise leave --> Verlassen");
                p.sendMessage("§e[JLG] §f/practise [modulnummer] --> Betreten");
            }

            return true;
        }
    }
}

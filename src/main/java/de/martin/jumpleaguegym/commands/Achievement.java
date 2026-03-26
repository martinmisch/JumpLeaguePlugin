package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.achievements.DisplayAchievements;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Achievement implements CommandExecutor {
    public Achievement() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player p = (Player) sender;
            if (args.length == 1) {
                p.sendMessage("§e[JLG] §f Achievements von " + args[0]);
                Player playerTarget = Bukkit.getPlayer(args[0]);
                p.openInventory(DisplayAchievements.getAchievementInv(args[0]));
            } else {
                p.sendMessage("§e[JLG] §f/achievements [Name]");
            }

            return true;
        }
    }
}

package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class CreateInventory implements Listener {
    private static Inventory inv;


    public static void createInventory() {
        Game game = Main.getPlugin().getGame();
        inv = Bukkit.createInventory(null, 27, "Erstelle Spiel");
        inv.setItem(9, (new CreateItem("Leicht-Module", Material.GOLD_BLOCK, Main.getPlugin().anzahlLeicht)).getItemStack());
        inv.setItem(10, (new CreateItem("Mittel-Module", Material.IRON_BLOCK, Main.getPlugin().anzahlMittel)).getItemStack());
        inv.setItem(11, (new CreateItem("Schwer-Module", Material.DIAMOND_BLOCK, Main.getPlugin().anzahlSchwer)).getItemStack());
        inv.setItem(13, (new CreateItem("Anzahl Spieler", "01naJ")).getSkullItemStack());
        inv.getItem(13).setAmount(Main.getPlugin().anzahlSpieler);
        inv.setItem(15, (new CreateItem("Jump-Zeit (min)", Material.CLOCK, Main.getPlugin().jumpZeit)).getItemStack());
        inv.setItem(16, (new CreateItem("PvP-Zeit (min)", Material.CLOCK, Main.getPlugin().pvpZeit)).getItemStack());
        inv.setItem(17, (new CreateItem("Max Items in Chest", Material.CARROT, Main.getPlugin().maxChestItems)).getItemStack());
        inv.setItem(26, (new CreateItem("Bestaetigen", Material.RED_DYE, 1)).getItemStack());
    }

    public static void openInventory(Player p) {
        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Game game = Main.getPlugin().getGame();
        if (e.getClickedInventory() != null) {
            if (e.getView().getTitle().equals("Erstelle Spiel")) {
                if (e.getCurrentItem() == null) {
                    return;
                }

                if (e.getSlot() == 9) {
                    inv.getItem(9).setAmount(inv.getItem(9).getAmount() + 1);
                    if (inv.getItem(9).getAmount() >= 5) {
                        inv.getItem(9).setAmount(1);
                    }

                    Main.getPlugin().anzahlLeicht = inv.getItem(9).getAmount();
                } else if (e.getSlot() == 10) {
                    inv.getItem(10).setAmount(inv.getItem(10).getAmount() + 1);
                    if (inv.getItem(10).getAmount() >= 5) {
                        inv.getItem(10).setAmount(1);
                    }

                    Main.getPlugin().anzahlMittel = inv.getItem(10).getAmount();
                } else if (e.getSlot() == 11) {
                    inv.getItem(11).setAmount(inv.getItem(11).getAmount() + 1);
                    if (inv.getItem(11).getAmount() >= 5) {
                        inv.getItem(11).setAmount(1);
                    }

                    Main.getPlugin().anzahlSchwer = inv.getItem(11).getAmount();
                } else if (e.getSlot() == 13) {
                    inv.getItem(13).setAmount(inv.getItem(13).getAmount() + 1);
                    if (inv.getItem(13).getAmount() > 10) {
                        inv.getItem(13).setAmount(1);
                    }
                    Main.getPlugin().anzahlSpieler = inv.getItem(13).getAmount();
                } else if (e.getSlot() == 15) {
                    inv.getItem(15).setAmount(inv.getItem(15).getAmount() + 1);
                    if (inv.getItem(15).getAmount() > 12) {
                        inv.getItem(15).setAmount(2);
                    }

                    Main.getPlugin().jumpZeit = inv.getItem(15).getAmount();
                } else if (e.getSlot() == 16) {
                    inv.getItem(16).setAmount(inv.getItem(16).getAmount() + 1);
                    if (inv.getItem(16).getAmount() > 12) {
                        inv.getItem(16).setAmount(2);
                    }

                    Main.getPlugin().pvpZeit = inv.getItem(16).getAmount();
                } else if (e.getSlot() == 17) {
                    inv.getItem(17).setAmount(inv.getItem(17).getAmount() + 1);
                    if (inv.getItem(17).getAmount() > 10) {
                        inv.getItem(17).setAmount(2);
                    }

                    Main.getPlugin().maxChestItems = inv.getItem(17).getAmount();
                }

                if (e.getSlot() == 26) {
                    e.getWhoClicked().closeInventory();
                    Main.getPlugin().resetAndCreate();
                }

                e.setCancelled(true);
            }
        }
    }
}

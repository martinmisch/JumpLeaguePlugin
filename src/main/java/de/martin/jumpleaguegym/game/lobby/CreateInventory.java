package de.martin.jumpleaguegym.game.lobby;

import de.martin.jumpleaguegym.game.Game;
import de.martin.jumpleaguegym.game.GameStates;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.CreateItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CreateInventory implements Listener {
    private static Inventory inv;

    public CreateInventory() {
    }

    public static void createInventory() {
        Game game = Main.getPlugin().getGame();
        inv = Bukkit.createInventory((InventoryHolder) null, 27, "StartMenu");
        inv.setItem(9, (new CreateItem("Leicht-Module", Material.GOLDEN_BOOTS, game.getAnzahlLeicht())).getItemStack());
        inv.setItem(10, (new CreateItem("Mittel-Module", Material.IRON_BOOTS, game.getAnzahlMittel())).getItemStack());
        inv.setItem(11, (new CreateItem("Schwer-Module", Material.DIAMOND_BOOTS, game.getAnzahlSchwer())).getItemStack());
        inv.setItem(13, (new CreateItem("Anzahl Spieler", "01naJ")).getSkullItemStack());
        inv.getItem(13).setAmount(4);
        inv.setItem(15, (new CreateItem("Jump-Zeit (min)", Material.CLOCK, game.getJumpZeit())).getItemStack());
        inv.setItem(16, (new CreateItem("PvP-Zeit (min)", Material.CLOCK, game.getPvpZeit())).getItemStack());
        inv.setItem(17, (new CreateItem("Anzahl max Chest-Items", Material.STONE_SWORD, game.getAnzahlItemsChest())).getItemStack());
        inv.setItem(18, (new CreateItem("Soup", Material.MUSHROOM_STEW, 1)).getItemStack());
        inv.setItem(26, (new CreateItem("Bestaetigen", Material.INK_SAC, 1)).getItemStack());
        inv.getItem(26).setDurability(DyeColor.GREEN.getDyeData());
    }

    public static void openInventory(Player p) {
        p.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Game game = Main.getPlugin().getGame();
        if (Game.getGs().equals(GameStates.LOBBY)) {
            if (e.getClickedInventory() != null) {
                if (e.getView().getTitle().equals("StartMenu")) {
                    if (e.getCurrentItem() == null) {
                        return;
                    }

                    if (!e.getCurrentItem().hasItemMeta()) {
                        return;
                    }

                    if (!e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        return;
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Leicht-Module")) {
                        inv.getItem(9).setAmount(inv.getItem(9).getAmount() + 1);
                        if (inv.getItem(9).getAmount() >= 5) {
                            inv.getItem(9).setAmount(1);
                        }

                        game.setAnzahlLeicht(inv.getItem(9).getAmount());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Mittel-Module")) {
                        inv.getItem(10).setAmount(inv.getItem(10).getAmount() + 1);
                        if (inv.getItem(10).getAmount() >= 5) {
                            inv.getItem(10).setAmount(1);
                        }

                        game.setAnzahlMittel(inv.getItem(10).getAmount());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Schwer-Module")) {
                        inv.getItem(11).setAmount(inv.getItem(11).getAmount() + 1);
                        if (inv.getItem(11).getAmount() >= 5) {
                            inv.getItem(11).setAmount(1);
                        }

                        game.setAnzahlSchwer(inv.getItem(11).getAmount());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Anzahl Spieler")) {
                        inv.getItem(13).setAmount(inv.getItem(13).getAmount() + 1);
                        if (inv.getItem(13).getAmount() > 10) {
                            inv.getItem(13).setAmount(1);
                        }

                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Jump-Zeit (min)")) {
                        inv.getItem(15).setAmount(inv.getItem(15).getAmount() + 1);
                        if (inv.getItem(15).getAmount() > 12) {
                            inv.getItem(15).setAmount(2);
                        }

                        game.setJumpZeit(inv.getItem(15).getAmount());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("PvP-Zeit (min)")) {
                        inv.getItem(16).setAmount(inv.getItem(16).getAmount() + 1);
                        if (inv.getItem(16).getAmount() > 12) {
                            inv.getItem(16).setAmount(2);
                        }

                        game.setPvpZeit(inv.getItem(16).getAmount());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Anzahl max Chest-Items")) {
                        inv.getItem(17).setAmount(inv.getItem(17).getAmount() + 1);
                        if (inv.getItem(17).getAmount() > 10) {
                            inv.getItem(17).setAmount(2);
                        }

                        game.setAnzahlItemsChest(inv.getItem(17).getAmount());
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("Bestaetigen")) {
                        e.getWhoClicked().closeInventory();
                        game.getLp().createGame();
                    }

                    e.setCancelled(true);
                }
            }
        }
    }
}

package aroueterra.EquipmentFramework.UI.inventory;

import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import static aroueterra.EquipmentFramework.UI.inventory.ItemType.WEAPON;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.DAMAGE;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.PRICE;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.RARITY;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class Inventory {

    private final Item[][] items;
    private CellPane[][] inventoryCells;

    public Inventory(int height, int width) {
        items = new Item[height][width];
        inventoryCells = new CellPane[5][5];
    }

    public boolean store(Item item, int row, int column) {
        if (row < 0 || row >= items.length || column < 0 || column >= items[0].length) {
            System.out.println("Location " + row + ", " + column + " is out of bounds");
            return false;
        }
        if (items[row][column] != null) {
            System.out.println(items[row][column].getItemType() + " [" + items[row][column].getName() + "] occupies this slot.");
            return false;
        }
        items[row][column] = item;
        return true;
    }

    public boolean store(Item item, int row, int column, String assetPath) {
        if (row < 0 || row >= items.length || column < 0 || column >= items[0].length) {
            System.out.println("Location " + row + ", " + column + " is out of bounds");
            return false;
        }
        if (items[row][column] != null) {
            System.out.println(items[row][column].getItemType() + " [" + items[row][column].getName() + "] occupies this slot.");
            return false;
        }
        var comp = retrieveCell(row, column).getComponents();
        for (var i : comp) {
            var slot = ((ImagePanel) i);
            slot.setImage(assetPath);
        }
        items[row][column] = item;
        return true;
    }

    public Item retrieve(int row, int column) {
        if (row < 0 || row >= items.length || column < 0 || column >= items[0].length) {
            System.out.println("Location " + row + ", " + column + " is out of bounds");
            return null;
        }
        if (items[row][column] == null) {
            System.out.println("There's nothing here");
            return null;
        }
        return items[row][column];
    }

    public void checkInventory() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (items[i][j] == null) {
                    System.out.println("No item" + " in row " + i + " col " + j);
                } else {
                    System.out.println(items[i][j].getName() + " in row " + i + " col " + j);
                }
            }
            System.out.println();
        }
    }

    public Map<String, Integer> checkFreeSlots() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (items[i][j] == null) {
                    Map<String, Integer> coordinates = new HashMap<>();
                    coordinates.put("row", i);
                    coordinates.put("column", j);
                    return coordinates;
                } else {
                    System.out.println(items[i][j].getName() + " in row " + i + " col " + j);
                }
            }
            System.out.println();
        }
        return null;
    }

    public CellPane retrieveCell(int row, int column) {
        if (row < 0 || row >= inventoryCells.length || column < 0 || column >= inventoryCells[0].length) {
            System.out.println("Location " + row + ", " + column + " is out of bounds");
            return null;
        }
        if (inventoryCells[row][column] == null) {
            System.out.println("There's nothing here");
            return null;
        }
        return inventoryCells[row][column];
    }

    public void assignCell(CellPane cell, int row, int col) {
        this.inventoryCells[row][col] = cell;
    }

    public void updateCells(JPopupMenu pop) {
        //Temporarily fill inventory with axes.
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                CellPane cell = inventoryCells[row][col];
                ImagePanel inner = new ImagePanel();
                //inner.setImage("/images/wpn-swd.png");
                //Item swd = new Item("sword", WEAPON, "/images/wpn-swd.png");
                //swd.setProperty(DAMAGE, 5);
                //swd.setProperty(RARITY, 1);
                //swd.setProperty(PRICE, 100);
                //store(swd, row, col);
                cell.add(inner, BorderLayout.CENTER);
                cell.setComponentPopupMenu(pop);
            }
        }
        //checkInventory();
    }
}

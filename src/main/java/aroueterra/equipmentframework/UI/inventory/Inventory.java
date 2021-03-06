package aroueterra.EquipmentFramework.UI.inventory;

import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import aroueterra.EquipmentFramework.player.Hero;
import aroueterra.EquipmentFramework.player.Shop;
import aroueterra.EquipmentFramework.utility.JukeBox;
import aroueterra.EquipmentFramework.utility.Song;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class Inventory {

    private final Item[][] items;
    private CellPane[][] inventoryCells;
    JukeBox jukeBox;

    public Inventory(int height, int width) {
        items = new Item[height][width];
        inventoryCells = new CellPane[5][5];
        var songs = new Song[]{
            Song.GOLD,
            Song.EQUIP,
            Song.SELECT,};
        var assets = new String[]{
            "/sounds/gold.wav",
            "/sounds/equip.wav",
            "/sounds/select.wav"
        };
        jukeBox = new JukeBox(songs, assets);
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
            if (assetPath == null) {
                slot.setImage(item.getAsset());
            } else {
                slot.setImage(assetPath);
            }
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
            //System.out.println("There's nothing here");
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
                    //System.out.println(items[i][j].getName() + " in row " + i + " col " + j);
                }
            }
        }
        return null;
    }

    public void repaintCells() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (items[i][j] != null) {
                    Map<String, Integer> coordinates = new HashMap<>();
                    coordinates.put("row", i);
                    coordinates.put("column", j);
                    var cell = retrieveCell(i, j);
                    var item = items[i][j];
                    repaintInnerCell(item.getAsset(), cell.getComponents());
                }
            }
        }
    }

    public void clear() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                var children = inventoryCells[i][j].getComponents();
                for (var child : children) {
                    var panel = (ImagePanel) child;
                    panel.setImage("/images/slot_empty.png");
                }
                items[i][j] = null;
            }
        }
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

    public boolean discard(int row, int column) {
        if (row < 0 || row >= items.length || column < 0 || column >= items[0].length) {
            System.out.println("Location " + row + ", " + column + " is out of bounds");
            return false;
        }
        if (items[row][column] == null) {
            System.out.println("There's nothing here");
            return false;
        }
        var comp = retrieveCell(row, column).getComponents();
        for (var i : comp) {
            var slot = ((ImagePanel) i);
            slot.setImage("/images/slot_empty.png");
        }
        items[row][column] = null;
        return true;
    }

    private void repaintInnerCell(String newAsset, Component[] comp) {
        for (var i : comp) {
            var slot = ((ImagePanel) i);
            if (newAsset.isEmpty()) {
                slot.setImage("/images/slot_empty.png");
            } else {
                slot.setImage(newAsset);
            }

        }
    }

    public boolean discard(java.awt.event.ActionEvent evt, Component invoker) {
        var cell = (CellPane) invoker;
        int row = cell.getRow();
        int column = cell.getColumn();

        if (row < 0 || row >= items.length || column < 0 || column >= items[0].length) {
            System.out.println("Location " + row + ", " + column + " is out of bounds");
            return false;
        }
        if (items[row][column] == null) {
            System.out.println("There's nothing to discard here");
            return false;
        }
        var comp = retrieveCell(row, column).getComponents();
        for (var i : comp) {
            var slot = ((ImagePanel) i);
            slot.setImage("/images/slot_empty.png");
        }
        System.out.println("" + items[row][column].getName() + " was removed");
        items[row][column] = null;

        return true;

    }

    //Creates inner cells once for each inventory cell
    public void createInnerCell(JPopupMenu pop, Map<String, JMenuItem> list, Shop shop) {
        var context = list.get("purchase");

        context.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Component invoker = pop.getInvoker();
                var purchased = shop.purchase(evt, invoker);
                if (purchased) {
                    jukeBox.Play(Song.GOLD);
                }

            }
        });
        pop.add(context);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                CellPane cell = inventoryCells[row][col];
                ImagePanel innerCell = new ImagePanel();
                cell.add(innerCell, BorderLayout.CENTER);
                cell.setComponentPopupMenu(pop);
            }
        }
    }

    public void createInnerCell(JPopupMenu pop, Map<String, JMenuItem> list, Hero hero) {

        var context = list.get("equip");
        context.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Component invoker = pop.getInvoker();
                boolean equipped = hero.equipItem(evt, invoker);
                if (equipped) {
                    jukeBox.Play(Song.EQUIP);
                }

            }
        });
        var context2 = list.get("discard");
        context2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Component invoker = pop.getInvoker();
                hero.inventory.discard(evt, invoker);
                jukeBox.Play(Song.SELECT);
            }
        });
        pop.add(context);
        pop.add(context2);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                CellPane cell = inventoryCells[row][col];
                ImagePanel innerCell = new ImagePanel();
                cell.add(innerCell, BorderLayout.CENTER);
                cell.setComponentPopupMenu(pop);
            }
        }
    }
}

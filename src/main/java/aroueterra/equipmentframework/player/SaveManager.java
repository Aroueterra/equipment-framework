/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.player;

import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.UI.inventory.Item;
import aroueterra.EquipmentFramework.UI.inventory.ItemType;
import aroueterra.EquipmentFramework.UI.inventory.PropertyType;
import aroueterra.EquipmentFramework.utility.JukeBox;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 *
 * @author aroue
 */
public class SaveManager {

    public Map<Integer, String> getEquips() {
        return equips;
    }

    public Map<String, String> getInventoryItem() {
        return inventoryItem;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getGold() {
        return gold;
    }

    public int getExperience() {
        return experience;
    }

    public int getAgility() {
        return agility;
    }

    public int getStrength() {
        return strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    private List<String> equipList = new ArrayList<String>();
    @Expose
    private Map<Integer, String> equips;
    @Expose
    private Map<String, Integer> inventoryCoordinates;
    @Expose
    private Map<String, String> inventoryItem;

    private Item[] equipment = new Item[8];
    @Expose
    private String name;
    @Expose
    private int health;
    @Expose
    private int gold;
    @Expose
    private int experience;

    public Inventory inventory;
    //status
    private int damage;
    private int armor;
    @Expose
    private int agility;
    @Expose
    private int strength;
    @Expose
    private int intelligence;

    public SaveManager(String name, int health, int gold, Inventory inventory, int agility, int strength, int intelligence, Item[] equipment) {
        this.name = name;
        this.health = health;
        this.gold = gold;
        this.inventory = inventory;
        this.agility = agility;
        this.strength = strength;
        this.intelligence = intelligence;
        this.equipment = equipment;
        computeEquipment(this.equipment);
        computeInventory(this.inventory);
    }

    public void computeInventory(Inventory inventory) {
        inventoryItem = new HashMap<>();
        String name = "none";
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                var item = inventory.retrieve(i, j);
                if (item != null) {
                    name = (item.getName());
                    getInventoryItem().put(i + ":" + j, name);
                }
            }
        }
    }

    public void computeEquipment(Item[] equipment) {
        equips = new HashMap<>();
        int counter = 0;
        String name = "none";
        for (var i : equipment) {
            if (i != null) {
                name = (i.getName());
            }
            getEquips().put((Integer) counter, name);
            counter++;
            name = "none";
        }
    }

    public void saveDialog(String json) {
        JFileChooser chooser = new JFileChooser();
        //        chooser.setCurrentDirectory(new File("/home/me/Documents"));
        int option = chooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".json");
                fw.write(json);
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

package aroueterra.EquipmentFramework.player;

import aroueterra.EquipmentFramework.UI.DashboardFrame;
import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import aroueterra.EquipmentFramework.UI.inventory.*;
import static aroueterra.EquipmentFramework.UI.inventory.ItemType.*;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.*;
import aroueterra.EquipmentFramework.utility.JukeBox;
import aroueterra.EquipmentFramework.utility.Song;
import java.awt.Component;
import java.util.Map;
import javax.swing.JLabel;

public class Hero {

    DashboardFrame frame;
    Map<PropertyType, JLabel> statusLabels;
    Map<ItemType, CellPane> components;
    private Item[] equipment = new Item[8];
    private Item previous;
    private String name;
    private int health;
    private int mana;
    private int gold;
    private int experience;
    public Inventory inventory;
    //status
    private int damage;
    private int armor;
    private int agility;
    private int strength;
    private int intelligence;
    private JukeBox jukeBox;

    public Hero(String name, int health, int mana, int gold, Inventory inventory) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.gold = gold;
        this.inventory = inventory;
        jukeBox = new JukeBox(Song.POTION, "/sounds/potion.wav");
    }

    public Hero(String name, int health, int gold, Inventory inventory, Item[] equipment, int strength, int agility, int intelligence) {
        this.name = name;
        this.health = health;
        this.gold = gold;
        this.inventory = inventory;
        this.equipment = equipment;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        jukeBox = new JukeBox(Song.POTION, "/sounds/potion.wav");
    }

    public Hero(String name, int health, int mana, int gold, Inventory inventory, Map<ItemType, CellPane> components) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.gold = gold;
        this.inventory = inventory;
        this.components = components;
        jukeBox = new JukeBox(Song.POTION, "/sounds/potion.wav");
    }

    public Item retrieveEquipment(int index) {
        if (index < 0 || index >= getEquipment().length) {
            System.out.println("Equip slot " + index + ", is out of bounds");
            return null;
        }
        if (getEquipment()[index] == null) {
            System.out.println("There's nothing equipped there");
            return null;
        }
        return getEquipment()[index];
    }

    //Equip via context menu
    public boolean equipItem(java.awt.event.ActionEvent evt, Component invoker) {
        var slot = (CellPane) invoker;
        int row = slot.getRow();
        int column = slot.getColumn();
        var item = this.inventory.retrieve(row, column);
        if (item == null) {
            System.out.println("There is nothing here!");
            return false;
        }
        if (item.getItemType() == ItemType.CONSUMABLE) {
            var child = slot.getComponents();
            System.out.println("You quaffed the potion, healing 20 HP ");
            jukeBox.Play(Song.POTION);
            setHealth(getHealth() + 20);
            frame.updateHealth(getHealth());
            inventory.discard(row, column);
            return false;
        }
        var success = equipItem(item);
        if (success) {
            System.out.println("The item was equipped");
            inventory.discard(row, column);
            return true;
        } else {
            System.out.println("The item was not equipped!");
            return false;
        }
    }

    public boolean equipItem(Item item) {
        var cell = components.get(item.getItemType()).getComponents();
        for (var i : cell) {
            var slot = ((ImagePanel) i);
            slot.setImage("/images/slot_empty.png");
            slot.setImage(item.getAsset());
        }
        return switch (item.getItemType()) {
            case NECK ->
                inventoryParity(0, item);
            case HEAD ->
                inventoryParity(1, item);
            case WEAPON ->
                inventoryParity(2, item);
            case CHEST ->
                inventoryParity(3, item);
            case OFFHAND ->
                inventoryParity(4, item);
            case LOWER ->
                inventoryParity(5, item);
            case FEET ->
                inventoryParity(6, item);
            case HANDS ->
                inventoryParity(7, item);
            case CONSUMABLE ->
                false;
            default ->
                false;
        };
    }

    public void unequipItem(int index) {
        getEquipment()[index] = null;
        System.out.println(getDamage());
    }

    public void repaintEquips() {
        for (var item : equipment) {
            if (item != null) {
                var cell = components.get(item.getItemType()).getComponents();
                for (var inner : cell) {
                    var slot = ((ImagePanel) inner);
                    //slot.setImage("/images/slot_empty.png");
                    slot.setImage(item.getAsset());
                }
            }
        }

    }

    private boolean inventoryParity(int index, Item item) {
        if (getEquipment()[index] != null) {
            previous = getEquipment()[index];
            var coordinates = this.inventory.checkFreeSlots();
            if (coordinates == null) {
                return false;
            } else {
                this.inventory.store(previous, coordinates.get("row"), coordinates.get("column"), previous.getAsset());
            }
        }
        getEquipment()[index] = item;
        balanceStatus();
        return true;
    }

    public void Get() {
        Item axe = new Item("axe", WEAPON, "/images/wpn-axe.png");
        axe.setProperty(DAMAGE, 5);
        axe.setProperty(RARITY, 1);
        axe.setProperty(PRICE, 100);
        boolean storedSuccessfully = getInventory().store(axe, 0, 0);
        if (!storedSuccessfully) {
            System.out.println("could not store the axe in the inventory");
            return;
        }
        Item sameAxe = getInventory().retrieve(0, 0);
        if (sameAxe == null) {
            System.out.println("did not find the axe in the inventory");
            return;
        }
        int axeDamage = sameAxe.getProperty(DAMAGE);
        System.out.println("The axe makes " + axeDamage + " damage");
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getGold() {
        return gold;
    }

    public int getExperience() {
        return experience;
    }

    public void balanceStatus(Item item) {
        if (item != null) {
            if (item.getItemType() == ItemType.WEAPON) {
                setDamage(getDamage() + item.getProperty(DAMAGE));
                updateLabels(PropertyType.DAMAGE);
            } else {
                setArmor(getArmor() + item.getProperty(ARMOR));
                updateLabels(PropertyType.ARMOR);
            }
        }
    }

    public void balanceStatus() {
        setDamage(0);
        setArmor(0);
        for (var item : getEquipment()) {
            if (item != null) {
                if (item.getItemType() == ItemType.WEAPON) {
                    setDamage(getDamage() + item.getProperty(DAMAGE));
                    updateLabels(PropertyType.DAMAGE);
                } else {
                    setArmor(getArmor() + item.getProperty(ARMOR));
                    updateLabels(PropertyType.ARMOR);
                }
            } else {
                updateLabels();
            }
        }
    }

    public void setFrame(DashboardFrame frame) {
        this.frame = frame;
    }

    public void updateLabels() {
        var label = statusLabels.get(PropertyType.DAMAGE);
        label.setText(Integer.toString(getDamage()));
        label = statusLabels.get(PropertyType.ARMOR);
        label.setText(Integer.toString(getArmor()));
        label = statusLabels.get(PropertyType.STRENGTH);
        label.setText(Integer.toString(getStrength()));
        label = statusLabels.get(PropertyType.AGILITY);
        label.setText(Integer.toString(getAgility()));
        label = statusLabels.get(PropertyType.INTELLIGENCE);
        label.setText(Integer.toString(getIntelligence()));
    }

    public void updateLabels(PropertyType type) {
        var label = statusLabels.get(type);
        switch (type) {
            case DAMAGE ->
                label.setText(Integer.toString(getDamage()));
            case ARMOR ->
                label.setText(Integer.toString(getArmor()));
            case STRENGTH ->
                label.setText(Integer.toString(getStrength()));
            case AGILITY ->
                label.setText(Integer.toString(getAgility()));
            case INTELLIGENCE ->
                label.setText(Integer.toString(getIntelligence()));
        }
    }

    public void getStatus() {
        System.out.println("DAM " + getDamage());
        System.out.println("ARM " + getArmor());
        System.out.println("STR " + getStrength());
        System.out.println("AGI " + getAgility());
        System.out.println("INT " + getIntelligence());
    }

    public void setEquipComponents(Map<ItemType, CellPane> components) {
        this.components = components;
    }

    public void setLabelComponents(Map<PropertyType, JLabel> statusLabels) {
        this.statusLabels = statusLabels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        if (health > 100) {
            this.health = 100;
        } else if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }

    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setExperience(int experience) {
        this.experience = experience;
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

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getDamage() {
        return damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public Item[] getEquipment() {
        return equipment;
    }

    public void setEquipment(Item[] equipment) {
        this.equipment = equipment;
    }
}

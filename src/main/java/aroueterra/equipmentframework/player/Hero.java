package aroueterra.EquipmentFramework.player;

import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import aroueterra.EquipmentFramework.UI.inventory.*;
import static aroueterra.EquipmentFramework.UI.inventory.ItemType.*;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.*;
import java.awt.Component;
import java.util.Map;

public class Hero {

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
    private int agility;
    private int strength;
    private int intelligence;

    public Hero(String name, int health, int mana, int gold, Inventory inventory) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.gold = gold;
        this.inventory = inventory;
    }

    public Hero(String name, int health, int mana, int gold, Inventory inventory, Map<ItemType, CellPane> components) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.gold = gold;
        this.inventory = inventory;
        this.components = components;
    }

    public Item retrieveEquipment(int index) {
        if (index < 0 || index >= equipment.length) {
            System.out.println("Equip slot " + index + ", is out of bounds");
            return null;
        }
        if (equipment[index] == null) {
            System.out.println("There's nothing equipped there");
            return null;
        }
        return equipment[index];
    }

    //Equip via context menu
    public boolean equipItem(java.awt.event.ActionEvent evt, Component invoker) {
        var slot = (CellPane) invoker;
        int row = slot.getRow();
        int column = slot.getColumn();
        var item = this.inventory.retrieve(row, column);
        if (item.getItemType() == ItemType.CONSUMABLE) {
            System.out.println("Quaffed potion");
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
        var index = 0;
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
                true;
            default ->
                false;
        };
    }

    private boolean inventoryParity(int index, Item item) {
        if (equipment[index] != null) {
            previous = equipment[index];
            var coordinates = this.inventory.checkFreeSlots();
            if (coordinates == null) {
                return false;
            } else {
                this.inventory.store(previous, coordinates.get("row"), coordinates.get("column"), previous.getAsset());
            }
        }
        equipment[index] = item;
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

    public void setComponents(Map<ItemType, CellPane> components) {
        this.components = components;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
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
}

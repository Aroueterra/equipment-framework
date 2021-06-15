package aroueterra.EquipmentFramework.player;

import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.inventory.*;
import static aroueterra.EquipmentFramework.UI.inventory.ItemType.*;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.*;
import java.awt.Component;

public class Hero {

    private String name;
    private int health;
    private int mana;
    private int gold;
    private int experience;
    public Inventory inventory;

    public Hero(String name, int health, int mana, int gold, Inventory inventory) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.gold = gold;
        this.inventory = inventory;
    }

    public void equipItem(java.awt.event.ActionEvent evt, Component invoker) {
        var slot = (CellPane) invoker;
        int row = slot.getRow();
        int column = slot.getColumn();
        System.out.println("The item was equipped");
        inventory.discard(row, column);
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

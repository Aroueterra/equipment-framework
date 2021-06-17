/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.player;

import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.UI.custom.ShopInventory;
import aroueterra.EquipmentFramework.UI.inventory.PropertyType;
import java.awt.Component;
import javax.swing.JTextField;

/**
 *
 * @author aroue
 */
public class Shop extends Hero {

    private JTextField shopCoffers;
    private JTextField heroCoffers;
    public Inventory inventory;
    Hero hero;

    public Shop(int gold, Inventory inventory, Hero hero, JTextField coins, JTextField savings) {
        super("Merchant", 1, 1, gold, inventory);
        this.inventory = inventory;
        this.hero = hero;
        this.shopCoffers = coins;
        this.heroCoffers = savings;
    }

    public void purchase(java.awt.event.ActionEvent evt, Component invoker) {
        var cell = (CellPane) invoker;
        var coordinates = hero.inventory.checkFreeSlots();
        int row = cell.getRow(), column = cell.getColumn();
        int gain = inventory.retrieve(row, column).getProperty(PropertyType.PRICE);
        if (hero.getGold() < gain || hero.getGold() <= 0) {
            System.out.println("You do not have enough gold!");
            return;
        }
        if (coordinates != null) {

            this.setGold(this.getGold() + gain);
            hero.setGold(hero.getGold() - gain);
            updateFields();
            var item = this.inventory.retrieve(row, column);
            hero.inventory.store(item, coordinates.get("row"), coordinates.get("column"), item.getAsset());
            inventory.discard(evt, invoker);
        } else {
            System.out.println("Cannot purchase, your inventory is full!");
        }
    }

    public void updateFields() {
        shopCoffers.setText(Integer.toString(this.getGold()));
        heroCoffers.setText(Integer.toString(hero.getGold()));
    }

}

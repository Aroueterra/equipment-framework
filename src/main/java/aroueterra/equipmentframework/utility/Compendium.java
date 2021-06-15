/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.utility;

import aroueterra.EquipmentFramework.UI.inventory.Item;
import aroueterra.EquipmentFramework.UI.inventory.ItemType;
import aroueterra.EquipmentFramework.UI.inventory.PropertyType;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.HashSet;

/**
 *
 * @author aroue
 */
public class Compendium {

    private String name;
    private String path;
    private String type;
    private int p1;
    private int p2;
    private int p3;

    public Item materialize() {
        var item = new Item(this.name, this.getType(), this.path);
        if (this.getType() == ItemType.WEAPON) {
            item.setProperty(PropertyType.DAMAGE, p1);
            item.setProperty(PropertyType.RARITY, p2);
        } else if (this.getType() == ItemType.CONSUMABLE) {
            item.setProperty(PropertyType.QUALITY, p1);
            item.setProperty(PropertyType.QUANTITY, p2);
        } else {
            item.setProperty(PropertyType.ARMOR, p1);
            item.setProperty(PropertyType.RARITY, p2);
        }
        item.setProperty(PropertyType.PRICE, p3);
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public ItemType getType() {
        return switch (this.type) {
            case "weapon" ->
                ItemType.WEAPON;
            case "off" ->
                ItemType.OFFHAND;
            case "armor-head" ->
                ItemType.HEAD;
            case "armor-chest" ->
                ItemType.CHEST;
            case "armor-hands" ->
                ItemType.HANDS;
            case "armor-lower" ->
                ItemType.LOWER;
            case "armor-feet" ->
                ItemType.FEET;
            case "armor-neck" ->
                ItemType.NECK;
            case "consumable" ->
                ItemType.CONSUMABLE;
            default ->
                ItemType.CONSUMABLE;
        };
    }

    public void setType(String value) {
        this.type = value;
    }

    public long getP1() {
        return p1;
    }

    public void setP1(int value) {
        this.p1 = value;
    }

    public long getP2() {
        return p2;
    }

    public void setP2(int value) {
        this.p2 = value;
    }

    public long getP3() {
        return p3;
    }

    public void setP3(int value) {
        this.p3 = value;
    }

}

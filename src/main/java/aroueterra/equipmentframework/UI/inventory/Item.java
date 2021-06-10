package aroueterra.EquipmentFramework.UI.inventory;

import java.util.EnumMap;
import java.util.Map;

public class Item {

    private final String name;
    private final String asset;
    private final ItemType itemType;
    private final Map<PropertyType, Integer> properties = new EnumMap<>(PropertyType.class);

    public Item(String name, ItemType type, String asset) {
        this.name = name;
        this.itemType = type;
        this.asset = asset;
    }

    public void setProperty(PropertyType property, int value) {
        this.properties.put(property, value);
    }

    public int getProperty(PropertyType property) {
        return this.properties.getOrDefault(property, 0);
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public String getName() {
        if (this.name == null || this.name == "") {
            return "There is no item ";
        } else {
            return this.name;
        }
    }

    public String getAsset() {
        return asset;
    }
}

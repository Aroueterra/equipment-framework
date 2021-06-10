//package inventory;
//
//import static inventory.PropertyType.*;
//import static inventory.ItemType.*;
//
//public class Main {
//    public static void main(String[] args) {
//
//        Inventory i = new Inventory(5, 5);
//
//        Item axe = new Item("axe", WEAPON);
//        axe.setProperty(DAMAGE, 5);
//        axe.setProperty(RARITY, 1);
//        axe.setProperty(PRICE, 100);
//
//        boolean storedSuccessfully = i.store(axe, 0, 0);
//        if (!storedSuccessfully) {
//            System.out.println("could not store the axe in the inventory");
//            return;
//        }
//
//        Item sameAxe = i.retrieve(0, 0);
//        if (sameAxe == null) {
//            System.out.println("did not find the axe in the inventory");
//            return;
//        }
//        int axeDamage = sameAxe.getProperty(DAMAGE);
//        System.out.println("The axe makes " + axeDamage + " damage");
//    }
//}

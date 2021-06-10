/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.player;

/**
 *
 * @author aroue
 */
public class Constants {

    public static String Rarity(int v) {
        switch (v) {
            case 1:
                return "common";
            case 2:
                return "uncommon";
            case 3:
                return "rare";
            case 4:
                return "legendary";
            default:
                return "invalid";
        }
    }
}

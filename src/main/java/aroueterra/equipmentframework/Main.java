package aroueterra.EquipmentFramework;

import aroueterra.EquipmentFramework.UI.DashboardFrame;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.player.Hero;
import aroueterra.EquipmentFramework.utility.Compendium;
import aroueterra.equipmentframework.UI.SetupFrame;
import java.awt.Frame;
//import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;

public class Main {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        //Setup();
        CreateDashboard();

    }

    public static void CreateDashboard() {
        var instance = new Main();
        var compendium = instance.generateCompendium();
        Inventory i = new Inventory(5, 5);
        Hero hero = new Hero("", 100, 100, 1000, i);
        var dashboard = new DashboardFrame(hero, compendium);
        dashboard.setVisible(true);
        dashboard.toFront();
        dashboard.setState(Frame.MAXIMIZED_BOTH);
        dashboard.requestFocus();
    }

    public static void Setup() {
        var setup = new SetupFrame();
        setup.setLocationRelativeTo(null);
        setup.setVisible(true);
        setup.toFront();
        //setup.setState(Frame.MAXIMIZED_BOTH);
        setup.requestFocus();
    }

    private Compendium[] generateCompendium() {
        Reader reader = new InputStreamReader(this.getClass()
                .getResourceAsStream("/json/compendium2.txt"));
        Compendium[] compendium = new Gson().fromJson(reader, Compendium[].class);
        return compendium;
        //System.out.println(compendium[0].getName());
    }
}

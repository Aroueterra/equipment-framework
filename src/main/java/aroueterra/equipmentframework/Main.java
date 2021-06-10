package aroueterra.EquipmentFramework;

import aroueterra.EquipmentFramework.UI.DashboardFrame;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.player.Hero;
import java.awt.Frame;
//import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import java.util.logging.Logger;

public class Main {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        //FlatDarkLaf.install();
        FlatDarkLaf.setup();
        CreateDashboard();
    }

    public static void CreateDashboard() {
        Inventory i = new Inventory(5, 5);
        Hero hero = new Hero("Vendredi", 100, 100, 1000, i);
        var dashboard = new DashboardFrame(hero);
        dashboard.setVisible(true);
        dashboard.toFront();
        dashboard.setState(Frame.MAXIMIZED_BOTH);
        dashboard.requestFocus();
    }
}

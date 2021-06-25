/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI;

import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.custom.MessageConsole;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.UI.inventory.Item;
import aroueterra.EquipmentFramework.player.Hero;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.*;
import aroueterra.EquipmentFramework.UI.custom.ZoomPanel;
import aroueterra.EquipmentFramework.player.Shop;
import aroueterra.EquipmentFramework.UI.custom.ShopInventory;
import aroueterra.EquipmentFramework.UI.inventory.ItemType;
import aroueterra.EquipmentFramework.UI.inventory.PropertyType;
import aroueterra.EquipmentFramework.player.SaveManager;
import aroueterra.EquipmentFramework.utility.Compendium;
import aroueterra.EquipmentFramework.utility.JukeBox;
import aroueterra.EquipmentFramework.utility.Song;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * https://stackoverflow.com/questions/15421708/how-to-draw-grid-using-swing-class-java-and-detect-mouse-position-when-click-and
 * https://stackoverflow.com/questions/18421674/using-gson-to-parse-a-json-array
 *
 * @author aroue
 */
public class DashboardFrame extends javax.swing.JFrame {

    Hero hero;
    Shop shop;
    Compendium[] compendium;
    JukeBox jukeBox;
    static boolean toggled = false;

    public DashboardFrame(Hero hero, Compendium[] compendium) {
        Font exocet = null;
        try {
            InputStream is = this.getClass().getResourceAsStream("/font/exocet.ttf");
            exocet = Font.createFont(Font.TRUETYPE_FONT, is);
            exocet = exocet.deriveFont(16f);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setFont(new FontUIResource(exocet));

        this.hero = hero;
        this.compendium = compendium;
        jukeBox = new JukeBox();
        initComponents();

        heroInit();

        this.shop = new Shop(0, new Inventory(5, 5), hero, shopCoinField, coinField);
        BufferedImage shopslot_bg = null, slot_bg = null, icon = null;
        try {
            shopslot_bg = ImageIO.read(getClass().getResource("/images/slot_paper.png"));
            slot_bg = ImageIO.read(getClass().getResource("/images/slot.png"));
            icon = ImageIO.read(getClass().getResource("/images/icon_large.png"));
            this.setIconImage(icon);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        CreateCells(slot_bg, inventoryCard, hero, 5);
        CreateCells(shopslot_bg, shopPanel, shop, 5);
        hero.inventory.repaintCells();
        hero.repaintEquips();
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }

    private void heroInit() {
        registerComponents();
        hero.balanceStatus();
        //hero.getStatus();
        lblName.setText(hero.getName());
        updateLabelMessage();
        updateHealth(hero.getHealth());
        coinField.setText(Integer.toString(hero.getGold()));
        hero.setFrame(this);
    }

    //Create hero slots
    private void CreateCells(BufferedImage img, JPanel parent, Hero hero, int count) {
        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                CellPane cellPane;
                cellPane = new CellPane(img, hero, row, col);
                Border border = null;
                if (row < 4) {
                    if (col < 4) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < 4) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                hero.getInventory().assignCell(cellPane, row, col);
                cellPane.setBorder(border);
                cellPane.saveBorder();
                parent.add(cellPane, gbc);
            }
        }
        //Creates inner cell for each slot
        var list = new HashMap<String, JMenuItem>();
        JSeparator sp = new JSeparator();
        list.put("equip", equipContext);
        list.put("discard", discardContext);
        hero.inventory.createInnerCell(inventoryPop, list, hero);
    }

    //Create shop slots
    private void CreateCells(BufferedImage img, JPanel parent, Shop shop, int count) {
        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                CellPane cellPane = new CellPane(img, hero, shop, row, col);
                Border border = null;
                if (row < 4) {
                    if (col < 4) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < 4) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                shop.getInventory().assignCell(cellPane, row, col);
                cellPane.setBorder(border);
                cellPane.saveBorder();
                parent.add(cellPane, gbc);
            }
        }
        //Creates inner cell for each slot
        var list = new HashMap<String, JMenuItem>();
        list.put("purchase", buyContext);
        shop.inventory.createInnerCell(shopPop, list, shop);
    }

    public void updateHealth(int health) {
        healthField.setText(Integer.toString(health));
        ((ZoomPanel) orb).setToolTipText(Integer.toString(health) + "/100");
        if (health > 90) {
            healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_small_100.png")));
            ((ZoomPanel) orb).setImage("/images/health_100.png");
        } else if (health > 51) {
            healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_small_75.png")));
            ((ZoomPanel) orb).setImage("/images/health_75.png");
        } else if (health > 26) {
            healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_small_50.png")));
            ((ZoomPanel) orb).setImage("/images/health_50.png");
        } else if (health > 10) {
            healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_small_25.png")));
            ((ZoomPanel) orb).setImage("/images/health_25.png");
        } else {
            healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_small_0.png")));
            ((ZoomPanel) orb).setImage("/images/health_0.png");
        }
    }

    private HashMap<PropertyType, JLabel> collectStatusLabels() {
        var statusLabels = new HashMap<PropertyType, JLabel>();
        statusLabels.put(PropertyType.DAMAGE, (JLabel) lblDamage);
        statusLabels.put(PropertyType.ARMOR, (JLabel) lblArmor);
        statusLabels.put(PropertyType.STRENGTH, (JLabel) lblStr);
        statusLabels.put(PropertyType.AGILITY, (JLabel) lblAgi);
        statusLabels.put(PropertyType.INTELLIGENCE, (JLabel) lblInt);
        return statusLabels;
    }

    private HashMap<ItemType, CellPane> setupEquipSlots() {
        var equipmentSlots = new HashMap<ItemType, CellPane>();
        equipmentSlots.put(ItemType.NECK, (CellPane) neckPanel);
        equipmentSlots.put(ItemType.HEAD, (CellPane) headPanel);
        equipmentSlots.put(ItemType.WEAPON, (CellPane) leftWeaponPanel);
        equipmentSlots.put(ItemType.CHEST, (CellPane) chestPanel);
        equipmentSlots.put(ItemType.OFFHAND, (CellPane) rightWeaponPanel);
        equipmentSlots.put(ItemType.LOWER, (CellPane) lowerPanel);
        equipmentSlots.put(ItemType.FEET, (CellPane) feetPanel);
        equipmentSlots.put(ItemType.HANDS, (CellPane) handPanel);
        return equipmentSlots;
    }

    private void updateLabelMessage() {
        lblDamage.setText(Integer.toString(hero.getDamage()));
        lblArmor.setText(Integer.toString(hero.getArmor()));
        lblStr.setText(Integer.toString(hero.getStrength()));
        lblAgi.setText(Integer.toString(hero.getAgility()));
        lblInt.setText(Integer.toString(hero.getIntelligence()));
    }

    private void registerComponents() {
        hero.setEquipComponents(setupEquipSlots());
        hero.setLabelComponents(collectStatusLabels());

    }

    private void randomizeShop(Compendium[] compendium, int max, int min) {
        var rand = new Random();
        var slots = shop.inventory.checkFreeSlots();
        if (slots == null) {
            shop.inventory.clear();
            return;
        }
        do {
            int val = rand.nextInt((max - min) + 1) + min;
            var item = compendium[val];
            AddItem(item.materialize(), shop.inventory, slots);
            slots = shop.inventory.checkFreeSlots();
        } while (slots != null);
    }

    private void setFont(FontUIResource myFont) {
        UIManager.put("CheckBoxMenuItem.acceleratorFont", myFont);
        UIManager.put("Button.font", myFont);
        UIManager.put("ToggleButton.font", myFont);
        UIManager.put("RadioButton.font", myFont);
        UIManager.put("CheckBox.font", myFont);
        UIManager.put("ColorChooser.font", myFont);
        UIManager.put("ComboBox.font", myFont);
        //UIManager.put("Label.font", myFont);
        UIManager.put("List.font", myFont);
        UIManager.put("MenuBar.font", myFont);
        UIManager.put("Menu.acceleratorFont", myFont);
        UIManager.put("RadioButtonMenuItem.acceleratorFont", myFont);
        UIManager.put("MenuItem.acceleratorFont", myFont);
        UIManager.put("MenuItem.font", myFont);
        UIManager.put("RadioButtonMenuItem.font", myFont);
        UIManager.put("CheckBoxMenuItem.font", myFont);
        UIManager.put("OptionPane.buttonFont", myFont);
        UIManager.put("OptionPane.messageFont", myFont);
        UIManager.put("Menu.font", myFont);
        UIManager.put("PopupMenu.font", myFont);
        UIManager.put("OptionPane.font", myFont);
        UIManager.put("Panel.font", myFont);
        UIManager.put("ProgressBar.font", myFont);
        UIManager.put("ScrollPane.font", myFont);
        //UIManager.put("Viewport.font", myFont);
        UIManager.put("TabbedPane.font", myFont);
        UIManager.put("Slider.font", myFont);
        UIManager.put("Table.font", myFont);
        UIManager.put("TableHeader.font", myFont);
        UIManager.put("TextField.font", myFont);
        UIManager.put("Spinner.font", myFont);
        UIManager.put("PasswordField.font", myFont);
        UIManager.put("TextArea.font", myFont);
        UIManager.put("TextPane.font", myFont);
        UIManager.put("EditorPane.font", myFont);
        UIManager.put("TabbedPane.smallFont", myFont);
        //UIManager.put("TitledBorder.font", myFont);
        UIManager.put("ToolBar.font", myFont);
        UIManager.put("ToolTip.font", myFont);
        UIManager.put("Tree.font", myFont);
        UIManager.put("FormattedTextField.font", myFont);
        UIManager.put("IconButton.font", myFont);
        UIManager.put("InternalFrame.optionDialogTitleFont", myFont);
        UIManager.put("InternalFrame.paletteTitleFont", myFont);
        //UIManager.put("InternalFrame.titleFont", myFont);
    }

    private void AddItem(Item item, Inventory inventory, Map<String, Integer> coordinates) {
        if (coordinates != null) {
            inventory.store(item, coordinates.get("row"), coordinates.get("column"), item.getAsset());
        } else {
            System.out.println("Item cannot be added: no free slots available.");
        }
    }

    private void AddItem(String itemName, ItemType itemType, String resource, int property1, int property2, int property3) {
        BufferedImage item = null;
        try {
            item = ImageIO.read(getClass().getResource(resource));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        var coordinates = hero.inventory.checkFreeSlots();
        if (coordinates != null) {
            Item newItem = new Item(itemName, itemType, resource);
            newItem.setProperty(DAMAGE, property1);
            newItem.setProperty(RARITY, property2);
            newItem.setProperty(PRICE, property3);
            hero.inventory.store(newItem, coordinates.get("row"), coordinates.get("column"), resource);
        } else {
            System.out.println("Item cannot be added: no free slots available.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        inventoryPop = new javax.swing.JPopupMenu();
        discardContext = new javax.swing.JMenuItem();
        equipContext = new javax.swing.JMenuItem();
        rightTab = new javax.swing.JTabbedPane();
        shopPop = new javax.swing.JPopupMenu();
        buyContext = new javax.swing.JMenuItem();
        sellContext = new javax.swing.JMenuItem();
        unequipContext = new javax.swing.JMenuItem();
        equipPop = new javax.swing.JPopupMenu();
        button_AddItem = new javax.swing.JButton();
        credits = new javax.swing.JLabel();
        emptyToggle1 = new javax.swing.JToggleButton();
        wrappingPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        orbHolder = new javax.swing.JPanel();
        orb = new javax.swing.JPanel();
        headerHolder = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        workHolder = new javax.swing.JPanel();
        scroll_Logs = new javax.swing.JScrollPane();
        textarea_status = new javax.swing.JTextArea();
        workPanel = new javax.swing.JPanel();
        scroll_WorkArea = new javax.swing.JScrollPane();
        splitPanel_WorkArea = new javax.swing.JSplitPane();
        leftSplit = new javax.swing.JPanel();
        leftCards = new javax.swing.JPanel();
        worldCard = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        worldPanel = new javax.swing.JPanel();
        inventoryCard = new javax.swing.JPanel();
        lblLeftSplit = new javax.swing.JLabel();
        mapCard = new javax.swing.JPanel();
        mapPanel = new javax.swing.JPanel();
        hotBar = new javax.swing.JPanel();
        spacer = new javax.swing.JLabel();
        inventoryToggle = new javax.swing.JToggleButton();
        btnMap = new javax.swing.JButton();
        equipToggle = new javax.swing.JToggleButton();
        shopToggle = new javax.swing.JToggleButton();
        btnSave = new javax.swing.JButton();
        btnHeal = new javax.swing.JButton();
        btnPoison = new javax.swing.JButton();
        coinStatus = new javax.swing.JPanel();
        coinIcon = new javax.swing.JLabel();
        coinHolder = new javax.swing.JPanel();
        coinField = new javax.swing.JTextField();
        healthStatus = new javax.swing.JPanel();
        healthIcon = new javax.swing.JLabel();
        healthHolder = new javax.swing.JPanel();
        healthField = new javax.swing.JTextField();
        spacer1 = new javax.swing.JLabel();
        rightSplit = new javax.swing.JPanel();
        rightCards = new javax.swing.JPanel();
        statusCard = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        detailsPanel = new javax.swing.JPanel();
        detailsBackground = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnAddStr = new javax.swing.JButton();
        btnAddAgi = new javax.swing.JButton();
        btnAddInt = new javax.swing.JButton();
        lblInt = new javax.swing.JLabel();
        lblAgi = new javax.swing.JLabel();
        lblStr = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblArmor = new javax.swing.JLabel();
        lblDamage = new javax.swing.JLabel();
        equipmentCard = new javax.swing.JPanel();
        leftWeaponPanel = new javax.swing.JPanel();
        leftWeapon = new javax.swing.JPanel();
        neckPanel = new javax.swing.JPanel();
        neckArmor = new javax.swing.JPanel();
        headPanel = new javax.swing.JPanel();
        headArmor = new javax.swing.JPanel();
        handPanel = new javax.swing.JPanel();
        handArmor = new javax.swing.JPanel();
        chestPanel = new javax.swing.JPanel();
        chestArmor = new javax.swing.JPanel();
        rightWeaponPanel = new javax.swing.JPanel();
        rightWeapon = new javax.swing.JPanel();
        lowerPanel = new javax.swing.JPanel();
        lowerArmor = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        feetPanel = new javax.swing.JPanel();
        feetArmor = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        shopCard = new javax.swing.JPanel();
        shopPanel = new javax.swing.JPanel();
        shopBar = new javax.swing.JPanel();
        spacer2 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JToggleButton();
        coinStatus1 = new javax.swing.JPanel();
        coinIcon1 = new javax.swing.JLabel();
        coinHolder1 = new javax.swing.JPanel();
        shopCoinField = new javax.swing.JTextField();
        spacer3 = new javax.swing.JLabel();
        credit = new javax.swing.JLabel();

        inventoryPop.setBackground(new java.awt.Color(102, 102, 102));
        inventoryPop.setForeground(new java.awt.Color(255, 255, 255));
        inventoryPop.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                inventoryPopPopupMenuWillBecomeVisible(evt);
            }
        });

        discardContext.setBackground(new java.awt.Color(255, 0, 0));
        discardContext.setForeground(new java.awt.Color(255, 204, 204));
        discardContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/discard.png"))); // NOI18N
        discardContext.setText("Discard");

        equipContext.setForeground(new java.awt.Color(255, 255, 255));
        equipContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/take.png"))); // NOI18N
        equipContext.setText("Use");

        JPanel pan = new JPanel();
        rightTab.addTab("2", pan);
        rightTab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        rightTab.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        rightTab.setMaximumSize(new java.awt.Dimension(800, 500));
        rightTab.setMinimumSize(new java.awt.Dimension(500, 500));
        rightTab.setPreferredSize(new java.awt.Dimension(800, 500));

        PropertyChangeListener propertyChangeListener = new PropertyChangeListener ()
        {
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                if ( evt.getNewValue () == Boolean.TRUE )
                {
                    // Retrieving popup menu window (we won't find it if it is inside of parent frame)
                    final Window ancestor = getWindowAncestor ( shopPop );
                    if ( ancestor != null && ancestor.getClass ().getCanonicalName ().endsWith ( "HeavyWeightWindow" ) )
                    {
                        // Checking that parent window for our window is opaque, only then setting opacity
                        final Component parent = ancestor.getParent ();
                        if ( parent != null && parent instanceof Window && parent.getBackground ().getAlpha () == 0 )
                        {
                            // Making popup menu window non-opaque
                            ancestor.setBackground ( new Color ( 0, 0, 0, 0 ) );
                        }
                    }
                }
            }

            private Window getWindowAncestor ( Component component )
            {
                if ( component == null )
                {
                    return null;
                }
                if ( component instanceof Window )
                {
                    return ( Window ) component;
                }
                for ( Container p = component.getParent (); p != null; p = p.getParent () )
                {
                    if ( p instanceof Window )
                    {
                        return ( Window ) p;
                    }
                }
                return null;
            }
        };
        shopPop.addPropertyChangeListener ( "visible", propertyChangeListener );
        shopPop.setBackground(new java.awt.Color(102, 102, 102));
        shopPop.setForeground(new java.awt.Color(255, 255, 255));

        buyContext.setForeground(new java.awt.Color(255, 255, 0));
        buyContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/give.png"))); // NOI18N
        buyContext.setText("Purchase?");

        sellContext.setForeground(new java.awt.Color(255, 255, 0));
        sellContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gain.png"))); // NOI18N
        sellContext.setText("Barter?");

        unequipContext.setBackground(new java.awt.Color(255, 0, 0));
        unequipContext.setForeground(new java.awt.Color(255, 204, 204));
        unequipContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/unequip.png"))); // NOI18N
        unequipContext.setText("Remove");

        unequipContext.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Component invoker = equipPop.getInvoker();
                var cell = (CellPane) invoker;
                var coordinates = hero.inventory.checkFreeSlots();

                int index = 0;
                switch (cell.getType()) {
                    case NECK ->
                    index = 0;
                    case HEAD ->
                    index = 1;
                    case WEAPON ->
                    index = 2;
                    case CHEST ->
                    index = 3;
                    case OFFHAND ->
                    index = 4;
                    case LOWER ->
                    index = 5;
                    case FEET ->
                    index = 6;
                    case HANDS ->
                    index = 7;
                    default ->
                    index = 0;
                }
                var item = hero.retrieveEquipment(index);
                if(coordinates == null || item == null){
                    System.out.println("It cannot be removed");
                    return;
                }

                hero.inventory.store(item, coordinates.get("row"), coordinates.get("column"), item.getAsset());
                hero.unequipItem(index);
                cell.setInnerImage(null);
                hero.balanceStatus();
                System.out.println("The item was unequipped");
            }
        });
        equipPop.add(unequipContext);
        equipPop.setBackground(new java.awt.Color(102, 102, 102));
        equipPop.setForeground(new java.awt.Color(255, 255, 255));

        button_AddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_normal.png"))); // NOI18N
        button_AddItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        button_AddItem.setIconTextGap(0);
        button_AddItem.setMargin(new java.awt.Insets(2, 2, 2, 2));
        button_AddItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        button_AddItem.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_highlight.png"))); // NOI18N
        button_AddItem.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        button_AddItem.setOpaque(false);
        button_AddItem.setContentAreaFilled(false);
        button_AddItem.setBorderPainted(false);
        button_AddItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        //try {
            //    Image img = ImageIO.read(getClass().getResource("/images/btn_accept.png"));
            //    //button_AddItem.setMargin(new Insets(0, 0, 0, 0));
            //    button_AddItem.setIcon(new ImageIcon(img));
            //  } catch (Exception ex) {
            //    System.out.println(ex);
            //}
        button_AddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_AddItemActionPerformed(evt);
            }
        });

        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setText("August Florese (Copyright 2021)");

        emptyToggle1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle1.setEnabled(false);
        emptyToggle1.setFocusPainted(false);
        emptyToggle1.setFocusable(false);
        emptyToggle1.setIconTextGap(0);
        emptyToggle1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        emptyToggle1.setMaximumSize(new java.awt.Dimension(35, 35));
        emptyToggle1.setMinimumSize(new java.awt.Dimension(35, 35));
        emptyToggle1.setPreferredSize(new java.awt.Dimension(35, 35));
        emptyToggle1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyToggle1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipment Framework");
        setBackground(java.awt.Color.white);
        setName("dashboardFrame"); // NOI18N
        setSize(new java.awt.Dimension(1200, 760));

        wrappingPanel.setBackground(new java.awt.Color(102, 102, 102));
        wrappingPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        wrappingPanel.setForeground(new java.awt.Color(255, 255, 255));
        wrappingPanel.setLayout(new java.awt.BorderLayout());

        menuPanel.setBackground(new java.awt.Color(71, 71, 71));
        menuPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        menuPanel.setForeground(new java.awt.Color(255, 255, 255));
        menuPanel.setMaximumSize(new java.awt.Dimension(1400, 130));
        menuPanel.setMinimumSize(new java.awt.Dimension(1400, 130));
        menuPanel.setPreferredSize(new java.awt.Dimension(1400, 130));
        menuPanel.setRequestFocusEnabled(false);

        headerPanel.setMaximumSize(new java.awt.Dimension(1400, 150));
        headerPanel.setMinimumSize(new java.awt.Dimension(1400, 150));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jLayeredPane2.setLayout(new java.awt.BorderLayout());

        orbHolder.setBackground(new java.awt.Color(204, 0, 0));
        orbHolder.setOpaque(false);

        try {
            BufferedImage img = null;
            img = ImageIO.read(getClass().getResource("/images/health_100.png"));
            orb = new ZoomPanel(img,"no control");
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        orb.setOpaque(false);
        orb.setPreferredSize(new java.awt.Dimension(230, 120));

        javax.swing.GroupLayout orbLayout = new javax.swing.GroupLayout(orb);
        orb.setLayout(orbLayout);
        orbLayout.setHorizontalGroup(
            orbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );
        orbLayout.setVerticalGroup(
            orbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 131, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout orbHolderLayout = new javax.swing.GroupLayout(orbHolder);
        orbHolder.setLayout(orbHolderLayout);
        orbHolderLayout.setHorizontalGroup(
            orbHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orbHolderLayout.createSequentialGroup()
                .addComponent(orb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1170, Short.MAX_VALUE))
        );
        orbHolderLayout.setVerticalGroup(
            orbHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orbHolderLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(orb, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane2.setLayer(orbHolder, javax.swing.JLayeredPane.POPUP_LAYER);
        jLayeredPane2.add(orbHolder, java.awt.BorderLayout.SOUTH);

        headerHolder.setOpaque(false);
        headerHolder.setLayout(new java.awt.BorderLayout());

        try {
            BufferedImage img = null;
            img = ImageIO.read(getClass().getResource("/images/header2.png"));
            header = new ZoomPanel(img,"no control");
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        header.setEnabled(false);
        header.setFocusable(false);
        header.setMinimumSize(new java.awt.Dimension(1400, 130));
        header.setPreferredSize(new java.awt.Dimension(1400, 130));
        header.setRequestFocusEnabled(false);
        header.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        headerHolder.add(header, java.awt.BorderLayout.CENTER);

        jLayeredPane2.add(headerHolder, java.awt.BorderLayout.PAGE_START);

        headerPanel.add(jLayeredPane2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        wrappingPanel.add(menuPanel, java.awt.BorderLayout.PAGE_START);

        workHolder.setLayout(new java.awt.BorderLayout());

        scroll_Logs.setBackground(new java.awt.Color(102, 102, 102));

        textarea_status.setEditable(false);
        textarea_status.setBackground(new java.awt.Color(0, 0, 0));
        textarea_status.setColumns(20);
        textarea_status.setForeground(new java.awt.Color(255, 255, 204));
        textarea_status.setRows(2);
        textarea_status.setText("Welcome");
        textarea_status.setText("Welcome to Sanctuary " + hero.getName());
        textarea_status.setToolTipText("");
        textarea_status.setWrapStyleWord(true);
        textarea_status.setBorder(null);
        textarea_status.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scroll_Logs.setViewportView(textarea_status);
        try {
            InputStream is = this.getClass().getResourceAsStream("/font/exocet.ttf");
            Font font_txtarea = Font.createFont(Font.TRUETYPE_FONT, is);
            Font sizedFont_txtarea = font_txtarea.deriveFont(18f);
            textarea_status.setFont(sizedFont_txtarea);
        } catch (FontFormatException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageConsole mc = new MessageConsole(textarea_status);
        mc.redirectOut(null, System.out);
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);

        workHolder.add(scroll_Logs, java.awt.BorderLayout.NORTH);
        scroll_Logs.getAccessibleContext().setAccessibleName("");

        workPanel.setBackground(new java.awt.Color(102, 102, 102));
        workPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        workPanel.setForeground(new java.awt.Color(255, 255, 255));
        workPanel.setOpaque(false);
        workPanel.setLayout(new java.awt.BorderLayout());

        scroll_WorkArea.setBackground(new java.awt.Color(102, 102, 102));
        scroll_WorkArea.setMaximumSize(new java.awt.Dimension(32767, 400));
        scroll_WorkArea.setOpaque(false);
        scroll_WorkArea.setPreferredSize(new java.awt.Dimension(450, 500));

        splitPanel_WorkArea.setBackground(new java.awt.Color(102, 102, 102));
        splitPanel_WorkArea.setResizeWeight(0.5);
        splitPanel_WorkArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        splitPanel_WorkArea.setMinimumSize(new java.awt.Dimension(500, 500));
        splitPanel_WorkArea.setPreferredSize(new java.awt.Dimension(800, 500));

        leftSplit.setBackground(new java.awt.Color(102, 102, 102));
        leftSplit.setMinimumSize(new java.awt.Dimension(550, 550));
        leftSplit.setPreferredSize(new java.awt.Dimension(650, 345));
        leftSplit.setLayout(new java.awt.BorderLayout());

        leftCards.setBackground(new java.awt.Color(102, 102, 102));
        leftCards.setLayout(new java.awt.CardLayout());

        //try {
            //    BufferedImage inventoryImg = ImageIO.read(getClass().getResource("/images/golein.png"));
            //    worldPanel = new ZoomPanel(inventoryImg);
            //} catch (IOException ex) {
            //    Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
            //}
        worldCard.setBackground(new java.awt.Color(0, 0, 0));
        worldCard.setMaximumSize(new java.awt.Dimension(800, 500));
        worldCard.setMinimumSize(new java.awt.Dimension(800, 500));
        worldCard.setLayout(new java.awt.GridLayout(1, 0));

        jLayeredPane1.setLayout(new java.awt.BorderLayout());

        try {
            BufferedImage inventoryImg = ImageIO.read(getClass().getResource("/images/golein.png"));
            worldPanel = new ZoomPanel(inventoryImg);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        worldPanel.setBackground(new java.awt.Color(0, 0, 0));
        worldPanel.setMaximumSize(new java.awt.Dimension(800, 500));
        worldPanel.setMinimumSize(new java.awt.Dimension(800, 500));
        worldPanel.setLayout(new java.awt.BorderLayout());
        jLayeredPane1.add(worldPanel, java.awt.BorderLayout.CENTER);

        worldCard.add(jLayeredPane1);

        leftCards.add(worldCard, "world");

        BufferedImage inv_img = null;
        try {
            inv_img = ImageIO.read(getClass().getResource("/images/panel_bg.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        inventoryCard = new ImagePanel(inv_img);
        inventoryCard.setLayout(new java.awt.GridBagLayout());
        inventoryCard.setMaximumSize(new Dimension(800, 800));
        inventoryCard.setMinimumSize(new Dimension(500, 500));
        inventoryCard.setBackground(new java.awt.Color(102, 102, 102));
        inventoryCard.setMaximumSize(new java.awt.Dimension(300, 300));
        inventoryCard.setPreferredSize(new java.awt.Dimension(300, 300));
        inventoryCard.setLayout(new java.awt.GridBagLayout());
        inventoryCard.add(lblLeftSplit, new java.awt.GridBagConstraints());

        leftCards.add(inventoryCard, "inventory");

        mapCard.setBackground(new java.awt.Color(0, 0, 0));
        mapCard.setMaximumSize(new java.awt.Dimension(800, 500));
        mapCard.setLayout(new java.awt.BorderLayout());

        try {
            BufferedImage mapImg = ImageIO.read(getClass().getResource("/images/map.png"));
            mapPanel = new ZoomPanel(mapImg);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        mapPanel.setBackground(new java.awt.Color(0, 0, 0));
        mapPanel.setMaximumSize(new java.awt.Dimension(800, 500));
        mapPanel.setMinimumSize(new java.awt.Dimension(800, 500));
        mapPanel.setLayout(new java.awt.BorderLayout());
        mapCard.add(mapPanel, java.awt.BorderLayout.CENTER);

        leftCards.add(mapCard, "map");

        leftSplit.add(leftCards, java.awt.BorderLayout.CENTER);

        hotBar.setAlignmentX(0.0F);
        hotBar.setAlignmentY(0.0F);
        hotBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        hotBar.setMaximumSize(new java.awt.Dimension(32767, 50));
        hotBar.setMinimumSize(new java.awt.Dimension(0, 50));
        hotBar.setPreferredSize(new java.awt.Dimension(346, 45));
        hotBar.add(spacer);

        inventoryToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_inv_normal_closed.png"))); // NOI18N
        inventoryToggle.setToolTipText("Inventory");
        inventoryToggle.setFocusPainted(false);
        inventoryToggle.setFocusable(false);
        inventoryToggle.setIconTextGap(0);
        inventoryToggle.setMargin(new java.awt.Insets(2, 2, 2, 2));
        inventoryToggle.setMaximumSize(new java.awt.Dimension(35, 35));
        inventoryToggle.setMinimumSize(new java.awt.Dimension(35, 35));
        inventoryToggle.setPreferredSize(new java.awt.Dimension(35, 35));
        inventoryToggle.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_inv_highlight_closed.png"))); // NOI18N
        inventoryToggle.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_inv_highlight_open.png"))); // NOI18N
        inventoryToggle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_inv_normal_open.png"))); // NOI18N
        inventoryToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryToggleActionPerformed(evt);
            }
        });
        hotBar.add(inventoryToggle);

        btnMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_map_normal.png"))); // NOI18N
        btnMap.setToolTipText("World Map");
        btnMap.setFocusPainted(false);
        btnMap.setFocusable(false);
        btnMap.setMaximumSize(new java.awt.Dimension(35, 35));
        btnMap.setMinimumSize(new java.awt.Dimension(35, 35));
        btnMap.setPreferredSize(new java.awt.Dimension(35, 35));
        btnMap.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_map_highlight.png"))); // NOI18N
        btnMap.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_map_pressed.png"))); // NOI18N
        btnMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMapActionPerformed(evt);
            }
        });
        hotBar.add(btnMap);

        equipToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_equip_normal_closed.png"))); // NOI18N
        equipToggle.setToolTipText("Equipment");
        equipToggle.setFocusPainted(false);
        equipToggle.setFocusable(false);
        equipToggle.setIconTextGap(0);
        equipToggle.setMargin(new java.awt.Insets(2, 2, 2, 2));
        equipToggle.setMaximumSize(new java.awt.Dimension(35, 35));
        equipToggle.setMinimumSize(new java.awt.Dimension(35, 35));
        equipToggle.setPreferredSize(new java.awt.Dimension(35, 35));
        equipToggle.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_equip_highlight_closed.png"))); // NOI18N
        equipToggle.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_equip_normal_open.png"))); // NOI18N
        equipToggle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_equip_normal_open.png"))); // NOI18N
        equipToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipToggleActionPerformed(evt);
            }
        });
        hotBar.add(equipToggle);

        shopToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_normal_closed.png"))); // NOI18N
        shopToggle.setToolTipText("Shop");
        shopToggle.setFocusPainted(false);
        shopToggle.setFocusable(false);
        shopToggle.setIconTextGap(0);
        shopToggle.setMargin(new java.awt.Insets(2, 2, 2, 2));
        shopToggle.setMaximumSize(new java.awt.Dimension(35, 35));
        shopToggle.setMinimumSize(new java.awt.Dimension(35, 35));
        shopToggle.setPreferredSize(new java.awt.Dimension(35, 35));
        shopToggle.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_highlight_closed.png"))); // NOI18N
        shopToggle.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_highlight_open2.png"))); // NOI18N
        shopToggle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_normal_open2.png"))); // NOI18N
        shopToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopToggleActionPerformed(evt);
            }
        });
        hotBar.add(shopToggle);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_save_normal.png"))); // NOI18N
        btnSave.setToolTipText("Save Game");
        btnSave.setFocusPainted(false);
        btnSave.setFocusable(false);
        btnSave.setMaximumSize(new java.awt.Dimension(35, 35));
        btnSave.setMinimumSize(new java.awt.Dimension(35, 35));
        btnSave.setPreferredSize(new java.awt.Dimension(35, 35));
        btnSave.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_save_highlight.png"))); // NOI18N
        btnSave.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_save_pressed.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        hotBar.add(btnSave);

        btnHeal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_heal_normal.png"))); // NOI18N
        btnHeal.setToolTipText("Rejuvenate: +10 HP");
        btnHeal.setMaximumSize(new java.awt.Dimension(35, 35));
        btnHeal.setMinimumSize(new java.awt.Dimension(35, 35));
        btnHeal.setPreferredSize(new java.awt.Dimension(35, 35));
        btnHeal.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_heal_highlight.png"))); // NOI18N
        btnHeal.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_heal_pressed.png"))); // NOI18N
        btnHeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHealActionPerformed(evt);
            }
        });
        hotBar.add(btnHeal);

        btnPoison.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_poison_normal.png"))); // NOI18N
        btnPoison.setToolTipText("Poison Nova: -10HP");
        btnPoison.setMaximumSize(new java.awt.Dimension(35, 35));
        btnPoison.setMinimumSize(new java.awt.Dimension(35, 35));
        btnPoison.setPreferredSize(new java.awt.Dimension(35, 35));
        btnPoison.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_poison_highlight.png"))); // NOI18N
        btnPoison.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_poison_pressed.png"))); // NOI18N
        btnPoison.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPoisonActionPerformed(evt);
            }
        });
        hotBar.add(btnPoison);

        coinStatus.setLayout(new javax.swing.BoxLayout(coinStatus, javax.swing.BoxLayout.LINE_AXIS));

        coinIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coin.png"))); // NOI18N
        coinIcon.setToolTipText("Gold in Bag");
        coinIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        coinIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                coinIconMouseClicked(evt);
            }
        });
        coinStatus.add(coinIcon);

        coinHolder.setBackground(new java.awt.Color(0, 0, 0));
        coinHolder.setMinimumSize(new java.awt.Dimension(80, 32));

        coinField.setBackground(new java.awt.Color(0, 0, 0));
        coinField.setForeground(new java.awt.Color(255, 255, 204));
        coinField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        coinField.setText("9999");
        coinField.setToolTipText("Gold in Bag");
        coinField.setFocusable(false);
        coinField.setRequestFocusEnabled(false);

        javax.swing.GroupLayout coinHolderLayout = new javax.swing.GroupLayout(coinHolder);
        coinHolder.setLayout(coinHolderLayout);
        coinHolderLayout.setHorizontalGroup(
            coinHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
            .addGroup(coinHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coinHolderLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(coinField)
                    .addContainerGap()))
        );
        coinHolderLayout.setVerticalGroup(
            coinHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(coinHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coinHolderLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(coinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        coinStatus.add(coinHolder);

        hotBar.add(coinStatus);
        coinStatus.setLayout(new java.awt.GridBagLayout());

        healthStatus.setLayout(new javax.swing.BoxLayout(healthStatus, javax.swing.BoxLayout.LINE_AXIS));

        healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_small_100.png"))); // NOI18N
        healthIcon.setToolTipText("Total Health");
        healthIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        healthIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                healthIconMouseClicked(evt);
            }
        });
        healthStatus.add(healthIcon);

        healthHolder.setBackground(new java.awt.Color(0, 0, 0));
        healthHolder.setMinimumSize(new java.awt.Dimension(80, 32));

        healthField.setBackground(new java.awt.Color(0, 0, 0));
        healthField.setForeground(new java.awt.Color(255, 0, 0));
        healthField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        healthField.setText("100");
        healthField.setToolTipText("Total Health");
        healthField.setFocusable(false);
        healthField.setRequestFocusEnabled(false);

        javax.swing.GroupLayout healthHolderLayout = new javax.swing.GroupLayout(healthHolder);
        healthHolder.setLayout(healthHolderLayout);
        healthHolderLayout.setHorizontalGroup(
            healthHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
            .addGroup(healthHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, healthHolderLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(healthField)
                    .addContainerGap()))
        );
        healthHolderLayout.setVerticalGroup(
            healthHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(healthHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, healthHolderLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(healthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        healthStatus.add(healthHolder);

        hotBar.add(healthStatus);
        coinStatus.setLayout(new java.awt.GridBagLayout());
        hotBar.add(spacer1);

        leftSplit.add(hotBar, java.awt.BorderLayout.NORTH);

        splitPanel_WorkArea.setLeftComponent(leftSplit);

        rightSplit.setMaximumSize(new java.awt.Dimension(800, 500));
        rightSplit.setMinimumSize(new java.awt.Dimension(500, 500));
        rightSplit.setPreferredSize(new java.awt.Dimension(800, 500));
        rightSplit.setBackground(new java.awt.Color(153, 153, 153));
        rightSplit.setAlignmentX(0.0F);
        rightSplit.setAlignmentY(0.0F);
        rightSplit.setMaximumSize(new java.awt.Dimension(700, 500));
        rightSplit.setMinimumSize(new java.awt.Dimension(500, 500));
        rightSplit.setPreferredSize(new java.awt.Dimension(700, 500));
        rightSplit.setLayout(new java.awt.BorderLayout());

        rightCards.setBackground(new java.awt.Color(153, 153, 153));
        rightCards.setAlignmentX(0.0F);
        rightCards.setAlignmentY(0.0F);
        rightCards.setMaximumSize(new java.awt.Dimension(700, 500));
        rightCards.setMinimumSize(new java.awt.Dimension(500, 500));
        rightCards.setPreferredSize(new java.awt.Dimension(700, 500));
        //rightSplit = new JPanel(new CardLayout());
        //CardLayout card= new CardLayout();
        //rightSplit.setLayout(card);
        //rightSplit.add(equipmentCard,"equipment");
        //rightSplit.add(statusCard,"status");
        rightCards.setLayout(new java.awt.CardLayout());

        statusCard.setBackground(new java.awt.Color(102, 102, 102));
        statusCard.setMaximumSize(new java.awt.Dimension(800, 500));
        statusCard.setMinimumSize(new java.awt.Dimension(800, 500));
        statusCard.setPreferredSize(new java.awt.Dimension(800, 500));
        BufferedImage inventoryBG = null, slotBG = null;
        try {
            inventoryBG = ImageIO.read(getClass().getResource("/images/panel_bg.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        equipmentCard = new ImagePanel(inventoryBG);
        equipmentCard.setLayout(new java.awt.GridBagLayout());
        statusCard.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        try {
            BufferedImage statusImg = ImageIO.read(getClass().getResource("/images/status_panel.png"));
            detailsPanel = new ImagePanel(statusImg);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        detailsPanel.setMinimumSize(new java.awt.Dimension(240, 508));
        detailsPanel.setPreferredSize(new java.awt.Dimension(240, 508));
        detailsPanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );

        jPanel2.add(detailsPanel);

        try {
            BufferedImage detailsBackgroundImg = ImageIO.read(getClass().getResource("/images/status_panel_dark.png"));
            detailsBackground = new ImagePanel(detailsBackgroundImg);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        detailsBackground.setLayout(new java.awt.GridBagLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/status_strength.png"))); // NOI18N
        jLabel3.setToolTipText("");
        jLabel3.setPreferredSize(new java.awt.Dimension(94, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        detailsBackground.add(jLabel3, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/status_agility.png"))); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(94, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        detailsBackground.add(jLabel4, gridBagConstraints);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/status_intelligence.png"))); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(94, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        detailsBackground.add(jLabel5, gridBagConstraints);

        btnAddStr.setOpaque(false);
        btnAddStr.setContentAreaFilled(false);
        btnAddStr.setBorderPainted(false);
        btnAddStr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_normal.png"))); // NOI18N
        btnAddStr.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnAddStr.setIconTextGap(0);
        btnAddStr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAddStr.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_pressed.png"))); // NOI18N
        btnAddStr.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_highlight.png"))); // NOI18N
        btnAddStr.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        btnAddStr.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        //try {
            //    Image img = ImageIO.read(getClass().getResource("/images/btn_accept.png"));
            //    //button_AddItem.setMargin(new Insets(0, 0, 0, 0));
            //    button_AddItem.setIcon(new ImageIcon(img));
            //  } catch (Exception ex) {
            //    System.out.println(ex);
            //}
        btnAddStr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 45);
        detailsBackground.add(btnAddStr, gridBagConstraints);

        btnAddAgi.setOpaque(false);
        btnAddAgi.setContentAreaFilled(false);
        btnAddAgi.setBorderPainted(false);
        btnAddAgi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_normal.png"))); // NOI18N
        btnAddAgi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnAddAgi.setIconTextGap(0);
        btnAddAgi.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAddAgi.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_pressed.png"))); // NOI18N
        btnAddAgi.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_highlight.png"))); // NOI18N
        btnAddAgi.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        btnAddAgi.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        //try {
            //    Image img = ImageIO.read(getClass().getResource("/images/btn_accept.png"));
            //    //button_AddItem.setMargin(new Insets(0, 0, 0, 0));
            //    button_AddItem.setIcon(new ImageIcon(img));
            //  } catch (Exception ex) {
            //    System.out.println(ex);
            //}
        btnAddAgi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAgiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 45);
        detailsBackground.add(btnAddAgi, gridBagConstraints);

        btnAddInt.setOpaque(false);
        btnAddInt.setContentAreaFilled(false);
        btnAddInt.setBorderPainted(false);
        btnAddInt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_normal.png"))); // NOI18N
        btnAddInt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnAddInt.setIconTextGap(0);
        btnAddInt.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAddInt.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_pressed.png"))); // NOI18N
        btnAddInt.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_add_highlight.png"))); // NOI18N
        btnAddInt.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        btnAddInt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_accept_pressed.png"))); // NOI18N
        //try {
            //    Image img = ImageIO.read(getClass().getResource("/images/btn_accept.png"));
            //    //button_AddItem.setMargin(new Insets(0, 0, 0, 0));
            //    button_AddItem.setIcon(new ImageIcon(img));
            //  } catch (Exception ex) {
            //    System.out.println(ex);
            //}
        btnAddInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddIntActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 45);
        detailsBackground.add(btnAddInt, gridBagConstraints);

        lblInt.setForeground(new java.awt.Color(255, 255, 204));
        lblInt.setText("            ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 26);
        detailsBackground.add(lblInt, gridBagConstraints);

        lblAgi.setForeground(new java.awt.Color(255, 255, 204));
        lblAgi.setText("            ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 26);
        detailsBackground.add(lblAgi, gridBagConstraints);

        lblStr.setForeground(new java.awt.Color(255, 255, 204));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 26);
        detailsBackground.add(lblStr, gridBagConstraints);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("     Agility");
        jLabel9.setMaximumSize(new java.awt.Dimension(36757, 36757));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        detailsBackground.add(jLabel9, gridBagConstraints);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Intelligence");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        detailsBackground.add(jLabel10, gridBagConstraints);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("    Strength");
        jLabel11.setMaximumSize(new java.awt.Dimension(36757, 36757));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        detailsBackground.add(jLabel11, gridBagConstraints);

        lblName.setForeground(new java.awt.Color(255, 255, 204));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        detailsBackground.add(lblName, gridBagConstraints);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/status_damage.png"))); // NOI18N
        jLabel6.setToolTipText("");
        jLabel6.setPreferredSize(new java.awt.Dimension(94, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        detailsBackground.add(jLabel6, gridBagConstraints);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/status_armor.png"))); // NOI18N
        jLabel7.setToolTipText("");
        jLabel7.setPreferredSize(new java.awt.Dimension(94, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        detailsBackground.add(jLabel7, gridBagConstraints);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("       Armor");
        jLabel12.setMaximumSize(new java.awt.Dimension(36757, 36757));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        detailsBackground.add(jLabel12, gridBagConstraints);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("      Damage");
        jLabel13.setMaximumSize(new java.awt.Dimension(36757, 36757));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        detailsBackground.add(jLabel13, gridBagConstraints);

        lblArmor.setForeground(new java.awt.Color(255, 255, 204));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 26);
        detailsBackground.add(lblArmor, gridBagConstraints);

        lblDamage.setForeground(new java.awt.Color(255, 255, 204));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 26);
        detailsBackground.add(lblDamage, gridBagConstraints);

        jPanel2.add(detailsBackground);

        statusCard.add(jPanel2, java.awt.BorderLayout.CENTER);

        equipmentCard.setMaximumSize(new Dimension(800, 800));
        equipmentCard.setMinimumSize(new Dimension(500, 500));

        rightCards.add(statusCard, "status");

        equipmentCard.setBackground(new java.awt.Color(102, 102, 102));
        equipmentCard.setAlignmentX(0.0F);
        equipmentCard.setAlignmentY(0.0F);
        equipmentCard.setMaximumSize(new java.awt.Dimension(700, 500));
        equipmentCard.setMinimumSize(new java.awt.Dimension(500, 500));
        equipmentCard.setPreferredSize(new java.awt.Dimension(700, 500));
        BufferedImage equipmentImg = null;
        try {
            equipmentImg = ImageIO.read(getClass().getResource("/images/equipment_panel.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        equipmentCard = new ImagePanel(equipmentImg);
        equipmentCard.setLayout(new java.awt.GridBagLayout());
        equipmentCard.setLayout(new java.awt.GridBagLayout());

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_weapon.png"));
            leftWeaponPanel = new CellPane(eq_img, hero, ItemType.WEAPON);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        leftWeaponPanel.setComponentPopupMenu(equipPop);
        leftWeaponPanel.setName("weapon outer"); // NOI18N
        leftWeaponPanel.setPreferredSize(new java.awt.Dimension(81, 74));
        leftWeaponPanel.setLayout(new java.awt.BorderLayout());

        leftWeapon = new ImagePanel();
        leftWeapon.setBackground(new java.awt.Color(51, 51, 51));
        leftWeapon.setName("weapon inner"); // NOI18N
        leftWeapon.setOpaque(false);
        leftWeapon.setPreferredSize(new java.awt.Dimension(69, 62));

        javax.swing.GroupLayout leftWeaponLayout = new javax.swing.GroupLayout(leftWeapon);
        leftWeapon.setLayout(leftWeaponLayout);
        leftWeaponLayout.setHorizontalGroup(
            leftWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        leftWeaponLayout.setVerticalGroup(
            leftWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        leftWeaponPanel.add(leftWeapon, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        equipmentCard.add(leftWeaponPanel, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_neck.png"));
            neckPanel = new CellPane(eq_img, hero, ItemType.NECK);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        neckPanel.setComponentPopupMenu(equipPop);
        neckPanel.setMinimumSize(new java.awt.Dimension(81, 74));
        neckPanel.setLayout(new java.awt.BorderLayout());

        neckArmor = new ImagePanel();
        neckArmor.setBackground(new java.awt.Color(51, 51, 51));
        neckArmor.setMinimumSize(new java.awt.Dimension(69, 62));
        neckArmor.setOpaque(false);

        javax.swing.GroupLayout neckArmorLayout = new javax.swing.GroupLayout(neckArmor);
        neckArmor.setLayout(neckArmorLayout);
        neckArmorLayout.setHorizontalGroup(
            neckArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );
        neckArmorLayout.setVerticalGroup(
            neckArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );

        neckPanel.add(neckArmor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        equipmentCard.add(neckPanel, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_head.png"));
            headPanel = new CellPane(eq_img, hero, ItemType.HEAD);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        headPanel.setComponentPopupMenu(equipPop);
        headPanel.setPreferredSize(new java.awt.Dimension(81, 74));
        headPanel.setLayout(new java.awt.BorderLayout());

        headArmor = new ImagePanel();
        headArmor.setBackground(new java.awt.Color(51, 51, 51));
        headArmor.setOpaque(false);
        headArmor.setPreferredSize(new java.awt.Dimension(69, 62));

        javax.swing.GroupLayout headArmorLayout = new javax.swing.GroupLayout(headArmor);
        headArmor.setLayout(headArmorLayout);
        headArmorLayout.setHorizontalGroup(
            headArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        headArmorLayout.setVerticalGroup(
            headArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        headPanel.add(headArmor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 1, 0);
        equipmentCard.add(headPanel, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_hand.png"));
            handPanel = new CellPane(eq_img, hero, ItemType.HANDS);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        handPanel.setComponentPopupMenu(equipPop);
        handPanel.setPreferredSize(new java.awt.Dimension(81, 74));
        handPanel.setLayout(new java.awt.BorderLayout());

        handArmor = new ImagePanel();
        handArmor.setBackground(new java.awt.Color(51, 51, 51));
        handArmor.setOpaque(false);
        handArmor.setPreferredSize(new java.awt.Dimension(69, 62));

        javax.swing.GroupLayout handArmorLayout = new javax.swing.GroupLayout(handArmor);
        handArmor.setLayout(handArmorLayout);
        handArmorLayout.setHorizontalGroup(
            handArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        handArmorLayout.setVerticalGroup(
            handArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        handPanel.add(handArmor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 4, 0);
        equipmentCard.add(handPanel, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_chest.png"));
            chestPanel = new CellPane(eq_img, hero, ItemType.CHEST);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        chestPanel.setComponentPopupMenu(equipPop);
        chestPanel.setPreferredSize(new java.awt.Dimension(81, 74));
        chestPanel.setLayout(new java.awt.BorderLayout());

        chestArmor = new ImagePanel();
        chestArmor.setBackground(new java.awt.Color(51, 51, 51));
        chestArmor.setOpaque(false);
        chestArmor.setPreferredSize(new java.awt.Dimension(69, 62));

        javax.swing.GroupLayout chestArmorLayout = new javax.swing.GroupLayout(chestArmor);
        chestArmor.setLayout(chestArmorLayout);
        chestArmorLayout.setHorizontalGroup(
            chestArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chestArmorLayout.setVerticalGroup(
            chestArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        chestPanel.add(chestArmor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        equipmentCard.add(chestPanel, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_off.png"));
            rightWeaponPanel = new CellPane(eq_img, hero, ItemType.OFFHAND);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        rightWeaponPanel.setComponentPopupMenu(equipPop);
        rightWeaponPanel.setPreferredSize(new java.awt.Dimension(81, 74));
        rightWeaponPanel.setLayout(new java.awt.BorderLayout());

        rightWeapon = new ImagePanel();
        rightWeapon.setBackground(new java.awt.Color(51, 51, 51));
        rightWeapon.setOpaque(false);
        rightWeapon.setPreferredSize(new java.awt.Dimension(69, 62));

        javax.swing.GroupLayout rightWeaponLayout = new javax.swing.GroupLayout(rightWeapon);
        rightWeapon.setLayout(rightWeaponLayout);
        rightWeaponLayout.setHorizontalGroup(
            rightWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        rightWeaponLayout.setVerticalGroup(
            rightWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        rightWeaponPanel.add(rightWeapon, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        equipmentCard.add(rightWeaponPanel, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_lower.png"));
            lowerPanel = new CellPane(eq_img, hero, ItemType.LOWER);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        lowerPanel.setComponentPopupMenu(equipPop);
        lowerPanel.setPreferredSize(new java.awt.Dimension(81, 74));
        lowerPanel.setLayout(new java.awt.BorderLayout());

        lowerArmor = new ImagePanel();
        lowerArmor.setBackground(new java.awt.Color(51, 51, 51));
        lowerArmor.setOpaque(false);
        lowerArmor.setPreferredSize(new java.awt.Dimension(69, 62));

        javax.swing.GroupLayout lowerArmorLayout = new javax.swing.GroupLayout(lowerArmor);
        lowerArmor.setLayout(lowerArmorLayout);
        lowerArmorLayout.setHorizontalGroup(
            lowerArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lowerArmorLayout.setVerticalGroup(
            lowerArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lowerPanel.add(lowerArmor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        equipmentCard.add(lowerPanel, gridBagConstraints);

        jLabel1.setMaximumSize(new java.awt.Dimension(81, 74));
        jLabel1.setMinimumSize(new java.awt.Dimension(81, 74));
        jLabel1.setPreferredSize(new java.awt.Dimension(81, 74));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        equipmentCard.add(jLabel1, gridBagConstraints);

        try {
            BufferedImage eq_img = null;
            eq_img = ImageIO.read(getClass().getResource("/images/slot_feet.png"));
            feetPanel = new CellPane(eq_img, hero, ItemType.FEET);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        feetPanel.setComponentPopupMenu(equipPop);
        feetPanel.setLayout(new java.awt.BorderLayout());

        feetArmor = new ImagePanel();
        feetArmor.setBackground(new java.awt.Color(51, 51, 51));
        feetArmor.setOpaque(false);

        javax.swing.GroupLayout feetArmorLayout = new javax.swing.GroupLayout(feetArmor);
        feetArmor.setLayout(feetArmorLayout);
        feetArmorLayout.setHorizontalGroup(
            feetArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        feetArmorLayout.setVerticalGroup(
            feetArmorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        feetPanel.add(feetArmor, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 3);
        equipmentCard.add(feetPanel, gridBagConstraints);

        jLabel2.setMaximumSize(new java.awt.Dimension(81, 74));
        jLabel2.setMinimumSize(new java.awt.Dimension(81, 74));
        jLabel2.setPreferredSize(new java.awt.Dimension(81, 74));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        equipmentCard.add(jLabel2, gridBagConstraints);

        equipmentCard.setMaximumSize(new Dimension(800, 800));
        equipmentCard.setMinimumSize(new Dimension(500, 500));

        rightCards.add(equipmentCard, "equipment");

        shopCard.setBackground(new java.awt.Color(102, 102, 102));
        shopCard.setAlignmentX(0.0F);
        shopCard.setAlignmentY(0.0F);
        shopCard.setMaximumSize(new java.awt.Dimension(700, 500));
        shopCard.setMinimumSize(new java.awt.Dimension(500, 500));
        shopCard.setPreferredSize(new java.awt.Dimension(700, 500));
        shopCard.setLayout(new java.awt.BorderLayout());

        shopPanel.setBackground(new java.awt.Color(102, 102, 102));
        shopPanel.setAlignmentX(0.0F);
        shopPanel.setAlignmentY(0.0F);
        BufferedImage shop_bg = null;
        try {
            shop_bg = ImageIO.read(getClass().getResource("/images/shop_bg_gray.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        shopPanel = new ImagePanel(shop_bg);
        shopPanel.setLayout(new java.awt.GridBagLayout());
        shopPanel.setMaximumSize(new java.awt.Dimension(700, 500));
        shopPanel.setMinimumSize(new java.awt.Dimension(500, 500));
        shopPanel.setPreferredSize(new java.awt.Dimension(700, 500));
        shopPanel.setLayout(new java.awt.GridBagLayout());

        shopPanel.setMaximumSize(new Dimension(800, 800));
        shopPanel.setMinimumSize(new Dimension(500, 500));

        shopCard.add(shopPanel, java.awt.BorderLayout.CENTER);

        shopBar.setAlignmentX(0.0F);
        shopBar.setAlignmentY(0.0F);
        shopBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        shopBar.setMaximumSize(new java.awt.Dimension(32767, 50));
        shopBar.setMinimumSize(new java.awt.Dimension(0, 50));
        shopBar.setPreferredSize(new java.awt.Dimension(346, 45));
        shopBar.add(spacer2);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_refresh_normal.png"))); // NOI18N
        btnRefresh.setToolTipText("Refresh shop items");
        btnRefresh.setFocusPainted(false);
        btnRefresh.setFocusable(false);
        btnRefresh.setIconTextGap(0);
        btnRefresh.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnRefresh.setMaximumSize(new java.awt.Dimension(35, 35));
        btnRefresh.setMinimumSize(new java.awt.Dimension(35, 35));
        btnRefresh.setPreferredSize(new java.awt.Dimension(35, 35));
        btnRefresh.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_refresh_highlight.png"))); // NOI18N
        btnRefresh.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_refresh_pressed.png"))); // NOI18N
        btnRefresh.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_refresh_pressed.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        shopBar.add(btnRefresh);

        coinStatus1.setLayout(new javax.swing.BoxLayout(coinStatus1, javax.swing.BoxLayout.LINE_AXIS));

        coinIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coin.png"))); // NOI18N
        coinIcon1.setToolTipText("Shop Earnings");
        coinIcon1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        coinStatus1.add(coinIcon1);

        coinHolder1.setBackground(new java.awt.Color(0, 0, 0));
        coinHolder1.setMinimumSize(new java.awt.Dimension(80, 32));

        shopCoinField.setBackground(new java.awt.Color(0, 0, 0));
        shopCoinField.setForeground(new java.awt.Color(255, 255, 204));
        shopCoinField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        shopCoinField.setText("0");
        shopCoinField.setToolTipText("Shop earnings");
        shopCoinField.setFocusable(false);
        shopCoinField.setRequestFocusEnabled(false);

        javax.swing.GroupLayout coinHolder1Layout = new javax.swing.GroupLayout(coinHolder1);
        coinHolder1.setLayout(coinHolder1Layout);
        coinHolder1Layout.setHorizontalGroup(
            coinHolder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
            .addGroup(coinHolder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coinHolder1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(shopCoinField)
                    .addContainerGap()))
        );
        coinHolder1Layout.setVerticalGroup(
            coinHolder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(coinHolder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coinHolder1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shopCoinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        coinStatus1.add(coinHolder1);

        shopBar.add(coinStatus1);
        coinStatus.setLayout(new java.awt.GridBagLayout());
        shopBar.add(spacer3);

        shopCard.add(shopBar, java.awt.BorderLayout.NORTH);

        shopPanel.setMaximumSize(new Dimension(800, 800));
        shopPanel.setMinimumSize(new Dimension(500, 500));

        rightCards.add(shopCard, "shop");

        rightSplit.add(rightCards, java.awt.BorderLayout.CENTER);

        splitPanel_WorkArea.setRightComponent(rightSplit);

        scroll_WorkArea.setViewportView(splitPanel_WorkArea);

        workPanel.add(scroll_WorkArea, java.awt.BorderLayout.PAGE_START);

        workHolder.add(workPanel, java.awt.BorderLayout.CENTER);

        wrappingPanel.add(workHolder, java.awt.BorderLayout.CENTER);

        credit.setBackground(new java.awt.Color(0, 0, 0));
        credit.setForeground(new java.awt.Color(255, 255, 204));
        credit.setText("Developed by: August Florese");
        wrappingPanel.add(credit, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wrappingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wrappingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_AddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_AddItemActionPerformed
        //AddItem("AXE", ItemType.WEAPON, "/images/wpn-swd.png", 1, 1, 1);
        hero.balanceStatus();
    }//GEN-LAST:event_button_AddItemActionPerformed

    private void inventoryToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryToggleActionPerformed

        CardLayout card = (CardLayout) leftCards.getLayout();
        if (!inventoryToggle.isSelected()) {
            jukeBox.Play(Song.INVENTORY_CLOSE);
            card.show(leftCards, "world");
            worldCard.revalidate();
            worldCard.repaint();

        } else {
            jukeBox.Play(Song.INVENTORY);
            card.show(leftCards, "inventory");
            inventoryCard.revalidate();
            inventoryCard.repaint();
        }
        splitPanel_WorkArea.revalidate();
        splitPanel_WorkArea.repaint();
    }//GEN-LAST:event_inventoryToggleActionPerformed

    private void equipToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equipToggleActionPerformed

        CardLayout card = (CardLayout) rightCards.getLayout();
        if (shopToggle.isSelected()) {
            shopToggle.setSelected(false);
        }
        if (!equipToggle.isSelected()) {
            jukeBox.Play(Song.PAPER_CLOSE);
            card.show(rightCards, "status");
            statusCard.revalidate();
            statusCard.repaint();

        } else {
            jukeBox.Play(Song.PAPER);
            card.show(rightCards, "equipment");
            equipmentCard.revalidate();
            equipmentCard.repaint();
        }
        splitPanel_WorkArea.revalidate();
        splitPanel_WorkArea.repaint();
    }//GEN-LAST:event_equipToggleActionPerformed

    private void shopToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopToggleActionPerformed

        CardLayout card = (CardLayout) rightCards.getLayout();
        if (equipToggle.isSelected()) {
            equipToggle.setSelected(false);
        }
        if (!shopToggle.isSelected()) {
            jukeBox.Play(Song.SHOP_CLOSE);
            card.show(rightCards, "status");
            statusCard.revalidate();
            statusCard.repaint();

        } else {
            jukeBox.Play(Song.SHOP);
            card.show(rightCards, "shop");
            shopPanel.revalidate();
            shopPanel.repaint();
        }
    }//GEN-LAST:event_shopToggleActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        jukeBox.Play(Song.REFRESH);
        randomizeShop(compendium, 16, 0);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void inventoryPopPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_inventoryPopPopupMenuWillBecomeVisible
        if (shopToggle.isSelected()) {
            //discardContext.setText("Barter");
        }
    }//GEN-LAST:event_inventoryPopPopupMenuWillBecomeVisible

    private void coinIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_coinIconMouseClicked
        if ((hero.getGold() + 1000) < 10000) {
            hero.setGold(hero.getGold() + 1000);
            coinField.setText(Integer.toString(hero.getGold()));
        }

    }//GEN-LAST:event_coinIconMouseClicked

    private void healthIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_healthIconMouseClicked
        hero.setHealth(hero.getHealth() - 10);
        updateHealth(hero.getHealth());
    }//GEN-LAST:event_healthIconMouseClicked

    private void btnAddStrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStrActionPerformed
        jukeBox.Play(Song.SELECT);
        hero.setStrength(hero.getStrength() + 1);
        updateLabelMessage();
    }//GEN-LAST:event_btnAddStrActionPerformed

    private void btnAddAgiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAgiActionPerformed
        jukeBox.Play(Song.SELECT);
        hero.setAgility(hero.getAgility() + 1);
        updateLabelMessage();
    }//GEN-LAST:event_btnAddAgiActionPerformed

    private void btnAddIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddIntActionPerformed
        jukeBox.Play(Song.SELECT);
        hero.setIntelligence(hero.getIntelligence() + 1);
        updateLabelMessage();
    }//GEN-LAST:event_btnAddIntActionPerformed

    private void btnHealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHealActionPerformed
        jukeBox.Play(Song.HEAL);
        hero.setHealth(hero.getHealth() + 10);
        updateHealth(hero.getHealth());
    }//GEN-LAST:event_btnHealActionPerformed
    private boolean mapIsVisible;
    private void btnMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapActionPerformed
        CardLayout card = (CardLayout) leftCards.getLayout();
        if (inventoryToggle.isSelected()) {
            inventoryToggle.setSelected(false);
        }
        if (mapIsVisible) {
            card.show(leftCards, "world");
            worldCard.revalidate();
            worldCard.repaint();
            mapIsVisible = false;
        } else {
            jukeBox.Play(Song.PAPER);
            card.show(leftCards, "map");
            mapCard.revalidate();
            mapCard.repaint();
            mapIsVisible = true;
        }

    }//GEN-LAST:event_btnMapActionPerformed

    private void btnPoisonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPoisonActionPerformed
        jukeBox.Play(Song.SKILL);
        hero.setHealth(hero.getHealth() - 10);
        updateHealth(hero.getHealth());
    }//GEN-LAST:event_btnPoisonActionPerformed

    private void emptyToggle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyToggle1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emptyToggle1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //var save = hero;
        SaveManager saver = new SaveManager(
                hero.getName(),
                hero.getHealth(),
                hero.getGold(),
                hero.getInventory(),
                hero.getAgility(),
                hero.getStrength(),
                hero.getIntelligence(),
                hero.getEquipment()
        );
        String saveString = gson.toJson(saver);
        saver.saveDialog(saveString);
        System.out.println(saveString);
    }//GEN-LAST:event_btnSaveActionPerformed

//    public static void main(String args[]) {
//
//        java.awt.EventQueue.invokeLater(() -> {
//            new DashboardFrame(hero).setVisible(true);
//
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAgi;
    private javax.swing.JButton btnAddInt;
    private javax.swing.JButton btnAddStr;
    private javax.swing.JButton btnHeal;
    private javax.swing.JButton btnMap;
    private javax.swing.JButton btnPoison;
    private javax.swing.JToggleButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton button_AddItem;
    private javax.swing.JMenuItem buyContext;
    private javax.swing.JPanel chestArmor;
    private javax.swing.JPanel chestPanel;
    private javax.swing.JTextField coinField;
    private javax.swing.JPanel coinHolder;
    private javax.swing.JPanel coinHolder1;
    private javax.swing.JLabel coinIcon;
    private javax.swing.JLabel coinIcon1;
    private javax.swing.JPanel coinStatus;
    private javax.swing.JPanel coinStatus1;
    private javax.swing.JLabel credit;
    private javax.swing.JLabel credits;
    private javax.swing.JPanel detailsBackground;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JMenuItem discardContext;
    private javax.swing.JToggleButton emptyToggle1;
    private javax.swing.JMenuItem equipContext;
    private javax.swing.JPopupMenu equipPop;
    private javax.swing.JToggleButton equipToggle;
    private javax.swing.JPanel equipmentCard;
    private javax.swing.JPanel feetArmor;
    private javax.swing.JPanel feetPanel;
    private javax.swing.JPanel handArmor;
    private javax.swing.JPanel handPanel;
    private javax.swing.JPanel headArmor;
    private javax.swing.JPanel headPanel;
    private javax.swing.JPanel header;
    private javax.swing.JPanel headerHolder;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JTextField healthField;
    private javax.swing.JPanel healthHolder;
    private javax.swing.JLabel healthIcon;
    private javax.swing.JPanel healthStatus;
    private javax.swing.JPanel hotBar;
    private javax.swing.JPanel inventoryCard;
    private javax.swing.JPopupMenu inventoryPop;
    private javax.swing.JToggleButton inventoryToggle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAgi;
    private javax.swing.JLabel lblArmor;
    private javax.swing.JLabel lblDamage;
    private javax.swing.JLabel lblInt;
    private javax.swing.JLabel lblLeftSplit;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStr;
    private javax.swing.JPanel leftCards;
    private javax.swing.JPanel leftSplit;
    private javax.swing.JPanel leftWeapon;
    private javax.swing.JPanel leftWeaponPanel;
    private javax.swing.JPanel lowerArmor;
    private javax.swing.JPanel lowerPanel;
    private javax.swing.JPanel mapCard;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel neckArmor;
    private javax.swing.JPanel neckPanel;
    private javax.swing.JPanel orb;
    private javax.swing.JPanel orbHolder;
    private javax.swing.JPanel rightCards;
    private javax.swing.JPanel rightSplit;
    private javax.swing.JTabbedPane rightTab;
    private javax.swing.JPanel rightWeapon;
    private javax.swing.JPanel rightWeaponPanel;
    private javax.swing.JScrollPane scroll_Logs;
    private javax.swing.JScrollPane scroll_WorkArea;
    private javax.swing.JMenuItem sellContext;
    private javax.swing.JPanel shopBar;
    private javax.swing.JPanel shopCard;
    private javax.swing.JTextField shopCoinField;
    private javax.swing.JPanel shopPanel;
    private javax.swing.JPopupMenu shopPop;
    private javax.swing.JToggleButton shopToggle;
    private javax.swing.JLabel spacer;
    private javax.swing.JLabel spacer1;
    private javax.swing.JLabel spacer2;
    private javax.swing.JLabel spacer3;
    private javax.swing.JSplitPane splitPanel_WorkArea;
    private javax.swing.JPanel statusCard;
    private javax.swing.JTextArea textarea_status;
    private javax.swing.JMenuItem unequipContext;
    private javax.swing.JPanel workHolder;
    private javax.swing.JPanel workPanel;
    private javax.swing.JPanel worldCard;
    private javax.swing.JPanel worldPanel;
    private javax.swing.JPanel wrappingPanel;
    // End of variables declaration//GEN-END:variables
}

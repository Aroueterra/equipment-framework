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
import aroueterra.EquipmentFramework.utility.Compendium;
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
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
        this.shop = new Shop(0, new Inventory(5, 5), hero, shopCoinField, coinField);
        initComponents();
        BufferedImage shopslot_bg = null, slot_bg = null;
        try {
            shopslot_bg = ImageIO.read(getClass().getResource("/images/slot_paper.png"));
            slot_bg = ImageIO.read(getClass().getResource("/images/slot.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        CreateCells(slot_bg, inventoryCard, hero, 5);
        CreateCells(shopslot_bg, shopPanel, shop, 5);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }

    public void FixPopMenu() {

    }

    public void CreateCells(BufferedImage img, JPanel parent, Hero hero, int count) {
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
    public void CreateCells(BufferedImage img, JPanel parent, Shop shop, int count) {
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

    private void randomizeShop(Compendium[] compendium, int max, int min) {
        var rand = new Random();
        var slots = shop.inventory.checkFreeSlots();
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

    public void AddItem(Item item, Inventory inventory, Map<String, Integer> coordinates) {
        if (coordinates != null) {
            System.out.println("-----------------------");
            System.out.println(item.getName());
            System.out.println(item.getAsset());
            inventory.store(item, coordinates.get("row"), coordinates.get("column"), item.getAsset());
        } else {
            System.out.println("Item cannot be added: no free slots available.");
        }
    }

    public void AddItem(String itemName, ItemType itemType, String resource, int property1, int property2, int property3) {
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

        inventoryPop = new javax.swing.JPopupMenu();
        discardContext = new javax.swing.JMenuItem();
        equipContext = new javax.swing.JMenuItem();
        rightTab = new javax.swing.JTabbedPane();
        shopPop = new javax.swing.JPopupMenu();
        buyContext = new javax.swing.JMenuItem();
        sellContext = new javax.swing.JMenuItem();
        wrappingPanel = new javax.swing.JPanel();
        workPanel = new javax.swing.JPanel();
        scroll_Logs = new javax.swing.JScrollPane();
        textarea_status = new javax.swing.JTextArea();
        scroll_WorkArea = new javax.swing.JScrollPane();
        splitPanel_WorkArea = new javax.swing.JSplitPane();
        leftSplit = new javax.swing.JPanel();
        leftCards = new javax.swing.JPanel();
        worldCard = new javax.swing.JPanel();
        worldPanel = new javax.swing.JPanel();
        inventoryCard = new javax.swing.JPanel();
        lblLeftSplit = new javax.swing.JLabel();
        hotBar = new javax.swing.JPanel();
        spacer = new javax.swing.JLabel();
        inventoryToggle = new javax.swing.JToggleButton();
        equipToggle = new javax.swing.JToggleButton();
        shopToggle = new javax.swing.JToggleButton();
        emptyToggle = new javax.swing.JToggleButton();
        emptyToggle1 = new javax.swing.JToggleButton();
        emptyToggle2 = new javax.swing.JToggleButton();
        emptyToggle3 = new javax.swing.JToggleButton();
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
        equipmentCard = new javax.swing.JPanel();
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
        credits = new javax.swing.JLabel();
        menuPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cmb_AddEquip = new javax.swing.JComboBox<>();
        button_AddItem = new javax.swing.JButton();

        inventoryPop.setBackground(new java.awt.Color(102, 102, 102));
        inventoryPop.setForeground(new java.awt.Color(255, 255, 255));

        discardContext.setBackground(new java.awt.Color(255, 0, 0));
        discardContext.setForeground(new java.awt.Color(255, 204, 204));
        discardContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/discard.png"))); // NOI18N
        discardContext.setText("Discard");

        equipContext.setForeground(new java.awt.Color(255, 255, 255));
        equipContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/equip.png"))); // NOI18N
        equipContext.setText("Equip");

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

        sellContext.setForeground(new java.awt.Color(255, 255, 0));
        sellContext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gain.png"))); // NOI18N
        sellContext.setText("Barter?");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipment Framework");
        setBackground(java.awt.Color.white);
        setName("dashboardFrame"); // NOI18N
        setSize(new java.awt.Dimension(1200, 760));

        wrappingPanel.setBackground(new java.awt.Color(102, 102, 102));
        wrappingPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        wrappingPanel.setForeground(new java.awt.Color(255, 255, 255));

        workPanel.setBackground(new java.awt.Color(102, 102, 102));
        workPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        workPanel.setForeground(new java.awt.Color(255, 255, 255));
        workPanel.setOpaque(false);

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

        scroll_WorkArea.setBackground(new java.awt.Color(102, 102, 102));
        scroll_WorkArea.setMaximumSize(new java.awt.Dimension(32767, 400));
        scroll_WorkArea.setOpaque(false);
        scroll_WorkArea.setPreferredSize(new java.awt.Dimension(450, 464));

        splitPanel_WorkArea.setBackground(new java.awt.Color(102, 102, 102));
        splitPanel_WorkArea.setResizeWeight(0.5);
        splitPanel_WorkArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        splitPanel_WorkArea.setMinimumSize(new java.awt.Dimension(500, 500));
        splitPanel_WorkArea.setOneTouchExpandable(true);
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

        try {
            BufferedImage inventoryImg = ImageIO.read(getClass().getResource("/images/golein.png"));
            worldPanel = new ZoomPanel(inventoryImg);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        worldPanel.setBackground(new java.awt.Color(0, 0, 0));
        worldPanel.setMaximumSize(new java.awt.Dimension(800, 500));
        worldPanel.setMinimumSize(new java.awt.Dimension(800, 500));
        worldCard.add(worldPanel);

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

        leftSplit.add(leftCards, java.awt.BorderLayout.CENTER);

        hotBar.setAlignmentX(0.0F);
        hotBar.setAlignmentY(0.0F);
        hotBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        hotBar.setMaximumSize(new java.awt.Dimension(32767, 50));
        hotBar.setMinimumSize(new java.awt.Dimension(0, 50));
        hotBar.setPreferredSize(new java.awt.Dimension(346, 45));
        hotBar.add(spacer);

        inventoryToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_inv_normal_closed.png"))); // NOI18N
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

        equipToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_equip_normal_closed.png"))); // NOI18N
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
        shopToggle.setFocusPainted(false);
        shopToggle.setFocusable(false);
        shopToggle.setIconTextGap(0);
        shopToggle.setMargin(new java.awt.Insets(2, 2, 2, 2));
        shopToggle.setMaximumSize(new java.awt.Dimension(35, 35));
        shopToggle.setMinimumSize(new java.awt.Dimension(35, 35));
        shopToggle.setPreferredSize(new java.awt.Dimension(35, 35));
        shopToggle.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_highlight_closed.png"))); // NOI18N
        shopToggle.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_pressed_open.png"))); // NOI18N
        shopToggle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/btn_shop_normal_open.png"))); // NOI18N
        shopToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopToggleActionPerformed(evt);
            }
        });
        hotBar.add(shopToggle);

        emptyToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle.setEnabled(false);
        emptyToggle.setFocusPainted(false);
        emptyToggle.setFocusable(false);
        emptyToggle.setIconTextGap(0);
        emptyToggle.setMargin(new java.awt.Insets(2, 2, 2, 2));
        emptyToggle.setMaximumSize(new java.awt.Dimension(35, 35));
        emptyToggle.setMinimumSize(new java.awt.Dimension(35, 35));
        emptyToggle.setPreferredSize(new java.awt.Dimension(35, 35));
        emptyToggle.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyToggleActionPerformed(evt);
            }
        });
        hotBar.add(emptyToggle);

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
        hotBar.add(emptyToggle1);

        emptyToggle2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle2.setEnabled(false);
        emptyToggle2.setFocusPainted(false);
        emptyToggle2.setFocusable(false);
        emptyToggle2.setIconTextGap(0);
        emptyToggle2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        emptyToggle2.setMaximumSize(new java.awt.Dimension(35, 35));
        emptyToggle2.setMinimumSize(new java.awt.Dimension(35, 35));
        emptyToggle2.setPreferredSize(new java.awt.Dimension(35, 35));
        emptyToggle2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle2.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyToggle2ActionPerformed(evt);
            }
        });
        hotBar.add(emptyToggle2);

        emptyToggle3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle3.setEnabled(false);
        emptyToggle3.setFocusPainted(false);
        emptyToggle3.setFocusable(false);
        emptyToggle3.setIconTextGap(0);
        emptyToggle3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        emptyToggle3.setMaximumSize(new java.awt.Dimension(35, 35));
        emptyToggle3.setMinimumSize(new java.awt.Dimension(35, 35));
        emptyToggle3.setPreferredSize(new java.awt.Dimension(35, 35));
        emptyToggle3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle3.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cell_empty.png"))); // NOI18N
        emptyToggle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyToggle3ActionPerformed(evt);
            }
        });
        hotBar.add(emptyToggle3);

        coinStatus.setLayout(new javax.swing.BoxLayout(coinStatus, javax.swing.BoxLayout.LINE_AXIS));

        coinIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coin.png"))); // NOI18N
        coinIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        coinStatus.add(coinIcon);

        coinHolder.setBackground(new java.awt.Color(0, 0, 0));
        coinHolder.setMinimumSize(new java.awt.Dimension(80, 32));

        coinField.setBackground(new java.awt.Color(0, 0, 0));
        coinField.setForeground(new java.awt.Color(255, 255, 204));
        coinField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        coinField.setText("9999");
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

        healthIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/health_100.png"))); // NOI18N
        healthIcon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        healthStatus.add(healthIcon);

        healthHolder.setBackground(new java.awt.Color(0, 0, 0));
        healthHolder.setMinimumSize(new java.awt.Dimension(80, 32));

        healthField.setBackground(new java.awt.Color(0, 0, 0));
        healthField.setForeground(new java.awt.Color(255, 0, 0));
        healthField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        healthField.setText("100");
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

        javax.swing.GroupLayout statusCardLayout = new javax.swing.GroupLayout(statusCard);
        statusCard.setLayout(statusCardLayout);
        statusCardLayout.setHorizontalGroup(
            statusCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        statusCardLayout.setVerticalGroup(
            statusCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );

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
            equipmentImg = ImageIO.read(getClass().getResource("/images/panel_bg.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        equipmentCard = new ImagePanel(equipmentImg);
        equipmentCard.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout equipmentCardLayout = new javax.swing.GroupLayout(equipmentCard);
        equipmentCard.setLayout(equipmentCardLayout);
        equipmentCardLayout.setHorizontalGroup(
            equipmentCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );
        equipmentCardLayout.setVerticalGroup(
            equipmentCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );

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
        coinIcon1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        coinStatus1.add(coinIcon1);

        coinHolder1.setBackground(new java.awt.Color(0, 0, 0));
        coinHolder1.setMinimumSize(new java.awt.Dimension(80, 32));

        shopCoinField.setBackground(new java.awt.Color(0, 0, 0));
        shopCoinField.setForeground(new java.awt.Color(255, 255, 204));
        shopCoinField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        shopCoinField.setText("9999");
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

        javax.swing.GroupLayout workPanelLayout = new javax.swing.GroupLayout(workPanel);
        workPanel.setLayout(workPanelLayout);
        workPanelLayout.setHorizontalGroup(
            workPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(workPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll_WorkArea, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
                    .addComponent(scroll_Logs))
                .addContainerGap())
        );
        workPanelLayout.setVerticalGroup(
            workPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll_WorkArea, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(scroll_Logs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setText("August Florese (Copyright 2021)");

        menuPanel.setBackground(new java.awt.Color(71, 71, 71));
        menuPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        menuPanel.setForeground(new java.awt.Color(255, 255, 255));

        cmb_AddEquip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Equipment --", "Armor", "Weapon", "Shield" }));
        cmb_AddEquip.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cmb_AddEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_AddEquipActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_AddItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_AddEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cmb_AddEquip)
            .addComponent(button_AddItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout wrappingPanelLayout = new javax.swing.GroupLayout(wrappingPanel);
        wrappingPanel.setLayout(wrappingPanelLayout);
        wrappingPanelLayout.setHorizontalGroup(
            wrappingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrappingPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(wrappingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wrappingPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(wrappingPanelLayout.createSequentialGroup()
                        .addGroup(wrappingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(workPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(credits, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(1, 1, 1)))
                .addGap(1, 1, 1))
        );
        wrappingPanelLayout.setVerticalGroup(
            wrappingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrappingPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(workPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(credits)
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wrappingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wrappingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_AddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_AddItemActionPerformed
        AddItem("AXE", ItemType.WEAPON, "/images/wpn-swd.png", 1, 1, 1);
    }//GEN-LAST:event_button_AddItemActionPerformed

    private void cmb_AddEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_AddEquipActionPerformed
        if (cmb_AddEquip.getSelectedItem().toString() != "-- Select Equipment --") {
            System.out.println(cmb_AddEquip.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmb_AddEquipActionPerformed

    private void inventoryToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryToggleActionPerformed
        CardLayout card = (CardLayout) leftCards.getLayout();
        if (!inventoryToggle.isSelected()) {
            card.show(leftCards, "world");
            worldCard.revalidate();
            worldCard.repaint();

        } else {
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
            card.show(rightCards, "status");
            statusCard.revalidate();
            statusCard.repaint();

        } else {
            card.show(rightCards, "equipment");
            equipmentCard.revalidate();
            equipmentCard.repaint();
        }
        splitPanel_WorkArea.revalidate();
        splitPanel_WorkArea.repaint();
    }//GEN-LAST:event_equipToggleActionPerformed

    private void emptyToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyToggleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emptyToggleActionPerformed

    private void emptyToggle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyToggle1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emptyToggle1ActionPerformed

    private void emptyToggle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyToggle2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emptyToggle2ActionPerformed

    private void emptyToggle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyToggle3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emptyToggle3ActionPerformed

    private void shopToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopToggleActionPerformed
        CardLayout card = (CardLayout) rightCards.getLayout();
        if (equipToggle.isSelected()) {
            equipToggle.setSelected(false);
        }
        if (!shopToggle.isSelected()) {
            card.show(rightCards, "status");
            statusCard.revalidate();
            statusCard.repaint();

        } else {
            card.show(rightCards, "shop");
            shopPanel.revalidate();
            shopPanel.repaint();
        }
//        splitPanel_WorkArea.revalidate();
//        splitPanel_WorkArea.repaint();
    }//GEN-LAST:event_shopToggleActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        randomizeShop(compendium, 16, 0);
    }//GEN-LAST:event_btnRefreshActionPerformed

//    public static void main(String args[]) {
//
//        java.awt.EventQueue.invokeLater(() -> {
//            new DashboardFrame(hero).setVisible(true);
//
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnRefresh;
    private javax.swing.JButton button_AddItem;
    private javax.swing.JMenuItem buyContext;
    private javax.swing.JComboBox<String> cmb_AddEquip;
    private javax.swing.JTextField coinField;
    private javax.swing.JPanel coinHolder;
    private javax.swing.JPanel coinHolder1;
    private javax.swing.JLabel coinIcon;
    private javax.swing.JLabel coinIcon1;
    private javax.swing.JPanel coinStatus;
    private javax.swing.JPanel coinStatus1;
    private javax.swing.JLabel credits;
    private javax.swing.JMenuItem discardContext;
    private javax.swing.JToggleButton emptyToggle;
    private javax.swing.JToggleButton emptyToggle1;
    private javax.swing.JToggleButton emptyToggle2;
    private javax.swing.JToggleButton emptyToggle3;
    private javax.swing.JMenuItem equipContext;
    private javax.swing.JToggleButton equipToggle;
    private javax.swing.JPanel equipmentCard;
    private javax.swing.JTextField healthField;
    private javax.swing.JPanel healthHolder;
    private javax.swing.JLabel healthIcon;
    private javax.swing.JPanel healthStatus;
    private javax.swing.JPanel hotBar;
    private javax.swing.JPanel inventoryCard;
    private javax.swing.JPopupMenu inventoryPop;
    private javax.swing.JToggleButton inventoryToggle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLeftSplit;
    private javax.swing.JPanel leftCards;
    private javax.swing.JPanel leftSplit;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel rightCards;
    private javax.swing.JPanel rightSplit;
    private javax.swing.JTabbedPane rightTab;
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
    private javax.swing.JPanel workPanel;
    private javax.swing.JPanel worldCard;
    private javax.swing.JPanel worldPanel;
    private javax.swing.JPanel wrappingPanel;
    // End of variables declaration//GEN-END:variables
}

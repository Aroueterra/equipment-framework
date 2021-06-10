/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI;

import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import aroueterra.EquipmentFramework.UI.custom.BackgroundPanel;
import aroueterra.EquipmentFramework.UI.custom.CellPane;
import aroueterra.EquipmentFramework.UI.custom.MessageConsole;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.UI.inventory.Item;
import aroueterra.EquipmentFramework.player.Hero;
import static aroueterra.EquipmentFramework.UI.inventory.ItemType.*;
import static aroueterra.EquipmentFramework.UI.inventory.PropertyType.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 * https://stackoverflow.com/questions/15421708/how-to-draw-grid-using-swing-class-java-and-detect-mouse-position-when-click-and
 *
 * @author aroue
 */
public class DashboardFrame extends javax.swing.JFrame {

    Hero hero;
    static boolean toggled = false;

    public DashboardFrame(Hero hero) {
        initComponents();
        this.hero = hero;
        BufferedImage inventoryImg = null, slotImg = null;
        try {
            inventoryImg = ImageIO.read(getClass().getResource("/images/panel_bg.png"));
            slotImg = ImageIO.read(getClass().getResource("/images/slot.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        JPanel leftPanel = new ImagePanel(inventoryImg);
        leftSplit = leftPanel;
        leftSplit.setLayout(new java.awt.GridBagLayout());
        leftSplit.setMaximumSize(new Dimension(800, 800));
        leftSplit.setMinimumSize(new Dimension(500, 500));
        CreateCells(slotImg, leftSplit);
//        JPanel rightPanel = new ImagePanel(inventoryBG);
//        rightSplit = rightPanel;
        equipmentPanel.setLayout(new java.awt.GridBagLayout());
        equipmentPanel.setMaximumSize(new Dimension(800, 800));
        equipmentPanel.setMinimumSize(new Dimension(500, 500));
//        splitPanel_WorkArea.setRightComponent(rightSplit);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }

    public void CreateCells(BufferedImage imgSlot, JPanel leftPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gbc.gridx = col;
                gbc.gridy = row;

                CellPane cellPane = new CellPane(imgSlot, hero, row, col);
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
                leftPanel.add(cellPane, gbc);

            }
        }
        hero.inventory.updateCells(inventoryPop);
        splitPanel_WorkArea.setLeftComponent(leftPanel);
    }

    public void AddItem() {

        BufferedImage swdImg = null;
        try {
            swdImg = ImageIO.read(getClass().getResource("/images/wpn-swd.png"));
        } catch (IOException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        var coordinates = hero.inventory.checkFreeSlots();
        if (coordinates != null) {
            Item newItem = new Item("sword", WEAPON, "/images/wpn-swd.png");
            newItem.setProperty(DAMAGE, 5);
            newItem.setProperty(RARITY, 1);
            newItem.setProperty(PRICE, 100);
            hero.inventory.store(newItem, coordinates.get("row"), coordinates.get("column"), "/images/wpn-swd.png");
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
        deleteContext = new javax.swing.JMenuItem();
        equipContext = new javax.swing.JMenuItem();
        rightTab = new javax.swing.JTabbedPane();
        wrappingPanel = new javax.swing.JPanel();
        workPanel = new javax.swing.JPanel();
        scroll_Logs = new javax.swing.JScrollPane();
        textarea_status = new javax.swing.JTextArea();
        scroll_WorkArea = new javax.swing.JScrollPane();
        splitPanel_WorkArea = new javax.swing.JSplitPane();
        leftSplit = new javax.swing.JPanel();
        lblLeftSplit = new javax.swing.JLabel();
        rightSplit = new javax.swing.JPanel();
        statusPanel = new javax.swing.JPanel();
        equipmentPanel = new javax.swing.JPanel();
        credits = new javax.swing.JLabel();
        menuPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cmb_AddEquip = new javax.swing.JComboBox<>();
        button_AddItem = new javax.swing.JButton();
        buttonHolder = new javax.swing.JPanel();
        inventoryToggle = new javax.swing.JToggleButton();

        inventoryPop.setBackground(new java.awt.Color(102, 102, 102));
        inventoryPop.setForeground(new java.awt.Color(255, 255, 255));

        inventoryPop.add(equipContext);
        JSeparator sp = new JSeparator();
        inventoryPop.add(sp);
        inventoryPop.add(deleteContext);

        deleteContext.setBackground(new java.awt.Color(255, 0, 0));
        deleteContext.setForeground(new java.awt.Color(255, 204, 204));
        deleteContext.setText("Discard");

        equipContext.setForeground(new java.awt.Color(255, 255, 255));
        equipContext.setText("Equip");

        JPanel pan = new JPanel();
        rightTab.addTab("2", pan);
        rightTab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        rightTab.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        rightTab.setMaximumSize(new java.awt.Dimension(800, 500));
        rightTab.setMinimumSize(new java.awt.Dimension(500, 500));
        rightTab.setPreferredSize(new java.awt.Dimension(800, 500));

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

        textarea_status.setEditable(false);
        textarea_status.setBackground(new java.awt.Color(0, 0, 0));
        textarea_status.setColumns(20);
        textarea_status.setForeground(new java.awt.Color(255, 255, 204));
        textarea_status.setRows(2);
        textarea_status.setText("Welcome");
        textarea_status.setToolTipText("");
        textarea_status.setWrapStyleWord(true);
        textarea_status.setBorder(null);
        textarea_status.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scroll_Logs.setViewportView(textarea_status);
        try {
            InputStream is = this.getClass().getResourceAsStream("/font/exocet.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            Font sizedFont = font.deriveFont(18f);
            textarea_status.setFont(sizedFont);
        } catch (FontFormatException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageConsole mc = new MessageConsole(textarea_status);
        mc.redirectOut(null, System.out);
        mc.redirectErr(Color.RED, null);
        mc.setMessageLines(100);

        scroll_WorkArea.setOpaque(false);
        scroll_WorkArea.setPreferredSize(new java.awt.Dimension(450, 400));

        splitPanel_WorkArea.setBackground(new java.awt.Color(102, 102, 102));
        splitPanel_WorkArea.setResizeWeight(0.5);
        splitPanel_WorkArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        splitPanel_WorkArea.setOneTouchExpandable(true);

        leftSplit.setBackground(new java.awt.Color(153, 153, 153));
        leftSplit.setMaximumSize(new java.awt.Dimension(300, 300));
        leftSplit.setPreferredSize(new java.awt.Dimension(100, 100));
        leftSplit.setLayout(new java.awt.GridBagLayout());
        leftSplit.add(lblLeftSplit, new java.awt.GridBagConstraints());

        splitPanel_WorkArea.setLeftComponent(leftSplit);

        rightSplit.setMaximumSize(new java.awt.Dimension(800, 500));
        rightSplit.setMinimumSize(new java.awt.Dimension(500, 500));
        rightSplit.setPreferredSize(new java.awt.Dimension(800, 500));
        //rightSplit = new JPanel(new CardLayout());
        //CardLayout card= new CardLayout();
        //rightSplit.setLayout(card);
        //rightSplit.add(equipmentPanel,"equipment");
        //rightSplit.add(statusPanel,"status");
        rightSplit.setLayout(new java.awt.CardLayout());

        statusPanel.setBackground(new java.awt.Color(102, 102, 102));
        statusPanel.setMaximumSize(new java.awt.Dimension(800, 500));
        statusPanel.setMinimumSize(new java.awt.Dimension(800, 500));
        statusPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        BufferedImage inventoryBG = null, slotBG = null;
        try {
            inventoryBG = ImageIO.read(getClass().getResource("/images/panel_bg.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        equipmentPanel = new ImagePanel(inventoryBG);
        equipmentPanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 915, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        equipmentPanel.setMaximumSize(new Dimension(800, 800));
        equipmentPanel.setMinimumSize(new Dimension(500, 500));

        rightSplit.add(statusPanel, "status");

        equipmentPanel.setBackground(new java.awt.Color(204, 204, 204));
        equipmentPanel.setMaximumSize(new java.awt.Dimension(800, 500));
        equipmentPanel.setMinimumSize(new java.awt.Dimension(800, 500));
        equipmentPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        BufferedImage equipmentImg = null;
        try {
            equipmentImg = ImageIO.read(getClass().getResource("/images/panel_bg.png"));
        } catch (IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        equipmentPanel = new ImagePanel(equipmentImg);
        equipmentPanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout equipmentPanelLayout = new javax.swing.GroupLayout(equipmentPanel);
        equipmentPanel.setLayout(equipmentPanelLayout);
        equipmentPanelLayout.setHorizontalGroup(
            equipmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 915, Short.MAX_VALUE)
        );
        equipmentPanelLayout.setVerticalGroup(
            equipmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        equipmentPanel.setMaximumSize(new Dimension(800, 800));
        equipmentPanel.setMinimumSize(new Dimension(500, 500));

        rightSplit.add(equipmentPanel, "equipment");

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
                .addContainerGap(49, Short.MAX_VALUE)
                .addComponent(scroll_WorkArea, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        javax.swing.GroupLayout buttonHolderLayout = new javax.swing.GroupLayout(buttonHolder);
        buttonHolder.setLayout(buttonHolderLayout);
        buttonHolderLayout.setHorizontalGroup(
            buttonHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryToggle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
        );
        buttonHolderLayout.setVerticalGroup(
            buttonHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonHolderLayout.createSequentialGroup()
                .addComponent(inventoryToggle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_AddItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_AddEquip, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cmb_AddEquip)
            .addComponent(button_AddItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        buttonHolder.setLayout(new java.awt.GridBagLayout());

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
        // TODO add your handling code here:

        AddItem();
    }//GEN-LAST:event_button_AddItemActionPerformed

    private void cmb_AddEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_AddEquipActionPerformed
        if (cmb_AddEquip.getSelectedItem().toString() != "-- Select Equipment --") {
            System.out.println(cmb_AddEquip.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmb_AddEquipActionPerformed

    private void inventoryToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryToggleActionPerformed

        CardLayout card = (CardLayout) rightSplit.getLayout();
        if (!inventoryToggle.isSelected()) {
            card.show(rightSplit, "status");
            statusPanel.revalidate();
            statusPanel.repaint();

        } else {
            card.show(rightSplit, "equipment");
            equipmentPanel.revalidate();
            equipmentPanel.repaint();
        }
        splitPanel_WorkArea.revalidate();
        splitPanel_WorkArea.repaint();
    }//GEN-LAST:event_inventoryToggleActionPerformed

//    public static void main(String args[]) {
//
//        java.awt.EventQueue.invokeLater(() -> {
//            new DashboardFrame(hero).setVisible(true);
//
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonHolder;
    private javax.swing.JButton button_AddItem;
    private javax.swing.JComboBox<String> cmb_AddEquip;
    private javax.swing.JLabel credits;
    private javax.swing.JMenuItem deleteContext;
    private javax.swing.JMenuItem equipContext;
    private javax.swing.JPanel equipmentPanel;
    private javax.swing.JPopupMenu inventoryPop;
    private javax.swing.JToggleButton inventoryToggle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLeftSplit;
    private javax.swing.JPanel leftSplit;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel rightSplit;
    private javax.swing.JTabbedPane rightTab;
    private javax.swing.JScrollPane scroll_Logs;
    private javax.swing.JScrollPane scroll_WorkArea;
    private javax.swing.JSplitPane splitPanel_WorkArea;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextArea textarea_status;
    private javax.swing.JPanel workPanel;
    private javax.swing.JPanel wrappingPanel;
    // End of variables declaration//GEN-END:variables
}

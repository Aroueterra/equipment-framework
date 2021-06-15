/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.equipmentframework.UI;

import aroueterra.EquipmentFramework.Main;
import aroueterra.EquipmentFramework.UI.DashboardFrame;
import aroueterra.EquipmentFramework.UI.custom.ImagePanel;
import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import aroueterra.EquipmentFramework.player.Hero;
import aroueterra.EquipmentFramework.utility.Compendium;
import com.google.gson.Gson;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author aroue
 */
public class SetupFrame extends javax.swing.JFrame {

    /**
     * Creates new form SetupFrame
     */
    public SetupFrame() {
        Font exocet = null, exocet_s = null;
        try {
            InputStream is = this.getClass().getResourceAsStream("/font/exocet.ttf");
            exocet = Font.createFont(Font.TRUETYPE_FONT, is);
            exocet = exocet.deriveFont(16f);
            exocet_s = exocet.deriveFont(28f);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(DashboardFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setFont(new FontUIResource(exocet), new FontUIResource(exocet_s));
        initComponents();

    }

    public void CreateDashboard() {
        var compendium = generateCompendium();
        Inventory i = new Inventory(5, 5);
        Hero hero = new Hero(txtName.getText(), 100, 100, 1000, i);
        var dashboard = new DashboardFrame(hero, compendium);
        dashboard.setVisible(true);
        dashboard.toFront();
        dashboard.setState(Frame.MAXIMIZED_BOTH);
        dashboard.requestFocus();
    }

    private Compendium[] generateCompendium() {
        Reader reader = new InputStreamReader(this.getClass()
                .getResourceAsStream("/json/compendium2.txt"));
        Compendium[] compendium = new Gson().fromJson(reader, Compendium[].class);
        return compendium;
        //System.out.println(compendium[0].getName());
    }

    private void setFont(FontUIResource myFont, FontUIResource myFont_s) {
        UIManager.put("CheckBoxMenuItem.acceleratorFont", myFont);
        UIManager.put("Button.font", myFont);
        UIManager.put("ToggleButton.font", myFont);
        UIManager.put("RadioButton.font", myFont);
        UIManager.put("CheckBox.font", myFont);
        UIManager.put("ColorChooser.font", myFont);
        UIManager.put("ComboBox.font", myFont);
        UIManager.put("Label.font", myFont);
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
        UIManager.put("TextField.font", myFont_s);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        framePanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnNewGame = new javax.swing.JButton();
        btnLoadGame = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sanctuary");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 193, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(520, 420));
        setMinimumSize(new java.awt.Dimension(520, 420));
        setPreferredSize(new java.awt.Dimension(520, 420));
        setResizable(false);

        BufferedImage frame_img = null;
        try {
            frame_img = ImageIO.read(getClass().getResource("/images/frame.png"));
        } catch (IOException ex) {
            Logger.getLogger(SetupFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        framePanel = new ImagePanel(frame_img);
        framePanel.setMaximumSize(new java.awt.Dimension(500, 420));
        framePanel.setPreferredSize(new java.awt.Dimension(500, 420));

        javax.swing.GroupLayout framePanelLayout = new javax.swing.GroupLayout(framePanel);
        framePanel.setLayout(framePanelLayout);
        framePanelLayout.setHorizontalGroup(
            framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 513, Short.MAX_VALUE)
        );
        framePanelLayout.setVerticalGroup(
            framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 408, Short.MAX_VALUE)
        );

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel6.setLayout(new java.awt.BorderLayout(50, 0));

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("  ");
        jPanel4.add(jLabel2, java.awt.BorderLayout.NORTH);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Equipment Framework");
        jPanel4.add(jLabel3, java.awt.BorderLayout.PAGE_END);

        jTextField1.setEditable(false);
        jTextField1.setForeground(new java.awt.Color(255, 215, 0));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Sanctuary");
        jTextField1.setBorder(null);
        jTextField1.setFocusable(false);
        jTextField1.setVerifyInputWhenFocusTarget(false);
        jPanel4.add(jTextField1, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel4, java.awt.BorderLayout.CENTER);

        jLabel4.setText(" ");
        jPanel6.add(jLabel4, java.awt.BorderLayout.WEST);

        jLabel5.setText(" ");
        jPanel6.add(jLabel5, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel6);
        jPanel2.add(jSeparator1);

        jPanel5.setMaximumSize(new java.awt.Dimension(400, 120));
        jPanel5.setMinimumSize(new java.awt.Dimension(400, 120));
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 120));
        jPanel5.setRequestFocusEnabled(false);

        jPanel8.setMaximumSize(new java.awt.Dimension(300, 108));
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 108));

        jLabel6.setText("Name");

        txtName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtName.setMaximumSize(new java.awt.Dimension(64, 26));

        btnNewGame.setText("New Game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        btnLoadGame.setText("Load");
        btnLoadGame.setMaximumSize(new java.awt.Dimension(88, 22));
        btnLoadGame.setMinimumSize(new java.awt.Dimension(88, 22));
        btnLoadGame.setPreferredSize(new java.awt.Dimension(88, 22));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 213, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNewGame, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLoadGame, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(btnLoadGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNewGame)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel5);
        jPanel2.add(jSeparator2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel7);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(framePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(framePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed

        CreateDashboard();
        this.setVisible(false);
    }//GEN-LAST:event_btnNewGameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetupFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadGame;
    private javax.swing.JButton btnNewGame;
    private javax.swing.JPanel framePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}

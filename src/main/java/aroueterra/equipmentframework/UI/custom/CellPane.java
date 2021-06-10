/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI.custom;

import aroueterra.EquipmentFramework.UI.DashboardFrame;
import aroueterra.EquipmentFramework.player.Hero;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author aroue
 */
public class CellPane extends JPanel {

    private Hero hero;
    private Color defaultBackground;
    private Border oldBorder;
    private Image image;
    private int row;
    private int column;

    public CellPane(Image img, Hero hero, int row, int column) {
        this.hero = hero;
        this.row = row;
        this.column = column;
        this.image = img;
        setOpaque(false);
        //setBorder(BorderFactory.createEmptyBorder(int top, int left, int bottom, int right));
        setLayout(new BorderLayout(0, 0));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //defaultBackground = getBackground();

                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.YELLOW)));
                //setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                //setBackground(Color.BLUE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.RED)));
                if (hero.inventory.retrieve(row, column) != null) {
                    System.out.println("You see a... " + hero.inventory.retrieve(row, column).getName());
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.YELLOW)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(oldBorder);
                validate();
                repaint();
            }
        });
    }

    public void saveBorder() {
        oldBorder = this.getBorder();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(70, 70);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}

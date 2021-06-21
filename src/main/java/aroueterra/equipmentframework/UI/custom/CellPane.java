/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI.custom;

import aroueterra.EquipmentFramework.UI.DashboardFrame;
import aroueterra.EquipmentFramework.UI.inventory.ItemType;
import aroueterra.EquipmentFramework.UI.inventory.PropertyType;
import aroueterra.EquipmentFramework.player.Hero;
import aroueterra.EquipmentFramework.player.Shop;
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

    private Shop shop;
    private Hero hero;
    private Color defaultBackground;
    private Border oldBorder;
    private Image image;
    private int row;
    private int column;
    private ItemType type;

    public CellPane(Image img, Hero hero, ItemType type) {
        this.hero = hero;
        this.type = type;
        this.image = img;
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //defaultBackground = getBackground();//setBackground(Color.BLUE);//setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.YELLOW)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.RED)));

                if (type == type.WEAPON) {
                    if (hero.retrieveEquipment(2) != null) {
                        System.out.println("Your main weapon provides " + hero.retrieveEquipment(2).getProperty(PropertyType.DAMAGE) + " damage");
                    }

                } else if (type == type.OFFHAND) {
                    if (hero.retrieveEquipment(4) != null) {
                        System.out.println("Your offhand provides " + hero.retrieveEquipment(4).getProperty(PropertyType.ARMOR) + " defense");
                    }
                } else {
                    System.out.println("The armor provides " + "provides 10 defense");
                }
//                if (hero.inventory.retrieve(row, column) != null) {
//                    System.out.println("" + hero.inventory.retrieve(row, column).getName() + " is here");
//                }

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

    public CellPane(Image img, Hero hero, int row, int column) {
        this.hero = hero;
        this.row = row;
        this.column = column;
        this.image = img;
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        addListener();
    }

    public CellPane(Image img, Hero hero, Shop shop, int row, int column) {
        this.image = img;
        this.hero = hero;
        this.shop = shop;
        this.row = row;
        this.column = column;
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        addShopListener();
    }

    private void addShopListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //defaultBackground = getBackground();//setBackground(Color.BLUE);//setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.YELLOW)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.RED)));
                if (shop.inventory.retrieve(row, column) != null) {
                    System.out.println("" + shop.inventory.retrieve(row, column).getName() + " is on display");
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

    private void addListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //defaultBackground = getBackground();//setBackground(Color.BLUE);//setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.YELLOW)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createLineBorder(Color.RED)));
                if (hero.inventory.retrieve(row, column) != null) {
                    System.out.println("" + hero.inventory.retrieve(row, column).getName() + " is here");
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

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setInnerImage(String assetPath) {
        var comp = getComponents();
        for (var i : comp) {
            var slot = ((ImagePanel) i);
            if (assetPath == null) {
                slot.setImage("/images/slot_empty.png");
            } else {
                slot.setImage(assetPath);
            }

        }
    }
}

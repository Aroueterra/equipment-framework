/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI.custom;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

/**
 *
 * @author aroue
 */
public class ValueImportTransferHandler extends TransferHandler {

    public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

    public ValueImportTransferHandler() {
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        boolean accept = false;
        if (canImport(support)) {
            try {
                Transferable t = support.getTransferable();
                Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                if (value instanceof String) {
                    Component component = support.getComponent();
                    if (component instanceof JLabel) {
                        ((JLabel) component).setText(value.toString());
                        accept = true;
                    }
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return accept;
    }
}

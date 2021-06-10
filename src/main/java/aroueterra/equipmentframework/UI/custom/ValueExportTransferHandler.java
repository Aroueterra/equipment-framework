/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI.custom;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author aroue
 */
public class ValueExportTransferHandler extends TransferHandler {

    public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
    private String value;

    public ValueExportTransferHandler(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        Transferable t = new StringSelection(getValue());
        return t;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        super.exportDone(source, data, action);
        // Decide what to do after the drop has been accepted
    }

}

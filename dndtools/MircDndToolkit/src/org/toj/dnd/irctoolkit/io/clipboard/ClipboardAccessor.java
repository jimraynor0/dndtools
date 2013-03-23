package org.toj.dnd.irctoolkit.io.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ClipboardAccessor implements ClipboardOwner {
    private static final ClipboardAccessor INSTANCE = new ClipboardAccessor();

    public static ClipboardAccessor getInstance() {
        return INSTANCE;
    }

    private Logger log = Logger.getLogger(this.getClass());

    private ClipboardAccessor() {
        super();
    }

    public String getClipboardContents() {
        Transferable contents = Toolkit.getDefaultToolkit()
                .getSystemClipboard().getContents(null);
        boolean hasTransferableText = (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                return (String) contents
                        .getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                // highly unlikely since we are using a standard DataFlavor
                log.error(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                log.error(ex);
                ex.printStackTrace();
            }
        }
        return "";
    }

    public void setClipboardContents(String aString) {
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    @Override
    public void lostOwnership(Clipboard arg0, Transferable arg1) {
        // TODO Auto-generated method stub
    }
}

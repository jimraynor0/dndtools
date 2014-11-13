package org.toj.dnd.irctoolkit.ui.map.modelpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class BtnOk extends JButton implements ActionListener {

    private static final long serialVersionUID = -2804865348516253414L;

    private MapModelEditor editorFrame;

    BtnOk(MapModelEditor editorFrame) {
        this.editorFrame = editorFrame;

        this.setText("È·¶¨");
        this.setBounds(45, 123, 89, 23);
        this.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            editorFrame.addOrUpdateModel();
        } catch (IllegalArgumentException e1) {
            return;
        }
        editorFrame.setVisible(false);
    }
}

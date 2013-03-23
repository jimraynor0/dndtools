package org.toj.dnd.irctoolkit.ui.map.modelpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class BtnCancel extends JButton implements ActionListener {

    private static final long serialVersionUID = -5461379820305401981L;

    private MapModelEditor editorFrame;

    BtnCancel(MapModelEditor editorFrame) {
        this.editorFrame = editorFrame;

        this.setText("Cancel");
        this.setBounds(173, 123, 89, 23);
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editorFrame.setVisible(false);
    }
}

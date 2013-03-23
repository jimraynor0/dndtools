package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextfieldParamComponent extends FilterParamComponent {
    private static final Dimension SUGGESTED_SIZE = new Dimension(400, 127);

    private JPanel panel;
    private JTextField tf;

    public TextfieldParamComponent(String params) {
        this.panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Parameter: "));
        tf = new JTextField();
        tf.setText(params);
        tf.setPreferredSize(new Dimension(316, 20));
        // tf.setSize(new Dimension());
        panel.add(tf);
    }

    @Override
    public Dimension getSuggestedParentSize() {
        return SUGGESTED_SIZE;
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public String retrieveParam() {
        return tf.getText();
    }

    @Override
    public Component getTitle() {
        return null;
    }
}

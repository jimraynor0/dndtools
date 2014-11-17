package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.toj.dnd.irctoolkit.ui.StyleConstants;

public class TextfieldParamComponent extends FilterParamComponent {
    private JPanel panel;
    private JTextField tf;

    public TextfieldParamComponent(String params) {
        this.panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel jLabel = new JLabel("参数: ");
        jLabel.setPreferredSize(StyleConstants.TEXT_PARAM_LABEL_SIZE);
        panel.add(jLabel);

        tf = new JTextField();
        tf.setText(params);
        tf.setPreferredSize(StyleConstants.TEXT_PARAM_INPUT_SIZE);
        panel.add(tf);
    }

    @Override
    public Dimension getSuggestedParentSize() {
        return StyleConstants.TEXT_FIELD_PARAM_SIZE;
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

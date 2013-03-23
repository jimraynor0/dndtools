package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

public class TextLabelComponent extends FilterParamComponent {
    private static final Dimension SUGGESTED_SIZE = new Dimension(400, 127);

    private static Map<String, TextLabelComponent> COMP_POOL = new HashMap<String, TextLabelComponent>();

    public static FilterParamComponent getInstance(String text) {
        if (!COMP_POOL.containsKey(text)) {
            COMP_POOL.put(text, new TextLabelComponent(text));
        }
        return COMP_POOL.get(text);
    }

    private JLabel label;

    public TextLabelComponent(String text) {
        this.label = new JLabel(text);
    }

    @Override
    public Dimension getSuggestedParentSize() {
        return SUGGESTED_SIZE;
    }

    @Override
    public Component getComponent() {
        return label;
    }

    @Override
    public String retrieveParam() {
        return null;
    }

    @Override
    public Component getTitle() {
        return null;
    }
}

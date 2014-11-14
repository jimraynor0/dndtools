package org.toj.dnd.irctoolkit.ui.map.modelpane;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.toj.dnd.irctoolkit.ui.StyleConstants;

public class ColorTypeRadioGroup extends ButtonGroup {

    private static final long serialVersionUID = 890748691867041619L;

    public static final String NEITHER = "neither";
    public static final String BACKGROUND = "background";
    public static final String FOREGROUND = "foreground";

    private JRadioButton radioFore;
    private JRadioButton radioBack;

    ColorTypeRadioGroup() {
        radioFore = new JRadioButton("ǰ��ɫ");
        this.add(radioFore);

        radioBack = new JRadioButton("����ɫ");
        this.add(radioBack);
    }

    public String getSelected() {
        if (radioFore.isSelected()) {
            return FOREGROUND;
        }
        if (radioBack.isSelected()) {
            return BACKGROUND;
        }
        return NEITHER;
    }

    public JRadioButton getRadioFore() {
        return radioFore;
    }

    public JRadioButton getRadioBack() {
        return radioBack;
    }
}

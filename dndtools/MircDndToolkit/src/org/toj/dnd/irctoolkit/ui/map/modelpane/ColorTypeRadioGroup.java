package org.toj.dnd.irctoolkit.ui.map.modelpane;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class ColorTypeRadioGroup extends ButtonGroup {

    private static final long serialVersionUID = 890748691867041619L;

    public static final String NEITHER = "neither";
    public static final String BACKGROUND = "background";
    public static final String FOREGROUND = "foreground";

    private JRadioButton radioFore;
    private JRadioButton radioBack;

    ColorTypeRadioGroup() {

        radioFore = new JRadioButton("Ç°¾°É«");
        this.add(radioFore);
        radioFore.setBounds(11, 65, 89, 23);

        radioBack = new JRadioButton("±³¾°É«");
        this.add(radioBack);
        radioBack.setBounds(102, 65, 89, 23);
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

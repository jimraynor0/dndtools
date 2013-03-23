package org.toj.dnd.irctoolkit.util;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtil {
    public static Element textElement(String name, String text) {
        Element e = DocumentHelper.createElement(name);
        e.setText(text);
        return e;
    }

    public static Element textElement(String name, int number) {
        Element e = DocumentHelper.createElement(name);
        e.setText(String.valueOf(number));
        return e;
    }

    public static Element textElement(String name, boolean b) {
        Element e = DocumentHelper.createElement(name);
        e.setText(String.valueOf(b));
        return e;
    }
}

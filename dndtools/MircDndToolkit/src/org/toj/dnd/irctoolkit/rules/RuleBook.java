package org.toj.dnd.irctoolkit.rules;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;

public class RuleBook {
    private static RuleBook INSTANCE;

    public static RuleBook getRuleBook() {
        if (INSTANCE == null) {
            INSTANCE = new RuleBook();
        }
        return INSTANCE;
    }

    private Document doc = null;

    private RuleBook() {
        SAXReader reader = new SAXReader();
        try {
            doc = reader.read(this.getClass().getResourceAsStream("rules.xml"));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> query(String ruleElement) {
        Iterator<Element> i = doc.getRootElement().elementIterator();
        while (i.hasNext()) {
            Element e = i.next();
            if (AbbreviationUtil.isAbbre(ruleElement, e.attribute("name")
                    .getValue())
                    || e.attribute("name").getValue()
                            .equalsIgnoreCase(ruleElement)) {
                List<String> result = new LinkedList<String>();
                result.add("---- " + e.attribute("name").getValue() + " ----");
                for (String line : e.getText().split("/")) {
                    result.add(line);
                }
                result.add("---- " + e.attribute("name").getValue() + " ----");
                return result;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(RuleBook.getRuleBook().query("blinded"));
    }
}

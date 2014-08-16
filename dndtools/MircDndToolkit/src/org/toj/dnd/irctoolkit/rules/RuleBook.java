package org.toj.dnd.irctoolkit.rules;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.toj.dnd.irctoolkit.util.AbbreviationUtil;

public class RuleBook {
    private static final File RULE_FILE = new File(
        "./resources/rules.xml");
    private static RuleBook INSTANCE;

    public static RuleBook getRuleBook() {
        if (INSTANCE == null) {
            INSTANCE = new RuleBook();
        }
        return INSTANCE;
    }

    private Map<String, List<Rule>> rules = new HashMap<String, List<Rule>>();

    private RuleBook() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(RULE_FILE);
            for (Element e : (List<Element>) doc.getRootElement().elements()) {
                Rule rule = new Rule();
                rule.setName(e.attributeValue("name"));
                rule.setType(e.attributeValue("type"));
                rule.setText(e.getText());
                addToRuleSet(rule);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addToRuleSet(Rule rule) {
        if (!rules.containsKey(rule.getName().toLowerCase())) {
            rules.put(rule.getName().toLowerCase(), new LinkedList<Rule>());
        }
        rules.get(rule.getName().toLowerCase()).add(rule);
    }

    @SuppressWarnings("unchecked")
    public List<String> query(String ruleElement) {
        ruleElement = ruleElement.toLowerCase();
        for (String ruleName : rules.keySet()) {
            if (ruleName.equals(ruleElement) || AbbreviationUtil.isAbbre(ruleElement, ruleName)) {
                List<String> result = new LinkedList<String>();
                for (Rule rule : rules.get(ruleName)) {
                    result.add(getTitle(rule));
                    for (String line : rule.getText().split("/")) {
                        result.add(line);
                    }
                    result.add(getTitle(rule));
                }
                return result;
            }
        }
        return null;
    }

    private String getTitle(Rule rule) {
        StringBuilder sb = new StringBuilder("---- ").append(rule.getName());
        if (!StringUtils.isEmpty(rule.getType())) {
            sb.append(" (").append(rule.getType()).append(")");
        }
        sb.append(" ----");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(RuleBook.getRuleBook().query("blinded"));
    }
}

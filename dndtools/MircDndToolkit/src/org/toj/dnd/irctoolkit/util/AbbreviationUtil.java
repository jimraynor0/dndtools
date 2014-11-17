package org.toj.dnd.irctoolkit.util;

import org.apache.commons.lang.StringUtils;

public class AbbreviationUtil {

    public static boolean isCapitalAbbre(String abbre, String text) {
        if (text == null || abbre == null) {
            return false;
        }
        String[] words = text.split(" ");
        StringBuilder ab = new StringBuilder();
        for (String word : words) {
            ab.append(word.charAt(0));
        }
        return ab.toString().equalsIgnoreCase(abbre);
    }

    public static boolean isPrefixAbbre(String abbre, String text) {
        if (text == null || abbre == null) {
            return false;
        }
        if (isChinese(abbre)) {
            return text.toLowerCase().startsWith(abbre.toLowerCase());
        } else {
            return abbre.length() >= 3
                    && text.toLowerCase().startsWith(abbre.toLowerCase());
        }
    }

    private static boolean isChinese(String abbre) {
        for (char ch : abbre.toCharArray()) {
            if ((ch >= 0x4e00) && (ch <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAbbre(String abbre, String text) {
        if (text == null || abbre == null) {
            return false;
        }
        if (text.contains(" ")) {
            return isCapitalAbbre(abbre, text);
        } else {
            return isPrefixAbbre(abbre, text);
        }
    }

    public static String getIcon(String desc) {
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        if (isChinese(desc.substring(0, 1))) {
            return desc.substring(0, 1);
        }
        if (desc.length() == 1) {
            return desc + desc;
        }
        if (desc.length() == 2) {
            return desc;
        }
        return desc.substring(0, 2);
    }

    public static void main(String[] args) {
        System.out.println(isPrefixAbbre("b", "Bairmot"));
        System.out.println(isPrefixAbbre("bai", "Bairmot"));
        System.out.println(isPrefixAbbre("bair", "Bairmot"));
        System.out.println(isPrefixAbbre("贝", "贝莫托"));
        System.out.println(isPrefixAbbre("贝莫托", "贝莫托顽石"));

        System.out.println("-------------------");

        System.out.println(getIcon("B"));
        System.out.println(getIcon("Ba"));
        System.out.println(getIcon("Bairmot"));
        System.out.println(getIcon("贝"));
        System.out.println(getIcon("贝莫托"));
    }
}

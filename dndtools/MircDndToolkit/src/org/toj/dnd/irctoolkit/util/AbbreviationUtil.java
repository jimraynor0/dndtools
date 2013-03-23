package org.toj.dnd.irctoolkit.util;

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
            return abbre.length() >= 3 && text.toLowerCase().startsWith(abbre.toLowerCase());
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

    public static void main(String[] args) {
        System.out.println(isPrefixAbbre("b", "Bairmot"));
        System.out.println(isPrefixAbbre("bai", "Bairmot"));
        System.out.println(isPrefixAbbre("bair", "Bairmot"));
        System.out.println(isPrefixAbbre("±´", "±´ÄªÍÐ"));
        System.out.println(isPrefixAbbre("±´ÄªÍÐ", "±´ÄªÍÐÍçÊ¯"));
    }
}

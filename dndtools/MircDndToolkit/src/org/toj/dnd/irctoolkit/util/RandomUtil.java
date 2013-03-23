package org.toj.dnd.irctoolkit.util;

public class RandomUtil {

    public static int random(int i, int j) {
        return i + (int) (Math.random() * (j - i));
    }

    public static String pickSide() {
        double rand = Math.random();
        if (rand < 0.25) {
            return "North";
        } else if (rand < 0.5) {
            return "East";
        } else if (rand < 0.75) {
            return "South";
        } else {
            return "West";
        }
    }
}

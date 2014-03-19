package org.toj.d6s.wuxia.movesetgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.toj.d6s.wuxia.movesetgen.Move.Protect;

public class MoveSetGenerator {
    private List<Move> moves = new LinkedList<Move>();

    private List<String> targetPool;
    private Map<String, Integer> protectPool;
    private List<Protect> protectArray;

    private Map<String, Integer> atkPref = new HashMap<String, Integer>();
    private Map<String, Integer> defPref = new HashMap<String, Integer>();
    private Map<String, Integer> defTypePref = new HashMap<String, Integer>();

    public MoveSetGenerator(List<String> targetPool, Map<String, Integer> protectPool, List<Protect> protectArray) {
        this.targetPool = new LinkedList<String>();
        this.targetPool.addAll(targetPool);
        this.protectPool = protectPool;
        this.protectArray = protectArray;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(move.getName()).append("\t").append(move.getTarget()).append("\t").append(move.getProtects()).append("\r\n");
        }
        sb.append("\r\n");
        sb.append("’– Ω«©√˚: \r\n").append(generateSignature());
        return sb.toString();
    }

    private String generateSignature() {
        for (Move move: moves) {
            increase(move.getTarget(), atkPref, 1);
            for (Protect p : move.getProtects()) {
                increase(p.getPart(), defPref, p.getBonus());
                increase(p.getType(), defTypePref, p.getBonus());
            }
        }
        StringBuilder sb = new StringBuilder("π•ª˜Œª÷√«„œÚ: ");
        List<String> keys = new ArrayList<String>(8);
        keys.addAll(atkPref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return atkPref.get(o2) - atkPref.get(o1);
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(atkPref.get(key)).append(", ");
        }
        sb.append("\r\n").append("∑¿”˘Œª÷√«„œÚ: ");
        keys.clear();
        keys.addAll(defPref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return defPref.get(o2) - defPref.get(o1);
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(defPref.get(key)).append(", ");
        }
        sb.append("\r\n").append("∑¿”˘¿‡–Õ«„œÚ: ");
        keys.clear();
        keys.addAll(defTypePref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return defTypePref.get(o2) - defTypePref.get(o1);
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(defTypePref.get(key)).append(", ");
        }
        return sb.toString();
    }

    private void increase(String key, Map<String, Integer> map, int weight) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + weight);
        } else {
            map.put(key, weight);
        }
    }

    public void generate() {
        if (protectArray.size() * targetPool.size() > countProtectPool()) {
            throw new RuntimeException("«◊£¨ƒ„µƒ∑¿”˘≤øŒª≥ÿ√ª∏¯πª∞°°≠°≠");
        }

        int index = 1;
        while (!targetPool.isEmpty()) {
            Move move = new Move();
            move.setName("’– Ω" + index);
            index++;

            move.setTarget(findTarget());

            move.setProtects(generateProtects());
            moves.add(move);
        }
    }

    private int countProtectPool() {
        int count = 0;
        for (String key : protectPool.keySet()) {
            count += protectPool.get(key);
        }
        return count;
    }

    private List<Protect> generateProtects() {
        List<Protect> protects = new LinkedList<Protect>();
        List<String> parts = generatePartPoolForMove();

        for (Protect t : protectArray) {
            Protect p = new Protect(t);
            p.setPart(getPart(parts));
            protects.add(p);
        }

        return protects;
    }

    private List<String> generatePartPoolForMove() {
        System.out.println("parts left: " + protectPool);
        List<String> parts = new ArrayList<String>();
        for (String part : protectPool.keySet()) {
            for (int i = 0; i < protectPool.get(part); i++) {
                parts.add(part);
            }
        }
        return parts;
    }

    private String getPart(List<String> parts) {
        int random = (int) (Math.random() * parts.size());
        String part = parts.get(random);
        parts.removeAll(Arrays.asList(part));
        int count = protectPool.get(part);
        if (count == 1) { // last one
            protectPool.remove(part);
        } else {
            protectPool.put(part, count - 1);
        }
        return part;
    }

    private String findTarget() {
        int random = (int) (Math.random() * targetPool.size());
        return targetPool.remove(random);
    }

    public static void main(String[] args) {
        List<String> targetPool = Arrays.asList("Õ∑", "Õ∑", "–ÿ", "–ÿ", "–ÿ", "–ÿ", "∏π", "∏π", "◊Û±€", "◊Û±€", "”“±€", "”“±€", "◊ÛÕ»", "”“Õ»");
//        List<String> targetPool = Arrays.asList("Õ∑", "Õ∑", "Õ∑", "–ÿ", "–ÿ", "–ÿ", "∏π", "∏π", "∏π", "◊Û±€", "”“±€", "◊ÛÕ»", "”“Õ»");

        Map<String, Integer> protectPool = new HashMap<String, Integer>();
        protectPool.put("Õ∑", 14);
        protectPool.put("–ÿ", 14);
        protectPool.put("∏π", 14);
        protectPool.put("±≥", 5);
        protectPool.put("◊Û±€", 12);
        protectPool.put("”“±€", 12);
        protectPool.put("◊ÛÕ»", 12);
        protectPool.put("”“Õ»", 12);

        List<Protect> protectArray = new ArrayList<Protect>(8);
        protectArray.add(new Protect("∏Òµ≤", 4));
        protectArray.add(new Protect("…¡±‹", 4));
        protectArray.add(new Protect("…¡±‹", 4));
        protectArray.add(new Protect("∏Òµ≤", 2));
        protectArray.add(new Protect("…¡±‹", 2));
        protectArray.add(new Protect("…¡±‹", 2));

        MoveSetGenerator gen = new MoveSetGenerator(targetPool, protectPool, protectArray);
        gen.generate();
        System.out.println(gen.toString());
    }
}

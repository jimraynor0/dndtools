package org.toj.d6s.wuxia.movesetgen;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.toj.d6s.wuxia.movesetgen.Move.Protect;

public class MoveSetGenerator {
    public static final String[] PARTS = {"Õ∑", "–ÿ", "∏π", "±≥", "◊Û±€", "”“±€", "◊ÛÕ»", "”“Õ»"};
    private List<Move> moves = new LinkedList<Move>();

    private List<String> targetPool;
    private Map<String, Integer> protectPool;
    private List<List<Protect>> protectArray;

    private Map<String, Integer> atkPref = new HashMap<String, Integer>();
    private Map<String, Integer> defPref = new HashMap<String, Integer>();
    private Map<String, Integer> defTypePref = new HashMap<String, Integer>();

    public MoveSetGenerator(List<String> targetPool, Map<String, Integer> protectPool, List<List<Protect>> protectArray) {
        this.targetPool = new LinkedList<String>();
        this.targetPool.addAll(targetPool);
        this.protectPool = protectPool;
        this.protectArray = protectArray;
    }

    private String generateSignature() {
        int weakPointCount = 0;
        for (Move move: moves) {
            weakPointCount += move.getWeakPoint();
            increase(move.getTarget(), atkPref, 1);
            for (Protect p : move.getProtects()) {
                increase(p.getPart(), defPref, p.getBonus());
                increase(p.getType(), defTypePref, p.getBonus());
            }
        }
        StringBuilder sb = new StringBuilder("π•ª˜Œª÷√±»¿˝: ");
        List<String> keys = new ArrayList<String>(8);
        keys.addAll(atkPref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (atkPref.get(o2) * 31 - Arrays.asList(PARTS).indexOf(o1)) - (atkPref.get(o1) * 31 - Arrays.asList(PARTS).indexOf(o1));
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(atkPref.get(key)).append("£¨");
        }
        sb.append("\r\n").append("∑¿”˘º”÷µ±»¿˝: ");
        keys.clear();
        keys.addAll(defPref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (defPref.get(o2) * 31 - Arrays.asList(PARTS).indexOf(o1)) - (defPref.get(o1) * 31 - Arrays.asList(PARTS).indexOf(o1));
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(defPref.get(key)).append("£¨");
        }
        sb.append("\r\n").append("∑¿”˘¿‡–Õ±»¿˝: ");
        keys.clear();
        keys.addAll(defTypePref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return defTypePref.get(o2) - defTypePref.get(o1);
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(defTypePref.get(key)).append("£¨");
        }
        NumberFormat numFormat = NumberFormat.getPercentInstance();
        numFormat.setMaximumFractionDigits(2);
        sb.append("\r\n").append("∑¿”˘¬©∂¥±»¿˝: ").append(weakPointCount).append("/").append(moves.size() * PARTS.length).append(" = ").append(numFormat.format(((double) weakPointCount) / (moves.size() * PARTS.length)));
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
        if (protectArray.size() < targetPool.size()) {
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
        calculateWeakPoints();
    }

    private void calculateWeakPoints() {
        List<Move> cyclingMoveList = new ArrayList<Move>(moves.size() + 3);
        cyclingMoveList.addAll(moves);
        cyclingMoveList.addAll(moves.subList(0, 3));
        for (int i = 0; i < moves.size(); i++) {
            Set<String> nextMoveCovers = new HashSet<String>();
            for (Protect p : cyclingMoveList.get(i + 1).getProtects()) {
                nextMoveCovers.add(p.getPart());
            }
            for (Protect p : cyclingMoveList.get(i + 2).getProtects()) {
                nextMoveCovers.add(p.getPart());
            }
            for (Protect p : cyclingMoveList.get(i + 3).getProtects()) {
                nextMoveCovers.add(p.getPart());
            }
            moves.get(i).setWeakPoint(PARTS.length - nextMoveCovers.size());
        }
    }

    private List<Protect> generateProtects() {
        List<Protect> protects = protectArray.remove(0);
        List<String> parts = generatePartPoolForMove();

        for (Protect t : protects) {
            t.setPart(getPart(parts));
        }

        return protects;
    }

    private List<String> generatePartPoolForMove() {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(String.format("%-16s%-6s", move.getName(), move.getTarget()));
            for (String part : PARTS) {
                Protect p = findProtect(part, move.getProtects());
                if (p != null) {
                    sb.append(String.format("| %-2s %s%+d ", p.getPart(), p.getType(), p.getBonus()));
                } else {
                    sb.append(String.format("|        "));
                }
            }
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("Ã◊¬∑«©√˚: \r\n").append(generateSignature());
        return sb.toString().replaceAll(" ", "°°");
    }

    public String toBbcodeTableString() {
        StringBuilder sb = new StringBuilder("[table]\r\n");
        for (Move move : moves) {
            sb.append("[tr]");
            appendCell(sb, move.getName());
            appendCell(sb, move.getTarget());
            for (String part : PARTS) {
                Protect p = findProtect(part, move.getProtects());
                if (p != null) {
                    appendCell(sb,
                        p.getPart() + " " + p.getType() + "+" + p.getBonus());
                } else {
                    appendCell(sb, "-");
                }
            }
            sb.append(move.getName()).append("\t").append(move.getTarget())
                .append("\t").append(move.getProtects()).append("\r\n");
            sb.append("[/tr]\r\n");
        }
        return sb.append("[/table]").toString();
    }

    private Protect findProtect(String part, List<Protect> protects) {
        for (Protect p: protects) {
            if (part.equals(p.getPart())) {
                return p;
            }
        }
        return null;
    }

    private void appendCell(StringBuilder sb, String str) {
        sb.append("[td]");
        sb.append(str);
        sb.append("[/td]");
    }

    public static void main(String[] args) {
//      List<String> targetPool = Arrays.asList("Õ∑", "Õ∑", "–ÿ", "–ÿ", "–ÿ", "–ÿ", "∏π", "∏π", "◊Û±€", "”“±€", "◊ÛÕ»", "”“Õ»");
        List<String> targetPool = Arrays.asList("Õ∑", "Õ∑", "Õ∑", "–ÿ", "∏π", "◊Û±€", "”“±€", "◊ÛÕ»", "”“Õ»", "◊Û±€", "”“±€", "◊ÛÕ»", "”“Õ»");

        Map<String, Integer> protectPool = new HashMap<String, Integer>();
        protectPool.put("Õ∑", 15);
        protectPool.put("–ÿ", 15);
        protectPool.put("∏π", 15);
        protectPool.put("±≥", 5);
        protectPool.put("◊Û±€", 12);
        protectPool.put("”“±€", 12);
        protectPool.put("◊ÛÕ»", 12);
        protectPool.put("”“Õ»", 12);

        List<List<Protect>> protectArray = new ArrayList<List<Protect>>(targetPool.size());
        for (int i = 0; i < 7; i++) {
            List<Protect> protects = new ArrayList<Protect>(8);
            protects.add(new Protect("…¡±‹", 3));
            protects.add(new Protect("∏Òµ≤", 3));
            protects.add(new Protect("…¡±‹", 1));
            protects.add(new Protect("…¡±‹", 1));
            protectArray.add(protects);
        }
        for (int i = 0; i < 7; i++) {
            List<Protect> protects = new ArrayList<Protect>(8);
            protects.add(new Protect("∏Òµ≤", 3));
            protects.add(new Protect("…¡±‹", 3));
            protects.add(new Protect("…¡±‹", 1));
            protectArray.add(protects);
        }
        Collections.shuffle(protectArray);

        MoveSetGenerator gen = new MoveSetGenerator(targetPool, protectPool, protectArray);
        gen.generate();
        System.out.println(gen.toString());
    }
}

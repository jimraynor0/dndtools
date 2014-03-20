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
    public static final String[] PARTS = {"Í·", "ÐØ", "¸¹", "±³", "×ó±Û", "ÓÒ±Û", "×óÍÈ", "ÓÒÍÈ"};
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
        StringBuilder sb = new StringBuilder("¹¥»÷Î»ÖÃ±ÈÀý: ");
        List<String> keys = new ArrayList<String>(8);
        keys.addAll(atkPref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (atkPref.get(o2) * 31 - Arrays.asList(PARTS).indexOf(o1)) - (atkPref.get(o1) * 31 - Arrays.asList(PARTS).indexOf(o1));
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(atkPref.get(key)).append("£¬");
        }
        sb.append("\r\n").append("·ÀÓù¼ÓÖµ±ÈÀý: ");
        keys.clear();
        keys.addAll(defPref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (defPref.get(o2) * 31 - Arrays.asList(PARTS).indexOf(o1)) - (defPref.get(o1) * 31 - Arrays.asList(PARTS).indexOf(o1));
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(defPref.get(key)).append("£¬");
        }
        sb.append("\r\n").append("·ÀÓùÀàÐÍ±ÈÀý: ");
        keys.clear();
        keys.addAll(defTypePref.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return defTypePref.get(o2) - defTypePref.get(o1);
            }
        });
        for (String key : keys) {
            sb.append(key).append("=").append(defTypePref.get(key)).append("£¬");
        }
        NumberFormat numFormat = NumberFormat.getPercentInstance();
        numFormat.setMaximumFractionDigits(2);
        sb.append("\r\n").append("·ÀÓùÂ©¶´±ÈÀý: ").append(weakPointCount).append("/").append(moves.size() * PARTS.length).append(" = ").append(numFormat.format(((double) weakPointCount) / (moves.size() * PARTS.length)));
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
            throw new RuntimeException("Ç×£¬ÄãµÄ·ÀÓù²¿Î»³ØÃ»¸ø¹»°¡¡­¡­");
        }

        int index = 1;
        while (!targetPool.isEmpty()) {
            Move move = new Move();
            move.setName("ÕÐÊ½" + index);
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
        sb.append("Ì×Â·Ç©Ãû: \r\n").append(generateSignature());
        return sb.toString().replaceAll(" ", "¡¡");
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
        List<String> targetPool = Arrays.asList("Í·", "Í·", "ÐØ", "¸¹", "¸¹", "¸¹", "±³", "±³", "×ó±Û", "ÓÒ±Û", "×óÍÈ", "×óÍÈ", "ÓÒÍÈ", "ÓÒÍÈ");
//        List<String> targetPool = Arrays.asList("Í·", "Í·", "Í·", "ÐØ", "ÐØ", "ÐØ", "¸¹", "¸¹", "¸¹", "×ó±Û", "ÓÒ±Û", "×óÍÈ", "ÓÒÍÈ");

        Map<String, Integer> protectPool = new HashMap<String, Integer>();
        protectPool.put("Í·", 12);
        protectPool.put("ÐØ", 12);
        protectPool.put("¸¹", 12);
        protectPool.put("±³", 5);
        protectPool.put("×ó±Û", 14);
        protectPool.put("ÓÒ±Û", 14);
        protectPool.put("×óÍÈ", 14);
        protectPool.put("ÓÒÍÈ", 14);

        List<Protect> protectArray = new ArrayList<Protect>(8);
        protectArray.add(new Protect("¸ñµ²", 4));
        protectArray.add(new Protect("ÉÁ±Ü", 4));
        protectArray.add(new Protect("ÉÁ±Ü", 4));
        protectArray.add(new Protect("ÉÁ±Ü", 2));
//        protectArray.add(new Protect("ÉÁ±Ü", 2));
//        protectArray.add(new Protect("ÉÁ±Ü", 2));

        MoveSetGenerator gen = new MoveSetGenerator(targetPool, protectPool, protectArray);
        gen.generate();
        System.out.println(gen.toString());
    }
}

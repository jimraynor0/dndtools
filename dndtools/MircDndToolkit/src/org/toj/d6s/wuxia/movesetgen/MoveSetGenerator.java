package org.toj.d6s.wuxia.movesetgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        return sb.toString();
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
        Collections.shuffle(moves);
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
        List<String> parts = new ArrayList<String>();
        parts.addAll(protectPool.keySet());

        for (Protect t : protectArray) {
            Protect p = new Protect(t);
            p.setPart(getPart(parts));
            protects.add(p);
        }

        return protects;
    }

    private String getPart(List<String> parts) {
        int random = (int) (Math.random() * parts.size());
        String part = parts.remove(random);
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
//        List<String> targetPool = Arrays.asList("Í·", "Í·", "ÐØ", "ÐØ", "ÐØ", "ÐØ", "¸¹", "¸¹", "×ó±Û", "×ó±Û", "ÓÒ±Û", "ÓÒ±Û", "×óÍÈ", "ÓÒÍÈ");
        List<String> targetPool = Arrays.asList("Í·", "Í·", "Í·", "ÐØ", "ÐØ", "ÐØ", "¸¹", "¸¹", "¸¹", "×ó±Û", "ÓÒ±Û", "×óÍÈ", "ÓÒÍÈ");

        Map<String, Integer> protectPool = new HashMap<String, Integer>();
        protectPool.put("Í·", 10);
        protectPool.put("ÐØ", 14);
        protectPool.put("¸¹", 14);
        protectPool.put("±³", 6);
        protectPool.put("×ó±Û", 10);
        protectPool.put("ÓÒ±Û", 10);
        protectPool.put("×óÍÈ", 10);
        protectPool.put("ÓÒÍÈ", 10);

        List<Protect> protectArray = new ArrayList<Protect>(8);
        protectArray.add(new Protect("¸ñµ²", 4));
        protectArray.add(new Protect("¸ñµ²", 4));
        protectArray.add(new Protect("ÉÁ±Ü", 4));
        protectArray.add(new Protect("¸ñµ²", 2));
        protectArray.add(new Protect("ÉÁ±Ü", 2));

        MoveSetGenerator gen = new MoveSetGenerator(targetPool, protectPool, protectArray);
        gen.generate();
        System.out.println(gen.toString());
    }
}

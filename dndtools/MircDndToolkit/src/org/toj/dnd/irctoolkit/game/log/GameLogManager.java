package org.toj.dnd.irctoolkit.game.log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.io.file.GameStore;

public class GameLogManager {
    private static GameLogManager INSTANCE = new GameLogManager();

    public static GameLogManager getInstance() {
        return INSTANCE;
    }

    private List<GameLog> logs;
    private String game;

    public void addLog(String title, String text, String logger) {
        checkInit();
        logs.add(new GameLog(logger, title, text));
        save();
    }

    public void removeLog(String titleOrTime) {
        checkInit();
        for (GameLog log : logs) {
            if (log.getTime().equals(titleOrTime)
                    || log.getTitle().equalsIgnoreCase(titleOrTime)) {
                logs.remove(log);
                break;
            }
        }
        save();
    }

    public List<String> query(String criteriaStr) {
        checkInit();
        if (criteriaStr == null || criteriaStr.isEmpty()) {
            List<String> results = new ArrayList<String>(logs.size());
            for (GameLog log : logs) {
                results.add(log.toString());
            }
            return results;
        }

        String[] parsed = criteriaStr.split("\\|");
        List<String> results = new LinkedList<String>();
        if (parsed.length == 1) {
            for (GameLog log : logs) {
                if (log.toString().contains(parsed[0])) {
                    results.add(log.toString());
                }
            }
            return results;
        }

        if (parsed.length != 2) {
            return new ArrayList<String>(0);
        }
        String crits = parsed[0];
        String keyword = parsed[1];
        parsed = crits.split("\\&");

        for (GameLog log : logs) {
            for (String c : parsed) {
                if (log.fitsCriteria(c, keyword)) {
                    results.add(log.toString());
                }
            }
        }
        return results;
    }

    private void save() {
        List<String> results = new ArrayList<String>(logs.size());
        for (GameLog log : logs) {
            results.add(log.toString());
        }
        GameStore.saveLog(results, ToolkitEngine.getEngine().getContext()
                .getGame().getName());
    }

    private void checkInit() {
        if (ToolkitEngine.getEngine().getContext().getGame().getName()
                .equals(this.game)) {
            return;
        }

        this.logs = new LinkedList<GameLog>();
        List<String> results = GameStore.loadLog(ToolkitEngine.getEngine()
                .getContext().getGame().getName());
        for (String line : results) {
            String time = line.substring(0, line.indexOf(GameLog.SEPARATOR));
            line = line.substring(line.indexOf(GameLog.SEPARATOR)
                    + GameLog.SEPARATOR.length());
            String logger = line.substring(0, line.indexOf(GameLog.SEPARATOR));
            line = line.substring(line.indexOf(GameLog.SEPARATOR)
                    + GameLog.SEPARATOR.length());
            String title = line.substring(0, line.indexOf(GameLog.SEPARATOR));
            line = line.substring(line.indexOf(GameLog.SEPARATOR)
                    + GameLog.SEPARATOR.length());
            this.logs.add(new GameLog(logger, time, title, line));
        }
        this.game = ToolkitEngine.getEngine().getContext().getGame().getName();
    }
}

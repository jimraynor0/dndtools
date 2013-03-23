package org.toj.dnd.irctoolkit.game.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLog {
    public static final String CRITERIA_TIME = "time";
    public static final String CRITERIA_TITLE = "title";
    public static final String CRITERIA_TEXT = "text";
    public static final String CRITERIA_LOGGER = "logger";

    public static final String SEPARATOR = " - ";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private String title;
    private String time;
    private String logger;
    private String text;

    public GameLog(String logger, String text) {
        this(logger, "UNTITLED", text);
    }

    public GameLog(String logger, String title, String text) {
        this(logger, dateFormat.format(new Date()), title, text);
    }

    public GameLog(String logger, String time, String title, String text) {
        this.logger = logger;
        this.text = text;
        this.title = title;
        this.time = time;
    }

    public boolean fitsCriteria(String criteria, String keyword) {
        if (keyword == null) {
            return false;
        }
        if (CRITERIA_TIME.equals(criteria)) {
            return this.time.startsWith(keyword);
        }
        if (CRITERIA_TITLE.equals(criteria)) {
            return this.title.toLowerCase().contains(keyword.toLowerCase());
        }
        if (CRITERIA_TEXT.equals(criteria)) {
            return this.text.toLowerCase().contains(keyword.toLowerCase());
        }
        if (CRITERIA_LOGGER.equals(criteria)) {
            return this.logger.toLowerCase().startsWith(keyword.toLowerCase());
        }
        return false;
    }

    public String toString() {
        return new StringBuilder().append(time).append(SEPARATOR)
                .append(logger).append(SEPARATOR).append(title)
                .append(SEPARATOR).append(text).toString();
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }
}

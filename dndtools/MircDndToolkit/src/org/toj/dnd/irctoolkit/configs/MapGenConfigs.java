package org.toj.dnd.irctoolkit.configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;

public class MapGenConfigs {
    public static final String CONF_MAP_WIDTH = "map.width";
    public static final String CONF_MAP_HEIGHT = "map.height";

    public static final String CONF_ROOM_COUNT = "room.count";
    public static final String CONF_ROOM_WIDTH_MIN = "room.minwidth";
    public static final String CONF_ROOM_WIDTH_MAX = "room.maxwidth";

    public static final String CONF_DOOR_WIDTH_MIN = "door.minwidth";
    public static final String CONF_DOOR_WIDTH_MAX = "door.maxwidth";

    private static final File CONFIG_FILE = new File(
            "./config/MapGenConfigs.properties");

    private Properties props;

    public String get(String key) {
        return props.getProperty(key);
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(props.getProperty(key));
        } catch (NumberFormatException e) {
            ToolkitEngine.getEngine().fireErrorMsgWindow(
                    "配置文件格式错误: " + CONFIG_FILE + " - " + key + "的值应为正整数。");
        }
        return 0;
    }

    public void set(String key, String value) {
        props.setProperty(key, value);
        save();
    }

    public MapGenConfigs() {
        props = new Properties();
        if (!CONFIG_FILE.isFile()) {
            JOptionPane.showMessageDialog(null,
                    "找不到配置文件" + CONFIG_FILE.getAbsolutePath() + "，启动失败。",
                    ToolkitEngine.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        try {
            InputStream is = new FileInputStream(CONFIG_FILE);
            props.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            OutputStream os = new FileOutputStream(CONFIG_FILE);
            props.store(os, null);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

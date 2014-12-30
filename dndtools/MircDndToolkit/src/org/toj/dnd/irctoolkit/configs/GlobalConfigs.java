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

public class GlobalConfigs {
    public static final String CONF_IRC_SERV_HOST = "irc.server.host";
    public static final String CONF_IRC_SERV_PORT = "irc.server.port";
    public static final String CONF_IRC_SERV_PASSWORD = "irc.server.password";
    public static final String CONF_IRC_NICKNAME = "irc.nickname";
    public static final String CONF_IRC_NICKNAME_ALT = "irc.nickname.alt";
    public static final String CONF_IRC_ENCODING = "irc.server.encoding";

    public static final String CONF_LAST_GAME = "last.game";
    public static final String CONF_LAST_MAP = "last.map";
    public static final String CONF_LAST_ENCOUNTER = "last.encounter";

    private static final File CONFIG_FILE = new File(
            "./config/GlobalConfigs.properties");
    private static final String DEFAULT_CONFIG_FILE = "/GlobalConfigs.properties";
    private static GlobalConfigs INSTANCE;

    public static GlobalConfigs getConfigs() {
        if (INSTANCE == null) {
            INSTANCE = new GlobalConfigs();
        }
        return INSTANCE;
    }

    private Properties props;

    public String get(String key) {
        return props.getProperty(key);
    }

    public void set(String key, String value) {
        props.setProperty(key, value);
        save();
    }

    private GlobalConfigs() {
        props = new Properties();
        if (CONFIG_FILE.isFile()) {
            try {
                InputStream is = new FileInputStream(CONFIG_FILE);
                props.load(is);
                is.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                InputStream is = this.getClass().getResourceAsStream(
                        DEFAULT_CONFIG_FILE);
                props.load(is);
                is.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(null,
                "找不到配置文件" + CONFIG_FILE.getAbsolutePath() + "，启动失败。",
                ToolkitEngine.ERR_MSG_TITLE, JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    private void save() {
        try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
            }
            OutputStream os = new FileOutputStream(CONFIG_FILE);
            props.store(os, null);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

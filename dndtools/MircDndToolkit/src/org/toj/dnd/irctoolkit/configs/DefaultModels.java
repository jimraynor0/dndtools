package org.toj.dnd.irctoolkit.configs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.map.MapModel;

public class DefaultModels {
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final File DEF_MODEL_FILE = new File(
            "./config/DefaultModels.xml");

    public List<MapModel> loadDefaultModels() {
        ArrayList<MapModel> result = new ArrayList<MapModel>();
        if (!DEF_MODEL_FILE.isFile()) {
            JOptionPane
                    .showMessageDialog(null,
                            "找不到配置文件" + DEF_MODEL_FILE.getAbsolutePath()
                                    + "，无法载入预设图例。",
                            ToolkitEngine.ERR_MSG_TITLE,
                            JOptionPane.ERROR_MESSAGE);
            return result;
        }

        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding(ENCODING_UTF_8);
            Document document = reader.read(DEF_MODEL_FILE);
            Element e = document.getRootElement();
            for (Element o : (List<Element>) e.elements()) {
                result.add(new MapModel(o));
            }
        } catch (DocumentException e) {
            JOptionPane.showMessageDialog(null,
                    "读取配置文件" + DEF_MODEL_FILE.getAbsolutePath()
                            + "失败，无法载入预设图例。", ToolkitEngine.ERR_MSG_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return result;
    }
}

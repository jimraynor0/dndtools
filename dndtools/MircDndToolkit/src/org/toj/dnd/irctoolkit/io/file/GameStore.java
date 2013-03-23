package org.toj.dnd.irctoolkit.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.encounter.Encounter;
import org.toj.dnd.irctoolkit.map.MapGrid;

public class GameStore {
    private static final String ENCODING_UTF_8 = "UTF-8";

    private static String getGameDir(String name) {
        return new StringBuilder("savegames").append(File.separator)
                .append(name).toString();
    }

    private static String getGameFile(String name) {
        return new StringBuilder("savegames").append(File.separator)
                .append(name).append(File.separator).append("game.xml")
                .toString();
    }

    private static String getLogFile(String name) {
        return new StringBuilder("savegames").append(File.separator)
                .append(name).append(File.separator).append("logs.xml")
                .toString();
    }

    private static String getEncounterFile(String name) {
        return new StringBuilder("encounters").append(File.separator)
                .append(name).append(".encounter").toString();
    }

    // private static String getDmNoteFile(String name) {
    // return new StringBuilder("savegames").append(File.separator)
    // .append(name).append(File.separator).append("dmNote.xml")
    // .toString();
    // }

    public static MapGrid loadMap(File gameFile) throws IOException {
        if (!gameFile.exists()) {
            return null;
        }
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding(ENCODING_UTF_8);
            Document document = reader.read(gameFile);
            return loadMap(document.getRootElement());
        } catch (DocumentException e) {
            throw new IOException(e);
        }
    }

    public static MapGrid loadMap(Element gameElement) {
        return new MapGrid(gameElement);
    }

    public static void save(MapGrid map, File mapFile) throws IOException {
        Document doc = DocumentHelper.createDocument();
        doc.setRootElement(map.toXmlElement());

        OutputFormat outFormat = OutputFormat.createPrettyPrint();
        outFormat.setEncoding(ENCODING_UTF_8);

        XMLWriter writer = new XMLWriter(new FileOutputStream(mapFile),
                outFormat);
        writer.write(doc);
        writer.close();
    }

    public static Game loadGame(String name) throws IOException {
        File gameFile = new File(getGameFile(name));
        if (!gameFile.exists()) {
            return null;
        }
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding(ENCODING_UTF_8);
            Document document = reader.read(gameFile);
            return loadGame(document.getRootElement());
        } catch (DocumentException e) {
            throw new IOException(e);
        }
    }

    public static Game loadGame(Element gameElement) {
        return new Game(gameElement);
    }

    public static Game createGame(String name) {
        return new Game(name);
    }

    public static void save(Game game) throws IOException {
        File gameDir = new File(getGameDir(game.getName()));
        if (!gameDir.isDirectory()) {
            gameDir.mkdirs();
        }

        Document doc = DocumentHelper.createDocument();
        doc.setRootElement(game.toXmlElement());

        OutputFormat outFormat = OutputFormat.createPrettyPrint();
        outFormat.setEncoding(ENCODING_UTF_8);

        XMLWriter writer = new XMLWriter(new FileOutputStream(
                getGameFile(game.getName())), outFormat);

        writer.write(doc);
        writer.close();
    }

    public static void saveLog(List<String> lines, String game) {
        File gameDir = new File(getGameDir(game));
        if (!gameDir.isDirectory()) {
            gameDir.mkdirs();
        }
        File log = new File(getLogFile(game));
        if (log.exists()) {
            log.delete();
        }
        try {
            log.createNewFile();
            FileOutputStream fos = new FileOutputStream(log);
            OutputStreamWriter writer = new OutputStreamWriter(fos, ENCODING_UTF_8);
            for (String line : lines) {
                writer.write(line);
                writer.write("\r\n");
            }
            writer.flush();
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadLog(String game) {
        File log = new File(getLogFile(game));
        if (!log.exists()) {
            return new ArrayList<String>(0);
        }

        List<String> lines = new LinkedList<String>();
        try {
            FileInputStream fis = new FileInputStream(log);
            InputStreamReader isr = new InputStreamReader(fis, ENCODING_UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static Encounter loadEncounter(String name) throws IOException {
        File file = new File(getEncounterFile(name));
        if (!file.exists()) {
            return null;
        }
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding(ENCODING_UTF_8);
            Document document = reader.read(file);
            return loadEncounter(document.getRootElement());
        } catch (DocumentException e) {
            throw new IOException(e);
        }
    }

    public static Encounter loadEncounter(Element e) {
        return new Encounter(e);
    }
}

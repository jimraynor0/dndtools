package org.toj.dnd.irctoolkit.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.toj.dnd.irctoolkit.configs.GlobalConfigs;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.game.d6smw.D6smwGame;
import org.toj.dnd.irctoolkit.game.dnd3r.Dnd3rGame;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;
import org.toj.dnd.irctoolkit.map.MapGrid;

public class GameStore {
    private static final String DEFAULT_ENCODING = "UTF-8";

    public static String getEncoding() {
        String encoding = GlobalConfigs.getConfigs().get("file.encoding");
        return encoding == null ? DEFAULT_ENCODING : encoding;
    }

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

    public static MapGrid loadMap(File gameFile) throws IOException {
        if (!gameFile.exists()) {
            return null;
        }
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding(getEncoding());
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
        outFormat.setEncoding(getEncoding());

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
            JAXBContext jaxbContext = JAXBContext.newInstance(Game.class,
                    DracaGame.class, D6smwGame.class, Dnd3rGame.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (Game) unmarshaller.unmarshal(gameFile);
        } catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    public static Game loadSnapshot(Object snapshot) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Game.class,
                    DracaGame.class, D6smwGame.class, Dnd3rGame.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader sr = new StringReader((String) snapshot);
            return (Game) unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            // should never happen;
        }
        return null;
    }

    public static Game createGame(String name, String ruleSet) {
        if ("d6smw".equalsIgnoreCase(ruleSet)) {
            return new D6smwGame(name);
        }
        if ("draca".equalsIgnoreCase(ruleSet)) {
            return new DracaGame(name);
        }
        return new Dnd3rGame(name);
    }

    public static void save(Game game) throws IOException, JAXBException {
        File gameDir = new File(getGameDir(game.getName()));
        if (!gameDir.isDirectory()) {
            gameDir.mkdirs();
        }

        JAXBContext context = JAXBContext.newInstance(Game.class,
                game.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, getEncoding());

        // Write to File
        m.marshal(game, new File(getGameFile(game.getName())));
    }

    public static Object getSnapshot(Game game) {
        try {
            JAXBContext context = JAXBContext.newInstance(Game.class,
                    game.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, getEncoding());

            java.io.StringWriter sw = new StringWriter();
            m.marshal(game, sw);
            return sw.toString();
        } catch (JAXBException e) {
            // should never happen;
        }
        return "";
    }

    public static List<String> listSavedGames() {
        File gameDir = new File(getGameDir("."));
        if (!gameDir.isDirectory()) {
            return Collections.emptyList();
        }
        return Arrays.asList(gameDir.list());
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
            OutputStreamWriter writer = new OutputStreamWriter(fos,
                    getEncoding());
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
            InputStreamReader isr = new InputStreamReader(fis, getEncoding());
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static File loadResourceFile(String game, String fileName) {
        return new File(getGameDir(game) + File.separator + fileName);
    }
}

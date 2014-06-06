package org.toj.common.irc.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Log2BbsCodeTranslater {

    private static final String OUTPUT_FILE = "/home/likewise-open/NRO/yzhai/git/dndtools/dndtools//MircDndToolkit/resources/translated.log";
    private static final String INPUT_FILE = "/home/likewise-open/NRO/yzhai/git/dndtools/dndtools//MircDndToolkit/resources/#trpg-jlu.log";
    
    private static final char TOKEN = (char) 3;
    private static final char SPACE = (char) 12288;
    private static final char SPACE_REPALCEMENT = (char) 9632;

    private static final String[] BBS_COLORS = { "white", "black", "#00007F",
            "#007F00", "#FF0000", "#7F0000", "#9A0A9A", "#FF7F00", "#FFFF4A",
            "#00FF00", "#00988A", "#00FFFF", "#0000FF", "#FF00FF", "#7F7F7F",
            "#D2D2D2" };

    private static BufferedWriter writer;

    /**
     * @param args
     */
    public static void main(String[] args) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        File folderNameFile = new File(INPUT_FILE);

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fis = new FileInputStream(folderNameFile);
            isr = new InputStreamReader(fis);
            reader = new BufferedReader(isr);

            fos = new FileOutputStream(new File(OUTPUT_FILE));
            osw = new OutputStreamWriter(fos);
            writer = new BufferedWriter(osw);

            String line = "";
            // line = reader.readLine().replaceAll("" + TOKEN + TOKEN, "" +
            // TOKEN);

            print("[font=ËÎÌå]", -1, true);
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("" + (char) 15, "");
                line = line.replaceAll("" + TOKEN + TOKEN, "" + TOKEN);
                if (line.indexOf("Palbot") != -1 && line.indexOf("¡¼") != -1) {
                    continue;
                }
                // if (line.indexOf("DnDBot") != -1) {
                // print(line, -1, true);
                // continue;
                // }

                Color color = null;
                // StringBuffer translated = new StringBuffer();
                if (line.indexOf(TOKEN) == -1) {
                    if (line.indexOf("changes topic to") != -1) {
                        print(line, 3, true);
                    } else {
                        print(line, 6, true);
                    }
                } else {
                    String[] parsed = line.split("" + TOKEN);

                    for (int i = 0; i < parsed.length; i++) {
                        color = extractColor(parsed[i], color);
                        String segment = parsed[i];
                        if (color != null) {
                            segment = parsed[i].substring(color.getLength());
                        }
                        if (segment != null && segment.length() > 0) {
                            if (color == null) {
                                print(segment, -1, false);
                            } else {
                                if (segment.contains("" + SPACE)) {
                                    printSegmentWithSpace(segment, color);
                                } else {
                                    print(segment, color.getForeground(), false);
                                }
                            }
                        }
                    }
                    print(null, -1, true);
                }
            }
            print("[/font]", -1, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printSegmentWithSpace(String segment, Color color)
            throws IOException {
        while (!segment.isEmpty()) {
            if (segment.matches(SPACE + "*")) {
                print(segment.replaceAll("" + SPACE, "" + SPACE_REPALCEMENT),
                        color.getBackground(), false);
                return;
            }
            if (!segment.contains("" + SPACE)) {
                print(segment, color.getForeground(), false);
                return;
            }

            int pos = segment.length();
            if (segment.startsWith("" + SPACE)) {
                for (int i = 0; i < segment.length(); i++) {
                    if (segment.charAt(i) != SPACE) {
                        pos = i;
                        break;
                    }
                }
                print(segment.substring(0, pos).replaceAll("" + SPACE,
                        "" + SPACE_REPALCEMENT), color.getBackground(), false);
            } else {
                pos = segment.indexOf("" + SPACE);
                print(segment.substring(0, pos), color.getForeground(), false);
            }
            segment = segment.substring(pos);
        }
    }

    private static Color extractColor(String str, Color prevColor) {
        System.out.println("extracting: " + str);
        try {
            Color color = new Color();
            if (str == null || str.length() < 2) {
                return null;
            }

            int foregroundCodeLength = 2;
            String foreground = str.substring(0, foregroundCodeLength);
            try {
                int foregroundCode = Integer.parseInt(foreground);
                if (foregroundCode >= BBS_COLORS.length) {
                    foregroundCodeLength = 1;
                    color.setForeground(Integer.parseInt(foreground.substring(
                            0, foregroundCodeLength)));
                } else {
                    color.setForeground(foregroundCode);
                }
            } catch (NumberFormatException e) {
                try {
                    foregroundCodeLength = 1;
                    color.setForeground(Integer.parseInt(foreground.substring(
                            0, foregroundCodeLength)));
                } catch (NumberFormatException e1) {
                    return null;
                }
            }
            System.out.println("foreground set: " + color.getForeground());
            if (str.length() >= foregroundCodeLength + 3
                    && ',' == str.charAt(foregroundCodeLength)) {
                String background = str.substring(foregroundCodeLength + 1,
                        foregroundCodeLength + 3);
                try {
                    color.setBackground(Integer.parseInt(background));
                    color.setLength(foregroundCodeLength + 3);
                } catch (NumberFormatException e) {
                    // this should not happen
                    e.printStackTrace();
                    return null;
                }
            } else {
                if (prevColor != null) {
                    color.setBackground(prevColor.getBackground());
                }
                color.setLength(foregroundCodeLength);
            }
            return color;
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(str);
            System.out.println(str.length());
            throw e;
        }
    }

    private static void print(String text, int colorCode, boolean newline)
            throws IOException {
        if (text != null && !text.isEmpty()) {
            if (colorCode != -1) {
                writer.write("[color=" + BBS_COLORS[colorCode] + "]");
                writer.write(text);
                writer.write("[/color]");
            } else {
                writer.write(text);
            }
        }

        if (newline) {
            writer.newLine();
        }
    }
}

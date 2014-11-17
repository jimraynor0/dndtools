package org.toj.dnd.irctoolkit.engine;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.game.battle.Battle;
import org.toj.dnd.irctoolkit.io.clipboard.ClipboardAccessor;

public class ConsoleTopicManager {
    private static Logger log = Logger.getLogger(ConsoleTopicManager.class);

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Battle title = new Battle();

        ClipboardAccessor clipboard = ClipboardAccessor.getInstance();
        Scanner scanner = new Scanner(System.in);
        String cmd = "";
        while (true) {
            String titleText = title.toString();
            if (titleText.isEmpty()) {
                log.debug("title is empty.");
            } else {
                log.debug("title: " + titleText);
            }
            if (title.getCurrent() != null) {
                log.debug("current acting: " + title.getCurrent().getName());
            }
            cmd = scanner.nextLine();
            try {
                // Command.buildCommand(cmd, addr, port).process(cmd, title);
                clipboard.setClipboardContents(title.toString());
            } catch (Exception e) {
                // pretend nothing happened...
            }
        }
    }
}

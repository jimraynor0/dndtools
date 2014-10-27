package org.toj.dnd.irctoolkit.engine.command;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.pircbot.IrcClient;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class IrcCommandFactory {

    private static Logger log = Logger.getLogger(IrcCommandFactory.class);

    private static List<Class<? extends GameCommand>> cmdClasses = new ArrayList<Class<? extends GameCommand>>();

    static {
        Reflections reflections = new Reflections(
                "org.toj.dnd.irctoolkit.engine.command");
        addToCmdClasses(reflections.getSubTypesOf(GameCommand.class));
    }

    private static void addToCmdClasses(Set<Class<? extends GameCommand>> cmds) {
        for (Class<? extends GameCommand> c : cmds) {
            if (c.isAnnotationPresent(IrcCommand.class)) {
                cmdClasses.add(c);
            }
        }
    }

    private static final String THP = "thp";
    private static final String MINUS = "-";
    private static final String PLUS = "+";
    private static final String HEAL = "heal";
    private static final String DAMAGE = "damage";
    private static final String DMG = "dmg";
    private static final String DAM = "dam";

    private static final String[] SPECIAL_COMMANDS = { "+", "-", "thp", "dmg",
            "heal" };

    public static Command buildCommand(String cmdStr, InetAddress addr, int port) {
        boolean forceUpdateTopic = false;

        if (cmdStr.endsWith(" DiceBot") || cmdStr.endsWith(" Oicebot")) {
            cmdStr = translateDiceBotResult(cmdStr);
        } else {
            if (!cmdStr.startsWith(".")) {
                return null;
            }
            if (cmdStr.startsWith(".t ")) {
                cmdStr = cmdStr.substring(3);
                forceUpdateTopic = true;
            } else {
                cmdStr = cmdStr.substring(1);
            }
        }
        String[] parts = cmdStr.split(" ");
        if (parts.length < 3) {
            return null;
        }
        log.debug("cmdStr trimmed: " + cmdStr);
        parts = separateSpecialCommand(parts);

        GameCommand cmd = buildCmdByType(Arrays.copyOfRange(parts, 0,
                parts.length - 2));
        if (cmd == null) {
            return null;
        } else {
            cmd.incomingAddr = addr;
            cmd.incomingPort = port;

            if (forceUpdateTopic) {
                cmd.setTopicRefreshNeeded(true);
            }

            cmd.chan = parts[parts.length - 2];
            if (cmd.chan == null) {
                log.warn("missing channel parameter!");
                return null;
            }
            cmd.caller = parts[parts.length - 1];
            if (cmd.chan == null) {
                log.warn("missing caller parameter!");
                return null;
            }

            if (cmd.requireGameContext() && Command.context.getGame() == null) {
                IrcClient.getInstance().send(
                        new OutgoingMsg(parts[parts.length - 2],
                                parts[parts.length - 1], "Game not initiated!",
                                OutgoingMsg.WRITE_TO_MSG, null, 0));
                return null;
            }

            return cmd;
        }
    }

    private static String[] separateSpecialCommand(String[] parts) {
        for (String spCmd : SPECIAL_COMMANDS) {
            if (parts[0].startsWith(spCmd)) {
                String[] formalized = new String[parts.length + 1];
                System.arraycopy(parts, 0, formalized, 1, parts.length);
                formalized[0] = spCmd;
                formalized[1] = formalized[1].substring(spCmd.length());
                log.debug("parts formalized: " + Arrays.toString(formalized));
                return formalized;
            }
        }
        return parts;
    }

    private static String translateDiceBotResult(String cmdStr) {
        cmdStr = cmdStr.replaceAll("", "");
        StringBuilder sb = new StringBuilder();

        // remove caller(DiceBot)
        cmdStr = cmdStr.substring(0, cmdStr.lastIndexOf(' '));

        // parse channel name
        String chanName = cmdStr.substring(cmdStr.lastIndexOf(' ') + 1);
        cmdStr = cmdStr.substring(0, cmdStr.lastIndexOf(' '));

        // What's left in cmdStr:
        // Hunk计算dam: 8+5+9=22
        // JimRaynor进行dmg检定: d6+3+4=3+3+4=10
        String actualCmdPrefix = "进行";
        String actualCmdSuffix = "检定";

        if (cmdStr.contains("计算")) {
            actualCmdPrefix = "计算";
            actualCmdSuffix = ": ";
        }

        String caller = cmdStr.substring(0, cmdStr.indexOf(actualCmdPrefix));
        cmdStr = cmdStr.substring(cmdStr.lastIndexOf(actualCmdPrefix)
                + actualCmdPrefix.length());

        String value = cmdStr.substring(cmdStr.lastIndexOf('=') + 1);
        cmdStr = cmdStr.substring(0, cmdStr.lastIndexOf('='));

        String[] cmd = cmdStr.substring(0, cmdStr.lastIndexOf(actualCmdSuffix))
                .split("\\ ");
        if (cmd.length < 1) {
            return "";
        }
        if (cmd[0].equalsIgnoreCase(DMG) || cmd[0].equalsIgnoreCase(MINUS)
                || cmd[0].equalsIgnoreCase(DAMAGE)
                || cmd[0].equalsIgnoreCase(DAM)) {
            cmd[0] = MINUS + value;
        } else if (cmd[0].equalsIgnoreCase(PLUS)
                || cmd[0].equalsIgnoreCase(HEAL)) {
            cmd[0] = PLUS + value;
        } else if (cmd[0].equalsIgnoreCase(THP)) {
            cmd[0] = THP + value;
        } else {
            cmd[0] = cmd[0] + " " + value;
        }

        for (String part : cmd) {
            sb.append(part).append(' ');
        }
        String actor = caller;
        if (getGame() != null && getGame().hasAliases(caller)) {
            actor = getGame().mapAlias(caller);
            if (log.isDebugEnabled()) {
                log.debug("alias mapped: " + caller + " -> " + actor);
            }
        }
        sb.append(chanName.trim()).append(' ').append(caller.trim());
        return sb.toString();
    }

    private static Game getGame() {
        return ToolkitEngine.getEngine().getContext().getGame();
    }

    private static GameCommand buildCmdByType(String[] parts) {
        for (Class<? extends GameCommand> c : cmdClasses) {
            IrcCommand anno = (IrcCommand) c.getAnnotation(IrcCommand.class);
            if (anno != null && getInterpreter(anno).matches(parts)) {
                try {
                    return (GameCommand) c
                            .getConstructor(Object[].class)
                            .newInstance(
                                    new Object[] { getInterpreter(anno)
                                            .sortArgs(
                                                    getTheRestOfTheParams(parts)) });
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        }
        return null;
    }

    private static Map<IrcCommand, IrcCommandPatternInterpreter> interpreters = new HashMap<IrcCommand, IrcCommandPatternInterpreter>();

    private static IrcCommandPatternInterpreter getInterpreter(IrcCommand anno) {
        if (!interpreters.containsKey(anno)) {
            interpreters.put(anno, new IrcCommandPatternInterpreter(anno));
        }
        return interpreters.get(anno);
    }

    private static String[] getTheRestOfTheParams(String[] parts) {
        return Arrays.copyOfRange(parts, 1, parts.length);
    }

    public static void main(String[] args) {
        GameCommand cmd = IrcCommandFactory.buildCmdByType(new String[] {
                "actas", "someguy" });
        System.out.println(cmd);
        GameCommand cmd1 = IrcCommandFactory.buildCmdByType(new String[] {
                "actas1", "someguy" });
        System.out.println(cmd1);
        GameCommand cmd2 = IrcCommandFactory.buildCmdByType(new String[] {
                "acta", "someguy" });
        System.out.println(cmd2);
        GameCommand cmd3 = IrcCommandFactory
                .buildCmdByType(new String[] { "actas" });
        System.out.println(cmd3);
        GameCommand cmd4 = IrcCommandFactory.buildCmdByType(new String[] {
                "actas", "someguy", "someotherguy" });
        System.out.println(cmd4);
    }
}

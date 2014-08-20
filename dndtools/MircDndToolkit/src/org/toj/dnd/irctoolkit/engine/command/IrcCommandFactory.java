package org.toj.dnd.irctoolkit.engine.command;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.game.ActAsCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ActionPointsCommand;
import org.toj.dnd.irctoolkit.engine.command.game.AddCharCommand;
import org.toj.dnd.irctoolkit.engine.command.game.AddPcCommand;
import org.toj.dnd.irctoolkit.engine.command.game.AddPowerCommand;
import org.toj.dnd.irctoolkit.engine.command.game.AddStateCommand;
import org.toj.dnd.irctoolkit.engine.command.game.CharStateCommand;
import org.toj.dnd.irctoolkit.engine.command.game.CreateOrLoadCommand;
import org.toj.dnd.irctoolkit.engine.command.game.D6sDiceRollCommand;
import org.toj.dnd.irctoolkit.engine.command.game.DamageCommand;
import org.toj.dnd.irctoolkit.engine.command.game.EndBattleCommand;
import org.toj.dnd.irctoolkit.engine.command.game.EndCommand;
import org.toj.dnd.irctoolkit.engine.command.game.FateDiceRollCommand;
import org.toj.dnd.irctoolkit.engine.command.game.GameUndoCommand;
import org.toj.dnd.irctoolkit.engine.command.game.GotoCommand;
import org.toj.dnd.irctoolkit.engine.command.game.HealCommand;
import org.toj.dnd.irctoolkit.engine.command.game.InitCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ListAliasCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ListItemCommand;
import org.toj.dnd.irctoolkit.engine.command.game.LogCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ModifyXpCommand;
import org.toj.dnd.irctoolkit.engine.command.game.MoveCharAfterCommand;
import org.toj.dnd.irctoolkit.engine.command.game.MoveCharBeforeCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ObtainItemCommand;
import org.toj.dnd.irctoolkit.engine.command.game.PowerGroupCommand;
import org.toj.dnd.irctoolkit.engine.command.game.PreCommand;
import org.toj.dnd.irctoolkit.engine.command.game.PsionicPointCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ReadPowerCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RefreshTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RegainPowerCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RemoveAliasCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RemoveCharCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RemovePcCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RemovePowerCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RemoveStateCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RenameCharCommand;
import org.toj.dnd.irctoolkit.engine.command.game.RuleQueryCommand;
import org.toj.dnd.irctoolkit.engine.command.game.SaveStateCommand;
import org.toj.dnd.irctoolkit.engine.command.game.SetCommand;
import org.toj.dnd.irctoolkit.engine.command.game.ShowTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.game.StartBattleCommand;
import org.toj.dnd.irctoolkit.engine.command.game.StartRoundCommand;
import org.toj.dnd.irctoolkit.engine.command.game.SurgeCommand;
import org.toj.dnd.irctoolkit.engine.command.game.TempHitPointsCommand;
import org.toj.dnd.irctoolkit.engine.command.game.UseItemCommand;
import org.toj.dnd.irctoolkit.engine.command.game.UsePowerCommand;
import org.toj.dnd.irctoolkit.engine.command.map.CreateMapObjectCommand;
import org.toj.dnd.irctoolkit.engine.command.map.ListModelCommand;
import org.toj.dnd.irctoolkit.engine.command.map.LoadMapFromFileCommand;
import org.toj.dnd.irctoolkit.engine.command.map.MoveMapObjectCommand;
import org.toj.dnd.irctoolkit.engine.command.map.PlaceRandomTrapCommand;
import org.toj.dnd.irctoolkit.engine.command.map.PrintMapCommand;
import org.toj.dnd.irctoolkit.engine.command.map.PrintMapForDmCommand;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.pircbot.IrcClient;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class IrcCommandFactory {

    private static Logger log = Logger.getLogger(IrcCommandFactory.class);

    private static List<Class<? extends GameCommand>> cmdClasses =
        new ArrayList<Class<? extends GameCommand>>();

    static {
        cmdClasses.add(ActAsCommand.class);
        cmdClasses.add(ActionPointsCommand.class);
        cmdClasses.add(AddCharCommand.class);
        cmdClasses.add(AddPcCommand.class);
        cmdClasses.add(AddPowerCommand.class);
        cmdClasses.add(HealCommand.class);
        cmdClasses.add(AddStateCommand.class);
        cmdClasses.add(CharStateCommand.class);
        cmdClasses.add(CreateOrLoadCommand.class);
        cmdClasses.add(D6sDiceRollCommand.class);
        cmdClasses.add(DamageCommand.class);
        cmdClasses.add(EndBattleCommand.class);
        cmdClasses.add(EndCommand.class);
        cmdClasses.add(FateDiceRollCommand.class);
        cmdClasses.add(GameUndoCommand.class);
        cmdClasses.add(GotoCommand.class);
        cmdClasses.add(InitCommand.class);
        cmdClasses.add(ListAliasCommand.class);
        cmdClasses.add(ListItemCommand.class);
        cmdClasses.add(LogCommand.class);
        cmdClasses.add(ModifyXpCommand.class);
        cmdClasses.add(MoveCharAfterCommand.class);
        cmdClasses.add(MoveCharBeforeCommand.class);
        cmdClasses.add(ObtainItemCommand.class);
        cmdClasses.add(PowerGroupCommand.class);
        cmdClasses.add(PreCommand.class);
        cmdClasses.add(PsionicPointCommand.class);
        cmdClasses.add(ReadPowerCommand.class);
        cmdClasses.add(RefreshTopicCommand.class);
        cmdClasses.add(RegainPowerCommand.class);
        cmdClasses.add(RemoveAliasCommand.class);
        cmdClasses.add(RemoveCharCommand.class);
        cmdClasses.add(RemovePcCommand.class);
        cmdClasses.add(RemovePowerCommand.class);
        cmdClasses.add(RemoveStateCommand.class);
        cmdClasses.add(RenameCharCommand.class);
        cmdClasses.add(RuleQueryCommand.class);
        cmdClasses.add(SaveStateCommand.class);
        cmdClasses.add(SetCommand.class);
        cmdClasses.add(ShowTopicCommand.class);
        cmdClasses.add(StartBattleCommand.class);
        cmdClasses.add(StartRoundCommand.class);
        cmdClasses.add(SurgeCommand.class);
        cmdClasses.add(TempHitPointsCommand.class);
        cmdClasses.add(UseItemCommand.class);
        cmdClasses.add(UsePowerCommand.class);

        cmdClasses.add(LoadMapFromFileCommand.class);
        cmdClasses.add(PrintMapCommand.class);
        cmdClasses.add(PrintMapForDmCommand.class);
        cmdClasses.add(ListModelCommand.class);
        cmdClasses.add(MoveMapObjectCommand.class);
        cmdClasses.add(CreateMapObjectCommand.class);
        cmdClasses.add(PlaceRandomTrapCommand.class);
    }

    private static final String START_GAME = "startgame";
    private static final String UNDO = "undo";

    private static final String CHARSTAT = "charstat";
    private static final String RULE = "rule";
    private static final String READ_POWER = "readpower";

    private static final String ADD_PC = "addpc";
    private static final String REMOVE_PC = "removepc";
    private static final String SET = "set";
    private static final String ADD_POWER = "addpower";
    private static final String REMOVE_POWER = "removepower";
    private static final String POWER = "power";
    private static final String REGAIN_POWER = "regainpower";
    private static final String POWER_GROUP = "powergroup";
    private static final String AP = "ap";
    private static final String SURGE = "surge";
    private static final String PP = "pp";
    private static final String SHORT_REST = "shortrest";
    private static final String EXTENDED_REST = "extendedrest";
    private static final String LONG_REST = "longrest";
    private static final String MILESTONE = "milestone";
    private static final String GAINXP = "gainxp";
    private static final String LOSEXP = "losexp";

    private static final String STARTBATTLE = "startbattle";
    private static final String ENDBATTLE = "endbattle";
    private static final String SURPRISE = "surprise";
    private static final String START = "start";
    private static final String INIT = "init";
    private static final String END = "end";
    private static final String PRE = "pre";
    private static final String GO = "go";
    private static final String THP = "thp";
    private static final String BEFORE = "before";
    private static final String AFTER = "after";
    private static final String RENAME = "rename";
    private static final String SAVE = "save";
    private static final String SV = "sv";
    private static final String MINUS = "-";
    private static final String PLUS = "+";
    private static final String HEAL = "heal";
    private static final String DAMAGE = "damage";
    private static final String DMG = "dmg";
    private static final String DAM = "dam";

    private static final String MAP = "map";
    private static final String MOVE = "move";

    private static final String LOG = "log";

    private static final String SHOW_TOPIC = "showtopic";

    private static final String D6S = "d6s";

    private static final String[] SPECIAL_COMMANDS = { "+", "-", "thp", "dmg",
        "heal" };

    public static Command
        buildCommand(String cmdStr, InetAddress addr, int port) {
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

        GameCommand cmd =
            buildCmdByType(Arrays.copyOfRange(parts, 0, parts.length - 2));
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
        // Hunk¼ÆËãdam: 8+5+9=22
        // JimRaynor½øÐÐdmg¼ì¶¨: d6+3+4=3+3+4=10
        String actualCmdPrefix = "½øÐÐ";
        String actualCmdSuffix = "¼ì¶¨";

        if (cmdStr.contains("¼ÆËã")) {
            actualCmdPrefix = "¼ÆËã";
            actualCmdSuffix = ": ";
        }

        String caller = cmdStr.substring(0, cmdStr.indexOf(actualCmdPrefix));
        cmdStr =
            cmdStr.substring(cmdStr.lastIndexOf(actualCmdPrefix)
                + actualCmdPrefix.length());

        String value = cmdStr.substring(cmdStr.lastIndexOf('=') + 1);
        cmdStr = cmdStr.substring(0, cmdStr.lastIndexOf('='));

        String[] cmd =
            cmdStr.substring(0, cmdStr.lastIndexOf(actualCmdSuffix)).split(
                "\\ ");
        if (cmd.length < 1) {
            return "";
        }
        if (cmd[0].equalsIgnoreCase(DMG) || cmd[0].equalsIgnoreCase(MINUS)
            || cmd[0].equalsIgnoreCase(DAMAGE) || cmd[0].equalsIgnoreCase(DAM)) {
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
                    return (GameCommand) c.getConstructor(Object[].class)
                        .newInstance(
                            new Object[] { getInterpreter(anno).sortArgs(
                                getTheRestOfTheParams(parts)) });
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        }

        /*
         * if (parts[0].equalsIgnoreCase(START_GAME)) { return new
         * CreateOrLoadCommand(parts[1]); } if
         * (parts[0].equalsIgnoreCase(CHARSTAT)) { if (parts.length > 1) {
         * return new CharStateCommand(getTheRestOfTheParams(parts)); } else {
         * return new CharStateCommand(); } } if
         * (parts[0].equalsIgnoreCase(ENDBATTLE)) { return new
         * EndBattleCommand(); } if (parts[0].equalsIgnoreCase(STARTBATTLE)) {
         * return new StartBattleCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(START)) { return new StartRoundCommand(1);
         * } if (parts[0].equalsIgnoreCase(SURPRISE)) { return new
         * StartRoundCommand(0); } if (parts[0].equalsIgnoreCase(END)) { return
         * new EndCommand(); } if (parts[0].equalsIgnoreCase(PRE)) { return new
         * PreCommand(); } if (parts[0].equalsIgnoreCase(UNDO)) { return new
         * GameUndoCommand(); } if (parts[0].equalsIgnoreCase(MAP)) { return new
         * PrintMapCommand(); } if (parts[0].equalsIgnoreCase(MOVE)) { if
         * (parts.length > 2) { if (AxisUtil.is2DAxis(parts[2])) { return new
         * MoveMapObjectCommand(parts[1], parts[2].substring(0, 2),
         * parts[2].substring(2)); } else { return new
         * MoveMapObjectCommand(parts[2], parts[1].substring(0, 2),
         * parts[1].substring(2)); } } else { return new
         * MoveMapObjectCommand(parts[1].split(",")[0], parts[1].split(",")[1]);
         * } } if (parts[0].equalsIgnoreCase(GO)) { if (parts.length > 2) {
         * return new GotoCommand(parts[1], Integer.valueOf(parts[2])); } else {
         * return new GotoCommand(parts[1]); } } if
         * (parts[0].equalsIgnoreCase(INIT)) { if (parts.length == 1) { return
         * new InitCommand(); // "init" } else if (parts.length == 2 &&
         * StringNumberUtil.isDouble(parts[1])) { return new
         * InitCommand(Double.parseDouble(parts[1])); // "init double" } else {
         * if (StringNumberUtil.isDouble(parts[1])) { return new
         * InitCommand(Double.parseDouble(parts[1]), Arrays.copyOfRange(parts,
         * 2, parts.length)); // "init double name1 name2..." } else if
         * (StringNumberUtil.isDouble(parts[parts.length - 1])) { return new
         * InitCommand(Double.parseDouble(parts[parts.length - 1]),
         * Arrays.copyOfRange(parts, 1, parts.length - 1)); //
         * "init name1 name2... double" } else { return new
         * InitCommand(getTheRestOfTheParams(parts)); // "init name1 name2..." }
         * } } if (parts[0].equalsIgnoreCase(SV) ||
         * parts[0].equalsIgnoreCase(SAVE)) { if (parts.length == 3) { return
         * new SaveStateCommand(Integer.parseInt(parts[1]), parts[2]); } else {
         * return new SaveStateCommand(Integer.parseInt(parts[1]), parts[2],
         * parts[3]); } } if (parts[0].equalsIgnoreCase(BEFORE)) { if
         * (parts.length == 2) { return new MoveCharBeforeCommand(parts[1]); }
         * else { return new MoveCharBeforeCommand(parts[1], parts[2]); } } if
         * (parts[0].equalsIgnoreCase(AFTER)) { if (parts.length == 2) { return
         * new MoveCharAfterCommand(parts[1]); } else { return new
         * MoveCharAfterCommand(parts[1], parts[2]); } } if
         * (parts[0].equalsIgnoreCase(RENAME)) { return new
         * RenameCharCommand(parts[1], parts[2]); } if
         * (parts[0].equalsIgnoreCase(AP)) { if (parts.length == 1) { return new
         * ActionPointsCommand(); } if (parts.length == 2) { try { return new
         * ActionPointsCommand(Integer.parseInt(parts[1])); } catch
         * (NumberFormatException e) { return new ActionPointsCommand(parts[1]);
         * } } if (parts.length == 3) { try { return new
         * ActionPointsCommand(parts[1], Integer.parseInt(parts[2])); } catch
         * (NumberFormatException e) { return new
         * ActionPointsCommand(Integer.parseInt(parts[1]), parts[2]); } } } if
         * (parts[0].equalsIgnoreCase(SURGE)) { if (parts.length == 1) { return
         * new SurgeCommand(); } if (parts.length == 2) { try { return new
         * SurgeCommand(Integer.parseInt(parts[1])); } catch
         * (NumberFormatException e) { return new SurgeCommand(parts[1]); } } if
         * (parts.length == 3) { try { return new SurgeCommand(parts[1],
         * Integer.parseInt(parts[2])); } catch (NumberFormatException e) {
         * return new SurgeCommand(Integer.parseInt(parts[1]), parts[2]); } } }
         * if (parts[0].equalsIgnoreCase(PP)) { return new
         * PsionicPointCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(LOG)) { return new
         * LogCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(ADD_PC)) { return new
         * AddPcCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(REMOVE_PC)) { return new
         * RemovePcCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(ADD_POWER)) { return new
         * AddPowerCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(REMOVE_POWER)) { return new
         * RemovePowerCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(READ_POWER)) { return new
         * ReadPowerCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(GAINXP)) { return new
         * ModifyXpCommand(Integer.valueOf(parts[1]), Arrays.copyOfRange(parts,
         * 2, parts.length)); } if (parts[0].equalsIgnoreCase(LOSEXP)) { return
         * new ModifyXpCommand(0 - Integer.valueOf(parts[1]),
         * Arrays.copyOfRange(parts, 2, parts.length)); } if
         * (parts[0].equalsIgnoreCase(SET)) { if (parts.length > 3) { try {
         * return new SetCommand(parts[1], Integer.parseInt(parts[2]),
         * parts[3]); } catch (NumberFormatException e) { return new
         * SetCommand(parts[1], parts[2], Integer.parseInt(parts[3])); } } else
         * { return new SetCommand(parts[1], Integer.parseInt(parts[2])); } } if
         * (parts[0].equalsIgnoreCase(SHORT_REST)) { return new
         * RestCommand(SHORT_REST, getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(EXTENDED_REST) ||
         * parts[0].equalsIgnoreCase(LONG_REST)) { return new
         * RestCommand(EXTENDED_REST, getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(MILESTONE)) { return new
         * RestCommand(MILESTONE, getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(POWER)) { return new
         * UsePowerCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(REGAIN_POWER)) { return new
         * RegainPowerCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(POWER_GROUP)) { return new
         * PowerGroupCommand(getTheRestOfTheParams(parts)); } if
         * (parts[0].equalsIgnoreCase(MILESTONE)) { return new
         * RestCommand(MILESTONE, getTheRestOfTheParams(parts)); }
         * 
         * if (parts[0].startsWith(THP)) { if (parts.length == 1) { return new
         * TempHitPointsCommand
         * (Integer.parseInt(parts[0].substring(THP.length()))); } else { return
         * new
         * TempHitPointsCommand(Integer.parseInt(parts[0].substring(THP.length
         * ())), getTheRestOfTheParams(parts)); } }
         * 
         * if (parts[0].startsWith(PLUS)) { String rest = parts[0].substring(1);
         * if (rest.isEmpty()) { return new
         * AddCharCommand(getTheRestOfTheParams(parts)); } if
         * (rest.matches("[0-9]+")) { return new
         * HealCommand(Integer.parseInt(rest), getTheRestOfTheParams(parts)); }
         * 
         * return new AddStateCommand(rest, getTheRestOfTheParams(parts)); } if
         * (parts[0].startsWith(MINUS)) { String rest = parts[0].substring(1);
         * if (rest.isEmpty()) { return new
         * RemoveCharCommand(getTheRestOfTheParams(parts)); } if
         * (rest.matches("[0-9]+")) { return new
         * DamageCommand(Integer.parseInt(rest), getTheRestOfTheParams(parts));
         * }
         * 
         * return new RemoveStateCommand(rest, getTheRestOfTheParams(parts)); }
         * if (parts[0].equalsIgnoreCase(D6S)) { return new
         * D6sDiceRollCommand(Integer.valueOf(parts[1]),
         * Arrays.copyOfRange(parts, 2, parts.length)); }
         */
        return null;
    }

    private static Map<IrcCommand, IrcCommandPatternInterpreter> interpreters =
        new HashMap<IrcCommand, IrcCommandPatternInterpreter>();

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
        GameCommand cmd =
            IrcCommandFactory
                .buildCmdByType(new String[] { "actas", "someguy" });
        System.out.println(cmd);
        GameCommand cmd1 =
            IrcCommandFactory
                .buildCmdByType(new String[] { "actas1", "someguy" });
        System.out.println(cmd1);
        GameCommand cmd2 =
            IrcCommandFactory
                .buildCmdByType(new String[] { "acta", "someguy" });
        System.out.println(cmd2);
        GameCommand cmd3 =
            IrcCommandFactory.buildCmdByType(new String[] { "actas" });
        System.out.println(cmd3);
        GameCommand cmd4 =
            IrcCommandFactory.buildCmdByType(new String[] { "actas", "someguy",
                "someotherguy" });
        System.out.println(cmd4);
    }
}

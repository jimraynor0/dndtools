package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.dice.Dice;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

@IrcCommand(command = "init", args = { CommandSegment.NULLABLE_DOUBLE, CommandSegment.NULLABLE_LIST },
        summary = ".init <先攻值> <PC/NPC名列表> - 将一个或多个PC或NPC按指定先攻值加入战斗。使用.help init查看完整说明。",
        desc = "这个命令有两种用法\n" + 
                "1) 若在命令中指定了先攻值参数，则本命令将一个或多个NPC以指定的先攻值加入战斗。\n" +
                "2) 若在命令中没有指定先攻值参数，则本命令将为PC进行先攻检定并依照其结果将PC加入战斗。本命令不能为NPC进行先攻检定。\n" +
                "若不指定<PC/NPC名列表>则默认以当前名称为PC名参数。若当前游戏未在一场战斗中则这个命令会开启一场新的战斗。")
public class InitCommand extends Dnd3rGameCommand {

    private String[] charName;
    private double init = Double.NEGATIVE_INFINITY;

    public InitCommand(Object[] args) {
        if (args.length > 0 && (args[0] instanceof Double || args[0] instanceof Integer)) {
            init = (Double) args[0];
        }

        if (args.length != 0 && (args.length > 1 || init == Double.NEGATIVE_INFINITY)) {
            if (init != Double.NEGATIVE_INFINITY) {
                charName = new String[args.length - 1];
                System.arraycopy(args, 1, charName, 0, charName.length);
            } else {
                charName = new String[args.length];
                System.arraycopy(args, 0, charName, 0, charName.length);
            }
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (!getGame().inBattle()) {
            getGame().startBattle();
        }
        if (charName == null) {
            charName = new String[] { caller };
        }

        for (String ch : charName) {
            if (init == Double.NEGATIVE_INFINITY) {
                PC pc = getGame().findCharByNameOrAbbre(ch);
                if (pc != null) {
                    int roll = Dice.getDice(20).roll();
                    int init = roll + pc.getInitMod();
                    String initModStr = (pc.getInitMod() >= 0 ? "+" : "") + pc.getInitMod();
                    sendMsg(pc.getName() + "进行先攻检定，结果: 1d20" + initModStr + "=" + roll + initModStr + "=" + init);
                    getGame().addCharByInit(ch, init);
                    continue;
                }
            } else {
                getGame().addCharByInit(ch, init);
            }
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

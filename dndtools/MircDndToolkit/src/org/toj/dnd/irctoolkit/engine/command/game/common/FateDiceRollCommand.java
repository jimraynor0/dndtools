package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.List;

import org.toj.dnd.irctoolkit.dice.FateRoll;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@IrcCommand(command = "4df", args = { CommandSegment.NULLABLE_INT, CommandSegment.NULLABLE_LIST },
        summary = ".4df <属性值> <说明> - fate规则的检定掷骰。投掷4df+属性值(默认为0)，可以包括说明文字来表示掷骰的内容。")
public class FateDiceRollCommand extends GameCommand {

    private int skill = -1;
    private String[] desc;

    public FateDiceRollCommand(Object[] args) {
        int descStartIndex = 0;
        if (args[0] instanceof Integer) {
            skill = (Integer) args[0];
            descStartIndex = 1;
        }

        if (args.length == descStartIndex) {
            desc = new String[0];
        } else {
            desc = new String[args.length - descStartIndex];
            System.arraycopy(args, descStartIndex, desc, 0, desc.length);
        }
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        FateRoll roll = new FateRoll();
        roll.roll();
        StringBuilder result = new StringBuilder(IrcColoringUtil.paint(this.caller, Color.RED.getCode()))
                .append("进行")
                .append(IrcColoringUtil.paint(composite(desc), Color.BLUE.getCode()))
                .append("检定，投掷")
                .append(IrcColoringUtil.paint((skill == -1 ? "" : (skill + "+")), Color.ORANGE.getCode()))
                .append(IrcColoringUtil.paint("4df", Color.PURPLE.getCode()))
                .append("，结果：")
                .append(IrcColoringUtil.paint((skill == -1 ? "" : (skill + (roll.getResultNumber() < 0 ? "" : "+"))),
                        Color.ORANGE.getCode()))
                .append(IrcColoringUtil.paint(String.valueOf(roll.getResultNumber()), Color.PURPLE.getCode()))
                .append("=")
                .append(IrcColoringUtil.paint(String.valueOf((skill == -1 ? 0 : skill) + roll.getResultNumber()),
                        Color.TEAL.getCode()));
        sendMsg(result.toString());
        result = new StringBuilder("掷骰结果：");
        result.append(IrcColoringUtil.paint(roll.getDiceResults().toString(), Color.TEAL.getCode()));
        sendMsg(result.toString());
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

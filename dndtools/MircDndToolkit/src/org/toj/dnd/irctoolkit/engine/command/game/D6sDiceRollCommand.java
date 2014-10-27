package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.dice.D6sRoll;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@IrcCommand(command = "d6s", args = { CommandSegment.INT,
        CommandSegment.NULLABLE_LIST })
public class D6sDiceRollCommand extends GameCommand {

    private int diceNumber;
    private String[] desc;

    public D6sDiceRollCommand(Object[] args) {
        diceNumber = (Integer) args[0];

        if (args.length == 1) {
            desc = new String[0];
        } else {
            desc = new String[args.length - 1];
            System.arraycopy(args, 1, desc, 0, desc.length);
        }
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        D6sRoll roll = new D6sRoll(diceNumber, true);
        roll.roll();
        StringBuilder result = new StringBuilder(this.caller)
                .append("进行")
                .append(IrcColoringUtil.paint(composite(desc),
                        Color.BLUE.getCode()))
                .append("检定，投掷")
                .append(IrcColoringUtil.paint(diceNumber + "d6",
                        Color.TEAL.getCode()))
                .append("(基础 ")
                .append(IrcColoringUtil.paint(
                        String.valueOf(roll.getBaseSucc()),
                        Color.PURPLE.getCode()))
                .append("，掷骰 ")
                .append(IrcColoringUtil.paint(
                        String.valueOf(roll.getDiceNumber()
                                - roll.getBaseSucc()), Color.PURPLE.getCode()))
                .append(")，结果：")
                .append(IrcColoringUtil.paint(
                        String.valueOf(roll.getBaseSucc()),
                        Color.PURPLE.getCode()))
                .append(" + ")
                .append(IrcColoringUtil.paint(
                        String.valueOf(roll.getRolledSucc()),
                        Color.PURPLE.getCode()))
                .append(" = ")
                .append(IrcColoringUtil.paint(
                        String.valueOf(roll.getBaseSucc()
                                + roll.getRolledSucc()), Color.TEAL.getCode()));
        sendMsg(result.toString());
        result = new StringBuilder("掷骰结果：");
        boolean first = true;
        for (int die : roll.getDiceRollResults()) {
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(die > 3 ? IrcColoringUtil.paint(String.valueOf(die),
                    Color.TEAL.getCode()) : die);
        }
        sendMsg(result.toString());
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.Collections;
import java.util.List;

import org.toj.dnd.irctoolkit.dice.WushuRoll;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@IrcCommand(command = "ws", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_INT, CommandSegment.NULLABLE_LIST })
public class WushuDiceRollCommand extends GameCommand {

    private int diceNumber;
    private int threshold = -1;
    private String[] desc;

    public WushuDiceRollCommand(Object[] args) {
        diceNumber = (Integer) args[0];
        if (args[1] != null) {
            threshold = (Integer) args[1];
        }

        if (args.length == 2) {
            desc = new String[0];
        } else {
            desc = new String[args.length - 2];
            System.arraycopy(args, 2, desc, 0, desc.length);
        }
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        WushuRoll roll = new WushuRoll(diceNumber, threshold);
        roll.roll();
        if (threshold > 0) {
            sendMsg(getNormalCheckResult(roll));
        } else {
            sendMsg(getSimpleCheckResult(roll));
        }
        return this.msgs;
    }

    private String getSimpleCheckResult(WushuRoll roll) {
        StringBuilder result =
            new StringBuilder(this.caller)
                .append("进行")
                .append(
                    IrcColoringUtil.paint(composite(desc), Color.BLUE.getCode()))
                .append("简化检定，投掷")
                .append(
                    IrcColoringUtil.paint(diceNumber + "d6",
                        Color.TEAL.getCode())).append("，结果：");
        boolean first = true;
        List<Integer> diceRollResults = roll.getDiceRollResults();
        Collections.sort(diceRollResults);
        Collections.reverse(diceRollResults);
        for (int die : diceRollResults) {
            if (first) {
                first = false;
                result.append(die <= threshold ? IrcColoringUtil.paint(
                    String.valueOf(die), Color.PURPLE.getCode()) : die);
            } else {
                result.append(", ");
                result.append(die <= threshold ? IrcColoringUtil.paint(
                    String.valueOf(die), Color.TEAL.getCode()) : die);
            }
        }
        return result.toString();
    }

    private String getNormalCheckResult(WushuRoll roll) {
        StringBuilder result =
            new StringBuilder(this.caller)
                .append("进行")
                .append(
                    IrcColoringUtil.paint(composite(desc), Color.BLUE.getCode()))
                .append("检定，投掷")
                .append(
                    IrcColoringUtil.paint(diceNumber + "d6",
                        Color.TEAL.getCode()))
                .append("(特质")
                .append(
                    IrcColoringUtil.paint(String.valueOf(threshold),
                        Color.TEAL.getCode()))
                .append(")")
                .append("，结果：")
                .append(
                    IrcColoringUtil.paint(String.valueOf(roll.getSucc()),
                        Color.PURPLE.getCode())).append(" : ");
        boolean first = true;
        for (int die : roll.getDiceRollResults()) {
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(die <= threshold ? IrcColoringUtil.paint(
                String.valueOf(die), Color.TEAL.getCode()) : die);
        }
        return result.toString();
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

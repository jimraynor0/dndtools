package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.List;

import org.toj.dnd.irctoolkit.dice.ShadowrunRoll;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@IrcCommand(command = "sr", args = { CommandSegment.INT, CommandSegment.NULLABLE_INT, CommandSegment.NULLABLE_LIST })
public class ShadowrunDiceRollCommand extends GameCommand {

    private int diceNumber;
    private int limit = Integer.MAX_VALUE;
    private String[] desc;

    public ShadowrunDiceRollCommand(Object[] args) {
        diceNumber = (Integer) args[0];
        if (args[1] != null) {
          limit = (Integer) args[1];
        }

        if (args.length <= 2) {
            desc = new String[0];
        } else {
            desc = new String[args.length - 2];
            System.arraycopy(args, 2, desc, 0, desc.length);
        }
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
      ShadowrunRoll roll = new ShadowrunRoll(diceNumber, limit);
        roll.roll();
        StringBuilder result = new StringBuilder(this.caller)
                .append("进行")
                .append(IrcColoringUtil.paint(composite(desc),
                        Color.BLUE.getCode()))
                .append("检定，投掷")
                .append(IrcColoringUtil.paint(diceNumber + "d6",
                        Color.TEAL.getCode()))
                .append("，结果：")
                .append(buildResultString(roll));
        sendMsg(result.toString());

        result = new StringBuilder("掷骰结果：");
        boolean first = true;
        int succCount = 0;
        for (int die : roll.getDiceRollResults()) {
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            Color color = Color.GRAY;
            if (die > 4) {
                succCount++;
                if (succCount > roll.getLimit()) {
                    color = Color.BLACK;
                } else {
                    color = Color.TEAL;
                }
            }
            result.append(IrcColoringUtil.paint(String.valueOf(die), color.getCode()));
        }
        sendMsg(result.toString());
        return this.msgs;
    }

    private String buildResultString(ShadowrunRoll roll) {
        StringBuilder result = new StringBuilder(
                IrcColoringUtil.paint(String.valueOf(Math.min(roll.getSucc(), roll.getLimit())), Color.PURPLE.getCode()));
        if (roll.getLimit() < roll.getSucc()) {
            result.append("(")
                .append(IrcColoringUtil.paint(String.valueOf(roll.getSucc()), Color.GRAY.getCode()))
                .append(")");
        }
        if (roll.isCriticalGlitch()) {
            result.append(IrcColoringUtil.paint("，Critical Glitch", Color.RED.getCode()));
        } else if (roll.isGlitch()) {
            result.append(IrcColoringUtil.paint("，Glitch", Color.RED.getCode()));
        }
        return result.toString();
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

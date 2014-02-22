package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.dice.Dice;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;
import org.toj.dnd.irctoolkit.game.encounter.NPC;

@IrcCommand(command = "init", args = { CommandSegment.NULLABLE_DOUBLE, CommandSegment.NULLABLE_LIST } )
public class InitCommand extends UndoableTopicCommand {

    private String[] charName;
    private double init = Double.NEGATIVE_INFINITY;

    public InitCommand(Object[] args) {
        if (args[0] != null) {
            init = (Double) args[0];
        }

        if (args.length == 1) {
            charName = new String[] {caller};
        } else {
            charName = new String[args.length - 1];
            System.arraycopy(args, 1, charName, 0, charName.length);
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
                    String initModStr = (pc.getInitMod() >= 0 ? "+" : "")
                            + pc.getInitMod();
                    sendMsgToDefaultChan(pc.getName() + "进行先攻检定，结果: 1d20"
                            + initModStr + "=" + roll + initModStr + "=" + init);
                    getGame().addCharByInit(ch, init);
                    continue;
                }
                NPC npc = getGame().getNpcs().get(ch);
                if (npc != null) {
                    int roll = Dice.getDice(20).roll();
                    int init = roll + npc.getInitMod();
                    String initModStr = (npc.getInitMod() >= 0 ? "+" : "")
                            + npc.getInitMod();
                    sendMsgToDefaultChan(npc.getName() + "进行先攻检定，结果: 1d20"
                            + initModStr + "=" + roll + initModStr + "=" + init);
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

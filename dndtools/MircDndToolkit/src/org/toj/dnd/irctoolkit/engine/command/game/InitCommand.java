package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.Arrays;

import org.toj.dnd.irctoolkit.dice.Dice;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;
import org.toj.dnd.irctoolkit.game.encounter.NPC;
import org.toj.dnd.irctoolkit.util.StringNumberUtil;

@IrcCommand(patterns = { "init" }, argsMin = 1)
public class InitCommand extends UndoableTopicCommand {

    private String[] charName;
    private double init = Double.NEGATIVE_INFINITY;

    public InitCommand(String[] args) {
        if (args.length == 2 && StringNumberUtil.isDouble(args[1])) {// "init double"
            this.init = Double.parseDouble(args[1]);
        } else {
            if (StringNumberUtil.isDouble(args[1])) {// "init double name1 name2..."
                this.init = Double.parseDouble(args[1]);
                this.charName = Arrays.copyOfRange(args, 2, args.length);
            } else if (StringNumberUtil.isDouble(args[args.length - 1])) {
                this.init = Double.parseDouble(args[args.length - 1]);
                this.charName = Arrays.copyOfRange(args, 1, args.length - 1);
            } else {
                this.charName = Arrays.copyOfRange(args, 1, args.length);
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

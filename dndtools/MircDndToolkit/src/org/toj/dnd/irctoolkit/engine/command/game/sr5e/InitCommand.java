package org.toj.dnd.irctoolkit.engine.command.game.sr5e;

import org.toj.dnd.irctoolkit.dice.Dice;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

@IrcCommand(command = "init", args = { CommandSegment.NULLABLE_INT,
        CommandSegment.NULLABLE_LIST }, summary = ".init 先攻值 <PC/NPC名列表> - 将一个或多个PC或NPC按指定先攻值加入战斗。若不指定<PC/NPC名列表>则默认以当前名称为PC名参数。若当前游戏未在一场战斗中则这个命令会开启一场新的战斗。")
public class InitCommand extends Sr5eGameCommand {

    private String[] charName;
    private int init;

    public InitCommand(Object[] args) {
        init = (Integer) args[0];

        if (args.length > 1) {
            charName = new String[args.length - 1];
            System.arraycopy(args, 1, charName, 0, charName.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (!getGame().isInBattle()) {
            getGame().startBattle();
        }
        if (charName == null) {
            charName = new String[] { caller };
        }

        for (String ch : charName) {
            if (getGame().isPc(ch)) {
                getGame().getBattle().addCombatantByInit(getGame().getPc(ch), init);
            }
            getGame().getBattle().addCombatantByInit(ch, init);
        }
        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }
}

package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.dice.Dice;
import org.toj.dnd.irctoolkit.dice.FateRoll;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.d6smw.Mech;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IrcColoringUtil;

@IrcCommand(command = "fire", args = { CommandSegment.NULLABLE_INT,
        CommandSegment.LIST })
public class FireCommand extends D6smwGameCommand {

    private String name;
    private String[] equipments;
    private int skill = -1;

    public FireCommand(Object[] args) {
        if (args[0] != null) {
            skill = (Integer) args[0];
        }
        int weaponsParamIndex = 1;
        if (getGame().getMech((String) args[weaponsParamIndex]) != null) {
            name = (String) args[weaponsParamIndex];
            weaponsParamIndex++;
        }
        this.equipments = new String[args.length - weaponsParamIndex];
        System.arraycopy(args, weaponsParamIndex, this.equipments, 0,
                equipments.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        Mech mech = null;
        if (StringUtils.isEmpty(name)) {
            mech = getGame().getMech(caller);
        } else {
            mech = getGame().getMech(name);
        }
        for (String eq : equipments) {
            String result = getGame().activateEquipment(mech, eq);
            if (!StringUtils.isEmpty(result)) {
                sendMsg(result);
            } else {
                FateRoll roll = new FateRoll();
                roll.roll();
                StringBuilder rollResult = new StringBuilder(
                        IrcColoringUtil.paint(this.caller, Color.RED.getCode()))
                        .append("进行")
                        .append(IrcColoringUtil.paint(eq + "攻击",
                                Color.BLUE.getCode()))
                        .append("检定，投掷")
                        .append(IrcColoringUtil.paint((skill == -1 ? ""
                                : (skill + "+")), Color.ORANGE.getCode()))
                        .append(IrcColoringUtil.paint("4df",
                                Color.PURPLE.getCode()))
                        .append("，结果：")
                        .append(IrcColoringUtil.paint((skill == -1 ? ""
                                : (skill + (roll.getResultNumber() < 0 ? ""
                                        : "+"))), Color.ORANGE.getCode()))
                        .append(IrcColoringUtil.paint(
                                String.valueOf(roll.getResultNumber()),
                                Color.PURPLE.getCode()))
                        .append("=")
                        .append(IrcColoringUtil.paint(
                                String.valueOf((skill == -1 ? 0 : skill)
                                        + roll.getResultNumber()),
                                Color.TEAL.getCode()))
                        .append(" ")
                        .append(IrcColoringUtil.paint(roll.getDiceResults()
                                .toString(), Color.TEAL.getCode()));
                int section = Dice.getDice(6).roll();
                rollResult.append("；攻击部位: ").append(
                        IrcColoringUtil.paint(getSection(section) + "("
                                + section + ")", Color.TEAL.getCode()));
                ;
                sendMsg(rollResult.toString());
            }
        }

        sendTopic(getGame().generateTopic());
        if (topicRefreshNeeded) {
            refreshTopic();
        }
    }

    private String getSection(int section) {
        if (section == 4) {
            return "左臂";
        }
        if (section == 5) {
            return "右臂";
        }
        if (section == 6) {
            return "腿";
        }
        return "胸腹";
    }
}

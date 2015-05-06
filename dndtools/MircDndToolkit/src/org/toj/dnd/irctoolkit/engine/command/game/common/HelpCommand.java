package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "help", args = { CommandSegment.NULLABLE_STRING },
        summary = ".help <命令> - 列出当前游戏支持的所有命令，可以加上<命令>参数来查看该命令的详细解释(不是所有的命令都有)。")
public class HelpCommand extends GameCommand {

    private String command = null;

    public HelpCommand(Object[] args) {
        if (args != null && args.length > 0) {
            command = (String) args[0];
        }
    }

    @Override
    public List<OutgoingMsg> execute() {
        boolean commandFound = command == null;
        List<Class<? extends GameCommand>> cmdClasses = IrcCommandFactory.getCmdClasses();
        for (Class<? extends GameCommand> c : cmdClasses) {
            IrcCommand anno = c.getAnnotation(IrcCommand.class);
            // skip command that doesn't have help text
            if (StringUtils.isEmpty(anno.summary())) {
                continue;
            }

            // if command name is not provided, print summary of all commands.
            if (command == null) {
                sendMsg(anno.summary());
            } else if (command.equalsIgnoreCase(anno.command())) {
                // if command name is provided, print the corresponding
                // command's description.
                sendMsg(anno.summary());
                if (!StringUtils.isEmpty(anno.desc())) {
                    for (String line : anno.desc().split("\n")) {
                        sendMsg(line);
                    }
                }
                commandFound = true;
                break;
            }
        }
        if (!commandFound) {
            sendMsg(command + "命令不存在。");
        }
        if (command == null) {
            Collections.sort(msgs, new Comparator<OutgoingMsg>() {
                @Override
                public int compare(OutgoingMsg o1, OutgoingMsg o2) {
                    return o1.getContent().compareTo(o2.getContent());
                }
            });
        }
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

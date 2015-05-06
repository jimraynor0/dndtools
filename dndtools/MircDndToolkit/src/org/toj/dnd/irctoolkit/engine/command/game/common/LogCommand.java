package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.log.GameLogManager;

@IrcCommand(command = "log", args = { CommandSegment.LIST }, summary = ".log <命令> 命令参数 - 团内日志功能，使用.help log查看全部说明。",
        desc = "log命令包含了三个功能，记录、查询和删除日志。\n" + ".log 日志标题|日志内容 - 记录一条日志。日志将被保存到游戏存档目录中，可以随时在之后的团中查看。\n"
                + ".log query <关键字> - 查找标题或内容包含<关键字>的日志。若不提供关键字则会显示所有日志。\n"
                + ".log delete 标题或日期参数 - 删除所有标题或日期与参数相同的日志。")
public class LogCommand extends UndoableTopicCommand {

    private String[] args;

    public LogCommand(Object[] args) {
        this.args = new String[args.length];
        System.arraycopy(args, 0, this.args, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (args.length == 0) {
            return;
        } else {
            if (args[0].equals("del") || args[0].equals("delete")) {
                GameLogManager.getInstance().removeLog(composite(Arrays.copyOfRange(args, 1, args.length)));
            } else if (args[0].equals("query")) {
                List<String> results = GameLogManager.getInstance().query(
                        composite(Arrays.copyOfRange(args, 1, args.length)));
                for (String line : results) {
                    sendMsg(line);
                }
            } else {
                String[] parts = composite(args).split("\\|");
                GameLogManager.getInstance().addLog(parts[0], parts[1], caller);
            }
        }
    }
}

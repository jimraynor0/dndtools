package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.toj.common.FileIoUtils;
import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.file.GameStore;

@IrcCommand(command = "loaddeck", args = { CommandSegment.LIST })
public class LoadDeckCommand extends UndoableDracaGameCommand {

    private String fileName;

    public LoadDeckCommand(Object[] args) {
        fileName = composite(args);
    }

    @Override
    public void doProcess() throws ToolkitCommandException, ToolkitWarningException {
        if (!isFromDm()) {
            sendMsg("只有DM可以重置牌库。");
            return;
        }
        File file = GameStore.loadResourceFile(getGame().getName(), fileName);
        if (!file.isFile()) {
            sendMsg("文件" + file.getAbsolutePath() + "不存在。");
        }
        try {
            BufferedReader reader = FileIoUtils.getReader(file);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] params = line.split("\t");
                if (params.length < 2 || params.length > 3) {
                    sendMsg("[" + line + "]不符合格式，跳过。");
                    continue;
                }
                int amount;
                try {
                    amount = Integer.parseInt(params[0]);
                } catch (NumberFormatException e) {
                    sendMsg("[" + line + "]不符合格式，跳过。");
                    continue;
                }
                getGame().addCards(params[1], params.length == 3 ? params[2] : null, amount);
            }
            reader.close();
        } catch (IOException e) {
            throw new ToolkitWarningException("读取文件出错，请重试。");
        }
    }
}

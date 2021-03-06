package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "startgame", args = { CommandSegment.NULLABLE_STRING, CommandSegment.STRING },
        summary = ".startgame 游戏名 <规则> - 使用.help startgame查看全部说明。", desc = "这条命令包括了新建和读取游戏两个功能。\n"
                + "toolbox会首先尝试读取名为[游戏名]的存档。若该存档不存在，则新建一个游戏存档。\n"
                + "新建存档时可以用第二个参数指定该游戏使用的规则，目前支持的规则包括dnd3r，d6smw，draca。如果新建存档时没有指定使用的规则则默认使用dnd3r。\n"
                + "读取存档时不需指定规则名，即使指定也会被忽略。")
public class CreateOrLoadCommand extends GameCommand {

    private Logger log = Logger.getLogger(this.getClass());
    private String name;
    private String ruleSet;

    public CreateOrLoadCommand(Object[] args) {
        if (args[0] == null) {
            name = (String) args[1];
        } else {
            name = (String) args[0];
            ruleSet = (String) args[1];
        }
    }

    @Override
    public List<OutgoingMsg> execute() {
        Game game = null;
        try {
            game = GameStore.loadGame(name);
        } catch (IOException e) {
            log.error("Error while loading: ", e);
        }
        if (game == null) {
            game = GameStore.createGame(name, ruleSet);
            context.setGame(game);
            sendMsg("Game created.");
            log.debug("Game created: " + name);
            try {
                GameStore.save(getGame());
            } catch (IOException e) {
                log.error("saving game failed", e);
            } catch (JAXBException e) {
                log.error("saving game failed", e);
            }

        } else {
            context.setGame(game);
            sendTopic(game.generateTopic());
            refreshTopic();
            sendMsg("Game loaded.");
            log.debug("Game loaded: " + name);
        }
        IrcCommandFactory.loadGameCommands(game);
        game.setChan(chan);
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}

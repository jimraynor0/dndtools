package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandParser;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class ShowTopicCommand extends GameCommand {
    private static final String SHOW_TOPIC = "showtopic";

    private static IrcCommandParser parser = new IrcCommandParser() {
		@Override
		public boolean canParse(String[] args) {
			return args[0].equalsIgnoreCase(SHOW_TOPIC);
		}

		@Override
		public GameCommand parse(String[] args) {
            return new ShowTopicCommand();
		}
	};

	static {
		IrcCommandFactory.register(parser);
	}


    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        this.sendMsg(getGame().generateTopic());
        return this.msgs;
    }
}

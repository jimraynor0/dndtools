package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandParser;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RefreshTopicCommand extends UndoableTopicCommand {
    private static final String REFRESH = "refresh";

    private static IrcCommandParser parser = new IrcCommandParser() {
		@Override
		public boolean canParse(String[] args) {
			return args[0].equalsIgnoreCase(REFRESH);
		}

		@Override
		public GameCommand parse(String[] args) {
            return new RefreshTopicCommand();
		}
	};

	static {
		IrcCommandFactory.register(parser);
	}

    protected boolean updateTopic = true;

    @Override
    public boolean topicRefreshNeeded() {
        return true;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        refreshTopic();
    }
}

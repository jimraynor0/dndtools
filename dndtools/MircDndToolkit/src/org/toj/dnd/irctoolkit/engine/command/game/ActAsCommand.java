package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.Arrays;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandParser;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class ActAsCommand extends UndoableTopicCommand {
    private static final String ACT_AS = "actas";

    private static IrcCommandParser parser = new IrcCommandParser() {
		@Override
		public boolean canParse(String[] args) {
			return args[0].equalsIgnoreCase(ACT_AS);
		}

		@Override
		public GameCommand parse(String[] args) {
            return new ActAsCommand(args[1]);
		}
	};

	static {
		IrcCommandFactory.register(parser);
	}

    private String actAs;

    public ActAsCommand(String actAs) {
        this.actAs = actAs;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (actAs.equals("DM")) {
            getGame().setDm(caller);
        } else {
            getGame().addAlias(caller, actAs);
        }
    }
}

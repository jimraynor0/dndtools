package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandFactory;
import org.toj.dnd.irctoolkit.engine.command.IrcCommandParser;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.rules.RuleBook;

public class RuleQueryCommand extends GameCommand {
    private static final String RULE = "rule";

    private static IrcCommandParser parser = new IrcCommandParser() {
		@Override
		public boolean canParse(String[] args) {
			return args[0].equalsIgnoreCase(RULE);
		}

		@Override
		public GameCommand parse(String[] args) {
            return new RuleQueryCommand(Arrays.copyOfRange(args, 1, args.length));
		}
	};

	static {
		IrcCommandFactory.register(parser);
	}

	private String[] elements;

    public RuleQueryCommand(String[] elements) {
        this.elements = elements;
    }

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        StringBuilder rule = new StringBuilder();
        for (String seg : elements) {
            if (seg != elements[0]) {
                rule.append(" ");
            }
            rule.append(seg);
        }

        List<String> result = RuleBook.getRuleBook().query(rule.toString());
        if (result != null && !result.isEmpty()) {
            for (String line : result) {
                this.sendMsg(line.trim());
            }
        } else {
            this.sendMsg("cannot find rule: [" + rule + "]");
        }
        return this.msgs;
    }

    public boolean requireGameContext() {
        return false;
    }
}

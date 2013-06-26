package org.toj.dnd.irctoolkit.engine.command;

public interface IrcCommandParser {
	boolean canParse(String[] args);
	Command parse(String[] args);
}

package org.toj.dnd.irctoolkit.engine.command.map;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;
import org.toj.dnd.irctoolkit.map.MapModel;

@IrcCommand(command = "legend", args = { CommandSegment.NULLABLE_LIST })
public class ListModelCommand extends GameCommand {

    private String[] models;

    public ListModelCommand(Object[] args) {
        models = new String[args.length];
        System.arraycopy(args, 0, models, 0, models.length);
    }

    @Override
    public List<OutgoingMsg> execute() {
        for (MapModel model : context.getModelList()) {
            if ("ALL".equals(models[0]) || contains(models, model.getCh()) || contains(models, model.getDesc())) {
                this.msgs.add(new OutgoingMsg(chan, caller, model.toString(),
                        OutgoingMsg.WRITE_TO_MSG, null, -1));
            }
        }
        return this.msgs;
    }

    private boolean contains(String[] array, String ch) {
        for (String e : array) {
            if (e.equals(ch)) {
                return true;
            }
        }
        return false;
    }
}

package org.toj.dnd.irctoolkit.game.dnd3r.battle;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StateXmlAdaptor extends XmlAdapter<String, State> {

    @Override
    public State unmarshal(String v) throws Exception {
        return State.parseState(v);
    }

    @Override
    public String marshal(State v) throws Exception {
        return v.toStatString();
    }
}

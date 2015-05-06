@XmlJavaTypeAdapters({ @XmlJavaTypeAdapter(value = StateXmlAdaptor.class, type = State.class), })
package org.toj.dnd.irctoolkit.game.dnd3r.battle;

import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.toj.dnd.irctoolkit.game.dnd3r.battle.State;
import org.toj.dnd.irctoolkit.game.dnd3r.battle.StateXmlAdaptor;


package org.toj.dnd.irctoolkit.game.draca;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.toj.dnd.irctoolkit.game.Game;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DracaGame extends Game {
    private Library lib;
    private Map<String, Zone> zones;
    private Map<String, PC> pcs;

    public DracaGame() {
        lib = new Library();
        zones = new HashMap<>();
        pcs = new HashMap<>();
    }

    public DracaGame(String name) {
        this();
        setName(name);
    }

    public void addCards(String name, String text, int amount) {
        lib.add(new Card(name, text));
        for (int i = 0; i < amount; i++) {
            getDeck().add(name);
        }
        shuffle();
    }

    public void shuffle() {
        getDeck().shuffle();
    }

    public void move(String card, String fromZone, String toZone) {
        Zone from = getZone(fromZone);
        Zone to = getZone(toZone);
        if (from == null) {
            throw new RuntimeException("Zone " + fromZone + " does not exist.");
        }
        if (to == null) {
            throw new RuntimeException("Zone " + toZone + " does not exist.");
        }
        move(card, from, to);
    }

    private void move(String card, Zone from, Zone to) {
        from.remove(card);
        to.add(card);
    }

    public String draw(String pc, int amount) {
        if (getDeck().size() < amount) {
            return "没法抓了，牌库只剩下" + getDeck().size() + "张牌了。";
        } else {
            for (int i = 0; i < amount; i++) {
                move(getDeck().get(0), getDeck(), pcs.get(pc)
                        .getZone(Zone.HAND));
            }
        }
        return pc + "抓了" + amount + "张牌，牌库还剩下" + getDeck().size() + "张牌。";
    }

    public Zone getDeck() {
        return getZone(Zone.DECK);
    }

    public Zone getDiscard() {
        return getZone(Zone.DISCARD);
    }

    public Zone getPcHand(String pc) {
        return pcs.get(pc).getZone(Zone.HAND);
    }

    public Zone getPcDisplay(String pc) {
        return pcs.get(pc).getZone(Zone.DISPLAY);
    }

    private Zone getZone(String zone) {
        if (!zones.containsKey(zone)) {
            zones.put(zone, new Zone());
        }
        return zones.get(zone);
    }

    public void addPc(String ch) {
        this.pcs.put(ch, new PC(ch));
    }

    @Override
    public String getRuleSet() {
        return "draca";
    }

    @Override
    public String generateTopic() {
        return "";
    }

    @Override
    public String getGameCommandPackage() {
        return "org.toj.dnd.irctoolkit.engine.command.game.draca";
    }
}

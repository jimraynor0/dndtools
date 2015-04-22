package org.toj.dnd.irctoolkit.game.draca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.toj.dnd.irctoolkit.engine.ToolkitWarningException;
import org.toj.dnd.irctoolkit.game.Game;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DracaGame extends Game {
    public static final String CARD_DOESNOT_EXIST_IN_ZONE = "card doesnot exist in zone.";
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

    public void move(String card, String fromZone, String toZone) throws ToolkitWarningException {
        Zone from = getZone(fromZone);
        Zone to = getZone(toZone);
        move(card, from, to);
    }

    private void move(String card, Zone from, Zone to) throws ToolkitWarningException {
        if (!from.contains(card)) {
            throw new ToolkitWarningException(CARD_DOESNOT_EXIST_IN_ZONE);
        }
        from.remove(card);
        to.add(card);
    }

    public List<String> draw(String pc, int amount)
            throws ToolkitWarningException {
        if (getDeck().size() < amount) {
            throw new ToolkitWarningException("没法抓了，牌库只剩下" + getDeck().size()
                    + "张牌了。");
        } else {
            List<String> drawn = new ArrayList<>(amount);
            for (int i = 0; i < amount; i++) {
                String card = getDeck().get(0);
                move(card, getDeck(), pcs.get(pc).getZone(Zone.HAND));
                drawn.add(card);
            }
            return drawn;
        }
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

    public Zone getZone(String zone) {
        if (isPcZone(zone)) {
            String[] params = zone.split("@");
            return pcs.get(params[1]).getZone(params[0]);
        }
        if (!zones.containsKey(zone)) {
            zones.put(zone, new Zone());
        }
        return zones.get(zone);
    }

    private boolean isPcZone(String zone) {
        if (!zone.contains("@")) {
            return false;
        }
        String[] params = zone.split("@");
        return params.length == 2 && pcs.keySet().contains(params[1]);
    }

    public void addPc(String ch) {
        this.pcs.put(ch, new PC(ch));
    }

    public void removePc(String pc) {
        this.pcs.remove(pc);
    }

    public void renamePc(String oldName, String newName) throws ToolkitWarningException {
        if (!pcs.containsKey(oldName)) {
            throw new ToolkitWarningException("PC[" + oldName + "]不存在");
        }
        if (pcs.containsKey(newName)) {
            throw new ToolkitWarningException("已经存在一位名为[" + newName + "]的PC");
        }
        PC pc = pcs.remove(oldName);
        pc.setName(newName);
        pcs.put(newName, pc);
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

    public Map<String, PC> getPcs() {
        return pcs;
    }

    public void reset() {
        Zone zone = new Zone();
        for (Zone z : zones.values()) {
            zone.addAll(z.getCards());
            z.clear();
        }
        for (PC pc : pcs.values()) {
            for (Zone z : pc.getPcZones().values()) {
                zone.addAll(z.getCards());
                z.clear();
            }
        }
        zones.put(Zone.DECK, zone);
        shuffle();
    }
}

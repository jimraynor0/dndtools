package org.toj.dnd.irctoolkit.map;

import java.io.Serializable;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.IdUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class MapModel implements Serializable {
    private static final long serialVersionUID = -4894500322186180868L;

    public static final String MODE_DRAW = "draw";
    public static final String MODE_ERASE = "erase";

    private static List<MapModel> currentSelection;
    private static String selectionMode = MapModel.MODE_DRAW;

    public static MapModel getFirstSelection() {
        if (currentSelection == null || currentSelection.isEmpty()) {
            return null;
        } else {
            return currentSelection.get(0);
        }
    }

    public static List<MapModel> getCurrentSelection() {
        return currentSelection;
    }

    public static void setCurrentSelection(List<MapModel> currentSelection) {
        MapModel.currentSelection = currentSelection;
    }

    public static boolean hasSelection() {
        return getCurrentSelection() != null;
    }

    public static String getSelectionMode() {
        return selectionMode;
    }

    public static void setSelectionMode(String selectionMode) {
        MapModel.selectionMode = selectionMode;
    }

    private Color foreground = Color.BLACK;
    private Color background = null;
    private String id;
    private String ch = "";
    private String desc = "";
    private boolean blocksLineOfSight = false;
    private boolean blocksLineOfEffect = false;

    public MapModel() {
        super();
        this.id = IdUtil.generateId();
    }

    public MapModel(Element e) {
        super();
        String idStr = e.elementText("id");
        if (idStr != null) {
            this.id = idStr;
        } else {
            this.id = IdUtil.generateId();
        }
        this.foreground = Color.forName(e.elementText("foreground"));
        this.background = Color.forName(e.elementText("background"));
        this.ch = e.elementText("ch");
        this.desc = e.elementText("desc");
        this.blocksLineOfSight = Boolean.parseBoolean(e
                .elementText("blocksLineOfSight"));
        this.blocksLineOfEffect = Boolean.parseBoolean(e
                .elementText("blocksLineOfEffect"));
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public boolean isBlocksLineOfSight() {
        return blocksLineOfSight;
    }

    public void setBlocksLineOfSight(boolean blocksLineOfSight) {
        this.blocksLineOfSight = blocksLineOfSight;
    }

    public boolean isBlocksLineOfEffect() {
        return blocksLineOfEffect;
    }

    public void setBlocksLineOfEffect(boolean blocksLineOfEffect) {
        this.blocksLineOfEffect = blocksLineOfEffect;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "MapModel [id=" + id + ", foreground=" + foreground
                + ", background=" + background + ", ch=" + ch + ", desc="
                + desc + ", blocksLineOfSight=" + blocksLineOfSight
                + ", blocksLineOfEffect=" + blocksLineOfEffect + "]";
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("mapModel");
        if (id != null) {
            e.add(XmlUtil.textElement("id", id));
        }
        if (foreground != null) {
            e.add(XmlUtil.textElement("foreground", foreground.getName()));
        }
        if (background != null) {
            e.add(XmlUtil.textElement("background", background.getName()));
        }
        if (ch != null) {
            e.add(XmlUtil.textElement("ch", ch));
        }
        e.add(XmlUtil.textElement("desc", desc));
        e.add(XmlUtil.textElement("blocksLineOfSight", blocksLineOfSight));
        e.add(XmlUtil.textElement("blocksLineOfEffect", blocksLineOfEffect));

        return e;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((background == null) ? 0 : background.hashCode());
        result = prime * result + (blocksLineOfEffect ? 1231 : 1237);
        result = prime * result + (blocksLineOfSight ? 1231 : 1237);
        result = prime * result + ((ch == null) ? 0 : ch.hashCode());
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result
                + ((foreground == null) ? 0 : foreground.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MapModel other = (MapModel) obj;
        if (background != other.background)
            return false;
        if (blocksLineOfEffect != other.blocksLineOfEffect)
            return false;
        if (blocksLineOfSight != other.blocksLineOfSight)
            return false;
        if (ch == null) {
            if (other.ch != null)
                return false;
        } else if (!ch.equals(other.ch))
            return false;
        if (desc == null) {
            if (other.desc != null)
                return false;
        } else if (!desc.equals(other.desc))
            return false;
        if (foreground != other.foreground)
            return false;
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

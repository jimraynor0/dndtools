package org.toj.dnd.irctoolkit.map;

import java.io.Serializable;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class MapObject implements Serializable {

    private static final long serialVersionUID = 249903036350503274L;

    private int posX;
    private int posY;
    private MapModel model;

    public MapObject(int posX, int posY, MapModel model) {
        super();
        this.posX = posX;
        this.posY = posY;
        this.model = model;
    }

    public MapObject(Element e) {
        super();
        this.posX = Integer.parseInt(e.elementText("posX"));
        this.posY = Integer.parseInt(e.elementText("posY"));
        this.model = new MapModel(e.element("mapModel"));
    }

    public MapObject(Element e, MapModelList modelList) {
        super();
        this.posX = Integer.parseInt(e.elementText("posX"));
        this.posY = Integer.parseInt(e.elementText("posY"));
        String modelId = e.elementText("modelId");
        this.model = modelList.findModelById(modelId);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public MapModel getModel() {
        return model;
    }

    public void setModel(MapModel model) {
        this.model = model;
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("mapObject");
        e.add(XmlUtil.textElement("posX", posX));
        e.add(XmlUtil.textElement("posY", posY));
        e.add(XmlUtil.textElement("modelId", model.getId()));
        return e;
    }
}

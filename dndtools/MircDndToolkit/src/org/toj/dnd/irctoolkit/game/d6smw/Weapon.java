package org.toj.dnd.irctoolkit.game.d6smw;

import org.dom4j.Element;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public class Weapon extends Equipment {
    private String ammoType;
    private Ammo ammo;

    public Weapon(Element e) {
        super(e);
        if (e.element("ammoType") != null) {
            ammoType = e.elementTextTrim("ammoType");
        }
    }

    public Element toXmlElement() {
        Element e = super.toXmlElement();
        e.add(XmlUtil.textElement("ammoType", ammoType));

        return e;
    }

    public String activate(TimePoint firingOn) {
        return fire(firingOn);
    }

    public String fire(TimePoint firingOn) {
        if (ammo != null) {
            if (ammo.hasAmmo()) {
                return getName() + "的弹药已耗尽，无法射击。";
            }
            ammo.load();
        }
        return super.activate(firingOn);
    }

    public String getAmmoType() {
        return ammoType;
    }

    public void setAmmoType(String ammoType) {
        this.ammoType = ammoType;
    }

    public Ammo getAmmo() {
        return ammo;
    }

    public void setAmmo(Ammo ammo) {
        this.ammo = ammo;
    }
}

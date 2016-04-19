package org.toj.dnd.irctoolkit.game.d6smw;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Weapon extends Equipment {
    private String ammoType;
    private Ammo ammo;

    public String activate(TimePoint firingOn) {
        return fire(firingOn);
    }

    public String fire(TimePoint firingOn) {
        if (ammo != null) {
            if (!ammo.hasAmmo()) {
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

    protected String getType() {
        return "weapon";
    }
}

package org.toj.d6s.wuxia.movesetgen;

import java.util.List;

public class Move {
    private String name;
    private String target;
    private List<Protect> protects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Protect> getProtects() {
        return protects;
    }

    public void setProtects(List<Protect> protects) {
        this.protects = protects;
    }

    static public class Protect {
        private String part;
        private String type;
        private int bonus;

        public Protect() {}

        public Protect(String type, int bonus) {
            this(null, type, bonus);
        }

        public Protect(Protect p) {
            this(null, p.getType(), p.getBonus());
        }

        public Protect(String part, String type, int bonus) {
            this.part = part;
            this.type = type;
            this.bonus = bonus;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getBonus() {
            return bonus;
        }

        public void setBonus(int bonus) {
            this.bonus = bonus;
        }

        public String toString() {
            return new StringBuilder(part).append(" ").append(type).append(bonus >= 0 ? "+" : "").append(bonus).toString();
        }
    }
}

package org.toj.dnd.irctoolkit.mapgenerator.cavecrawler;

class VectorPoint {
    public int x;
    public int y;
    public Direction direction;

    public VectorPoint() {
        super();
    }

    public VectorPoint(int x, int y, Direction d) {
        this.x = x;
        this.y = y;
        this.direction = d;
    }
}
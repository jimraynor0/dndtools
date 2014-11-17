package org.toj.dnd.irctoolkit.mapgenerator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private int posX;
    private int posY;
    private int width;
    private int height;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Point> getWallCorners() {
        List<Point> points = new ArrayList<Point>(4);
        points.add(new Point(this.getPosX() - 1, this.getPosY() - 1));
        points.add(new Point(this.getPosX() + this.getWidth(),
                this.getPosY() - 1));
        points.add(new Point(this.getPosX() - 1, this.getPosY()
                + this.getHeight()));
        points.add(new Point(this.getPosX() + this.getWidth(), this.getPosY()
                + this.getHeight()));
        return points;
    }

    public List<Point> getCorners() {
        List<Point> points = new ArrayList<Point>(4);
        points.add(new Point(this.getPosX(), this.getPosY()));
        points.add(new Point(this.getPosX() + this.getWidth() - 1, this
                .getPosY()));
        points.add(new Point(this.getPosX(), this.getPosY() + this.getHeight()
                - 1));
        points.add(new Point(this.getPosX() + this.getWidth() - 1, this
                .getPosY() + this.getHeight() - 1));
        return points;
    }

    public boolean wallContains(Point p) {
        return p.x >= posX - 1 && p.x <= posX + width && p.y >= posY - 1
                && p.y <= posY + height;
    }

    public boolean contains(Point p) {
        return p.x >= posX && p.x <= posX + width - 1 && p.y >= posY
                && p.y <= posY + height - 1;
    }

    @Override
    public String toString() {
        return "Room [posX=" + posX + ", posY=" + posY + ", width=" + width
                + ", height=" + height + "]";
    }
}

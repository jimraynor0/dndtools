package org.toj.dnd.irctoolkit.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.engine.ReadonlyContext;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.observers.FilterListObserver;
import org.toj.dnd.irctoolkit.engine.observers.PcViewObserver;
import org.toj.dnd.irctoolkit.filter.AxisLabelFilter;
import org.toj.dnd.irctoolkit.filter.FillEmptySpaceFilter;
import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.token.Color;
import org.toj.dnd.irctoolkit.util.XmlUtil;

/**
 * 以左上角为[0, 0]点.
 */
public class MapGrid implements FilterListObserver, PcViewObserver {

    private transient Logger log = Logger.getLogger(this.getClass());

    private static final long serialVersionUID = -1055570870132909466L;

    public static final MapGridCell WHITESPACE = new MapGridCell(Color.BLACK,
            Color.WHITE, "　");

    private MapFilter axisLabelFilter = new AxisLabelFilter(this);

    private int width;
    private int height;

    private List<MapObject> objs;
    private transient MapGridCell[][] grid;

    private transient MapGridCell[][] filteredGrid;

    private MapModelList modelList;
    private List<MapFilter> filterList;

    private MapGrid(MapGridCell[][] grid) {
        super();
        this.width = grid.length;
        this.height = grid[0].length;
        this.grid = grid;
    }

    public MapGrid(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        grid = new MapGridCell[width][height];
        objs = new LinkedList<MapObject>();
        modelList = new MapModelList();
        filterList = new LinkedList<MapFilter>();
        // filterList.add(new AxisLabelFilter());
        registerAsObserver();
    }

    @SuppressWarnings("unchecked")
    public MapGrid(Element e) {
        this(Integer.parseInt(e.elementText("width")), Integer.parseInt(e
                .elementText("height")));

        for (Element o : (List<Element>) e.element("modelList").elements()) {
            modelList.add(new MapModel(o));
        }
        for (Element o : (List<Element>) e.element("mapObjects").elements()) {
            if (o.element("modelId") != null) {
                objs.add(new MapObject(o, modelList));
            } else {
                objs.add(new MapObject(o));
            }
        }
        List<MapObject> toBeRemoved = new LinkedList<MapObject>();
        for (MapObject o : objs) {
            int i = modelList.indexOf(o.getModel());
            if (i != -1) {
                o.setModel(modelList.get(i));
            } else {
                toBeRemoved.add(o);
            }
        }
        objs.removeAll(toBeRemoved);

        filterList.clear();
        for (Element o : (List<Element>) e.element("filterList").elements()) {
            MapFilter filter = MapFilter.MapFilterFactory.createFilter(o);
            if (filter != null) {
                filterList.add(filter);
            }
        }
        forceRefresh();
    }

    public void registerAsObserver() {
        ToolkitEngine.getEngine().addFilterListObserver(this);
        ToolkitEngine.getEngine().addPcViewObserver(this);
    }

    public void drawObject(MapObject item) {
        if (modelList != null) {
            int i = getPriority(item);
            override(item, item.getPosX(), item.getPosY(), i);
        } else {
            log.warn("WARN: model list is null!");
        }
        objs.add(item);

        calculateObjsOrder();
        updateGrid();
    }

    public void drawObjectsOntoGrid(int[] posXs, int[] posYs, MapModel model) {
        for (int x : posXs) {
            for (int y : posYs) {
                MapObject o = new MapObject(x, y, model);
                if (modelList != null) {
                    int i = getPriority(o);
                    override(o, o.getPosX(), o.getPosY(), i);
                } else {
                    log.warn("WARN: model list is null!");
                }
                objs.add(o);
            }
        }

        calculateObjsOrder();
        updateGrid();
    }

    public void eraseObjects(List<Integer> posXs, List<Integer> posYs,
            List<MapModel> models) {
        List<MapObject> iterator = new ArrayList<MapObject>(objs.size());
        iterator.addAll(objs);
        for (MapObject o : iterator) {
            if (posXs.contains(o.getPosX()) && posYs.contains(o.getPosY())
                    && (models == null || models.contains(o.getModel()))) {
                objs.remove(o);
            }
        }
        updateGrid();
    }

    public void eraseObjectsForModel(MapModel model) {
        List<MapObject> iterator = new ArrayList<MapObject>(objs.size());
        iterator.addAll(objs);
        for (MapObject o : iterator) {
            if (o.getModel().equals(model)) {
                objs.remove(o);
            }
        }
        updateGrid();
    }

    private int getPriority(MapObject item) {
        int i = modelList.indexOf(item.getModel());
        if (i == -1) {
            i = modelList.size();
            log.warn("WARN: model not found for obj: " + item);
            log.warn("WARN: priority set to: " + i);
        }
        return i;
    }

    private void override(MapObject self, int posX, int posY, int i) {
        for (MapObject o : this.objs) {
            if (o != self && o.getPosX() == posX && o.getPosY() == posY
                    && getPriority(o) == i) {
                objs.remove(o);
                return;
            }
        }
    }

    public void forceRefresh() {
        calculateObjsOrder();
        updateGrid();
    }

    private void calculateObjsOrder() {
        Collections.sort(objs, new Comparator<MapObject>() {

            @Override
            public int compare(MapObject o1, MapObject o2) {
                if (modelList == null) {
                    return 0;
                }
                int i1 = modelList.indexOf(o1.getModel());
                int i2 = modelList.indexOf(o2.getModel());
                if (i1 == -1 && i2 == -1) {
                    return 0;
                } else if (i1 == -1) {
                    return 1;
                } else if (i2 == -1) {
                    return -1;
                }
                return i1 - i2;
            }
        });
    }

    private void updateGrid() {
        grid = new MapGridCell[width][height];

        for (MapObject o : objs) {
            if (grid[o.getPosX()][o.getPosY()] == null) {
                grid[o.getPosX()][o.getPosY()] = new MapGridCell(o);
            } else {
                if (o.getModel().getBackground() != null) {
                    if (grid[o.getPosX()][o.getPosY()].getBackground() == null) {
                        grid[o.getPosX()][o.getPosY()].setBackground(o
                                .getModel().getBackground());

                        grid[o.getPosX()][o.getPosY()]
                                .setBlockLineOfSight(grid[o.getPosX()][o
                                        .getPosY()].isBlockLineOfSight()
                                        || o.getModel().isBlocksLineOfSight());
                        grid[o.getPosX()][o.getPosY()]
                                .setBlockLineOfEffect(grid[o.getPosX()][o
                                        .getPosY()].isBlockLineOfEffect()
                                        || o.getModel().isBlocksLineOfEffect());
                    }
                } else {
                    if (grid[o.getPosX()][o.getPosY()].getForeground() == null) {
                        grid[o.getPosX()][o.getPosY()].setForeground(o
                                .getModel().getForeground());
                        grid[o.getPosX()][o.getPosY()].setCh(o.getModel()
                                .getCh());
                        grid[o.getPosX()][o.getPosY()].setObjRef(o);

                        grid[o.getPosX()][o.getPosY()]
                                .setBlockLineOfSight(grid[o.getPosX()][o
                                        .getPosY()].isBlockLineOfSight()
                                        || o.getModel().isBlocksLineOfSight());
                        grid[o.getPosX()][o.getPosY()]
                                .setBlockLineOfEffect(grid[o.getPosX()][o
                                        .getPosY()].isBlockLineOfEffect()
                                        || o.getModel().isBlocksLineOfEffect());

                        // grid[o.getPosX()][o.getPosY()].setBlockLineOfSight(o
                        // .getModel().isBlocksLineOfSight());
                        // grid[o.getPosX()][o.getPosY()].setBlockLineOfEffect(o
                        // .getModel().isBlocksLineOfEffect());
                    }
                }

            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // TODO make private
    public MapGridCell[][] getGrid() {
        if (grid == null) {
            updateGrid();
        }
        return grid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MapGrid [width=").append(width)
                .append(", height=").append(height).append(", grid=\r\n");

        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                sb.append(grid[j][i]);
            }
            sb.append("\r\n");
        }

        sb.append("]");
        return sb.toString();
    }

    public MapModelList getModelList() {
        return modelList;
    }

    public MapGridCell getCell(int x, int y) {
        return getGrid()[x][y];
    }

    public void moveObject(MapObject obj, Point dest) {
        obj.setPosX(dest.getX());
        obj.setPosY(dest.getY());
        if (!this.objs.contains(obj)) {
            this.drawObject(obj);
        } else {
            this.override(obj, dest.getX(), dest.getY(), getPriority(obj));
            this.forceRefresh();
        }
    }

    public MapObject findObjByIconOrDesc(String desc) {
        for (MapObject o : this.objs) {
            if (o.getModel().getCh().equalsIgnoreCase(desc)) {
                return o;
            }
        }
        for (MapObject o : this.objs) {
            if (o.getModel().getDesc().toLowerCase()
                    .startsWith(desc.toLowerCase())) {
                return o;
            }
        }
        return null;
    }

    public MapGrid getFilteredMap() {
        if (filteredGrid == null) {
            refreshFilteredMap();
        }
        return new MapGrid(filteredGrid);
    }

    public MapGrid getUnfilteredMap() {
        MapGridCell[][] filteredGrid = new FillEmptySpaceFilter().applyFilter(this.getGrid());
        return new MapGrid(filteredGrid);
    }
    
    private void refreshFilteredMap() {
        filteredGrid = new FillEmptySpaceFilter().applyFilter(this.getGrid());
        for (MapFilter filter : filterList) {
            filteredGrid = filter.applyFilter(filteredGrid);
        }
        filteredGrid = axisLabelFilter.applyFilter(filteredGrid);
    }

    public MapGridCell[][] getUnAxisfiedFilteredMap() {
        MapGridCell[][] filteredGrid = new FillEmptySpaceFilter()
                .applyFilter(this.getGrid());
        for (MapFilter filter : filterList) {
            filteredGrid = filter.applyFilter(filteredGrid);
        }
        return filteredGrid;
    }

    public List<MapFilter> getFilterList() {
        return filterList;
    }

    @Override
    public void update(ReadonlyContext context) {
        refreshFilteredMap();
    }

    public Element toXmlElement() {
        Element e = DocumentHelper.createElement("mapGrid");
        e.add(XmlUtil.textElement("width", width));
        e.add(XmlUtil.textElement("height", height));

        e.addElement("mapObjects");
        for (MapObject o : objs) {
            e.element("mapObjects").add(o.toXmlElement());
        }

        e.addElement("modelList");
        for (MapModel o : modelList) {
            e.element("modelList").add(o.toXmlElement());
        }

        e.addElement("filterList");
        for (MapFilter o : filterList) {
            e.element("filterList").add(
                    MapFilter.MapFilterFactory.toXmlElement(o));
        }

        return e;
    }

    public MapObject[] findObjAt(int xMin, int xMax, int yMin, int yMax) {
        if (MapModel.hasSelection()) {
            List<MapObject> results = new ArrayList<MapObject>();
            for (MapObject o : objs) {
                if (o.getPosX() <= xMax
                        && o.getPosX() >= xMin
                        && o.getPosY() <= yMax
                        && o.getPosY() >= yMin
                        && MapModel.getCurrentSelection()
                                .contains(o.getModel())) {
                    results.add(o);
                }
            }
            return results.toArray(new MapObject[results.size()]);
        } else {
            Map<String, MapObject> results = new HashMap<String, MapObject>();
            for (MapObject o : objs) {
                if (o.getPosX() <= xMax
                        && o.getPosX() >= xMin
                        && o.getPosY() <= yMax
                        && o.getPosY() >= yMin
                        && !results
                                .containsKey(o.getPosX() + "," + o.getPosY())) {
                    results.put(o.getPosX() + "," + o.getPosY(), o);
                }
            }
            return results.values().toArray(new MapObject[results.size()]);
        }
    }
}

package org.toj.dnd.irctoolkit.engine;

import java.util.List;

import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.map.MapModelList;

public interface ReadonlyContext {

    MapGrid getCurrentMap();

    MapModelList getModelList();

    List<MapFilter> getFilterList();
}

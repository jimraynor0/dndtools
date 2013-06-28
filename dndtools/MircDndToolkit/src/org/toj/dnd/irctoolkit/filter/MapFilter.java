package org.toj.dnd.irctoolkit.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.toj.dnd.irctoolkit.map.MapGridCell;
import org.toj.dnd.irctoolkit.map.Point;
import org.toj.dnd.irctoolkit.util.AxisUtil;
import org.toj.dnd.irctoolkit.util.XmlUtil;

public abstract class MapFilter {

    public static final String TYPE_AXIS_LABEL_FILTER = "×ø±êÂË¾µ";
    public static final String TYPE_INVISIBILITY_FILTER = "ÒþÐÎÂË¾µ";
    public static final String TYPE_LINE_OF_SIGHT_FILTER = "¿ÉÊÓ·¶Î§ÂË¾µ";
    public static final String TYPE_CROP_FILTER = "½ØÈ¡ÂË¾µ";

    private boolean active = true;

    public MapGridCell[][] applyFilter(MapGridCell[][] original) {
        if (active) {
            return doApplyFilter(original);
        } else {
            return original;
        }
    }

    public abstract MapGridCell[][] doApplyFilter(MapGridCell[][] original);

    public abstract String getType();

    public abstract String getParams();

    public static class MapFilterFactory {

        public static Element toXmlElement(MapFilter filter) {
            Element e = DocumentHelper.createElement("mapFilter");
            e.add(XmlUtil.textElement("type", filter.getType()));
            e.add(XmlUtil.textElement("params", filter.getParams()));
            return e;
        }

        public static MapFilter createFilter(Element e) {
            return createFilter(e.elementText("type"), e.elementText("params"));
        }

        public static MapFilter createFilter(String type, String paramStr) {
            // if (TYPE_AXIS_LABEL_FILTER.equals(type)) {
            // return new AxisLabelFilter();
            // }
            if (TYPE_INVISIBILITY_FILTER.equals(type)) {
                if (paramStr == null) {
                    paramStr = "";
                }
                return new InvisibilityFilter(
                        Arrays.asList(paramStr.split(",")));
            }
            if (TYPE_LINE_OF_SIGHT_FILTER.equals(type)) {
                String[] params = paramStr.split("\\,");
                List<ViewPoint> vps = new ArrayList<ViewPoint>(params.length);
                for (String p : params) {
                    ViewPoint vp = null;
                    if (AxisUtil.is2DAxis(p)) {
                        Point point = AxisUtil.parse2DAxis(p);
                        vp = new ViewPoint(point.getX(), point.getY());
                    } else if (p.matches("x.y.\\|\\d")) {
                        vp = new ViewPoint(
                                AxisUtil.toNumber(p.substring(0, 2)),
                                AxisUtil.toNumber(p.substring(2, 4)),
                                Integer.parseInt(p.substring(5)));
                    } else if (p.contains("|")) {
                        vp = new ViewPoint(p.split("\\|")[0],
                                Integer.parseInt(p.split("\\|")[1]));
                    } else {
                        vp = new ViewPoint(p.split("\\|")[0], 100);
                    }
                    vps.add(vp);
                }
                return new LineOfSightFilter(vps,
                        LineOfSightFilter.MASK_TYPE_LINE_OF_SIGHT);
            }
            if (TYPE_CROP_FILTER.equals(type)) {
                String[] params = paramStr.split(",");
                int[] bounds = new int[params.length];
                for (int i = 0; i < params.length; i++) {
                    try {
                        bounds[i] = Integer.parseInt(params[i]);
                    } catch (NumberFormatException e) {
                        bounds[i] = AxisUtil.toNumber(params[i]);
                    }
                }
                return new CropFilter(bounds[0], bounds[1], bounds[2],
                        bounds[3]);
            }
            return null;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

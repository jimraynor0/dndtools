package org.toj.dnd.irctoolkit.ui.map.filterpane.params;

import java.awt.Component;
import java.awt.Dimension;

import org.toj.dnd.irctoolkit.filter.MapFilter;

public abstract class FilterParamComponent {

    public static FilterParamComponent getFilterParamComponent(
            String filterType, String param) {
        if (filterType == null) {
            return TextLabelComponent.getInstance("请选择你要添加/修改的滤镜类型。");
        } else if (filterType == MapFilter.TYPE_AXIS_LABEL_FILTER) {
            return TextLabelComponent
                    .getInstance("你选择的滤镜不需要额外的参数。");
        } else if (filterType == MapFilter.TYPE_CROP_FILTER) {
            return new CropFilterParamComponent(param);
        } else if (filterType == MapFilter.TYPE_INVISIBILITY_FILTER) {
            return new InvFilterParamComponent(param);
        } else if (filterType == MapFilter.TYPE_LINE_OF_SIGHT_FILTER) {
            return new TextfieldParamComponent(param);
        }
        return null;
    }

    public abstract String retrieveParam();

    public abstract Dimension getSuggestedParentSize();

    public abstract Component getTitle();

    public abstract Component getComponent();
}

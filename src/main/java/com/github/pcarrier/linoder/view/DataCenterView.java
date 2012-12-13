package com.github.pcarrier.linoder.view;

import com.github.pcarrier.linoder.model.DataCenter;

public class DataCenterView {
    public String renderSimpleDataCenter(DataCenter dc) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Datacenter ");
        builder.append(dc.getId());
        builder.append(": ");
        builder.append(dc.getLocation());
        return builder.toString();
    }

}

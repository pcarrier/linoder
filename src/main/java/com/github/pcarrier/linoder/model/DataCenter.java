package com.github.pcarrier.linoder.model;

import lombok.Data;

@Data
public class DataCenter implements Comparable<DataCenter> {
    private final int id;
    private final String location;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getId());
        builder.append(": ");
        builder.append(location);
        return builder.toString();
    }

    @Override
    public int compareTo(DataCenter o) {
        return this.getId() - o.getId();
    }
}

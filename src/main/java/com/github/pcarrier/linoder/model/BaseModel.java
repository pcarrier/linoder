package com.github.pcarrier.linoder.model;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.util.Iterator;

public class BaseModel implements Comparable<BaseModel> {
    protected final JSONObject data;

    BaseModel(JSONObject data) throws JSONException {
        this.data = data;
    }

    public int getId() {
        throw new UnsupportedOperationException();
    }

    public String getLabel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getId());
        builder.append(": ");
        builder.append(this.getLabel());
        return builder.toString();
    }

    public String toDetailedString() {
        StringBuilder builder = new StringBuilder(this.toString());

        builder.append('\n');

        Iterator<String> keyIterator = data.sortedKeys();
        while (keyIterator.hasNext()) {
            final String key = keyIterator.next();
            builder.append("  ");
            builder.append(key);
            builder.append(": ");
            try {
                builder.append(data.get(key));
            } catch (JSONException e) {
                builder.append('?');
            }
            builder.append('\n');
        }

        return builder.toString();
    }

    @Override
    public int compareTo(BaseModel o) {
        return this.getId() - o.getId();
    }
}

package com.github.pcarrier.linoder.model;

import lombok.Getter;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class Plan extends BaseModel {
    @Getter
    private final int id;
    @Getter
    private final String label;
    @Getter
    private final int disk;
    @Getter
    private final int ram;
    @Getter
    private final int xfer;
    @Getter
    private final double price;
    @Getter
    private final SortedMap<Integer, Integer> availability = new TreeMap<Integer, Integer>();

    public Plan(JSONObject data) throws JSONException {
        super(data);

        this.label = data.getString("LABEL");
        this.id = data.getInt("PLANID");
        this.disk = data.getInt("DISK");
        this.ram = data.getInt("RAM");
        this.xfer = data.getInt("XFER");
        this.price = data.getDouble("PRICE");

        JSONObject avail = data.getJSONObject("AVAIL");
        Iterator<String> keysIterator = avail.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            int datacenterId = Integer.parseInt(key);
            availability.put(datacenterId, avail.getInt(key));
        }
    }
}

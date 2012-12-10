package com.github.pcarrier.linoder.model;

import lombok.Getter;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class Address extends BaseModel {
    @Getter
    private final int id;
    @Getter
    private final int linodeId;
    @Getter
    private final boolean publik;
    @Getter
    private final String ipAddress;
    @Getter
    private final String reverseName;

    public Address(JSONObject data) throws JSONException {
        super(data);

        this.id = data.getInt("IPADDRESSID");
        this.linodeId = data.getInt("LINODEID");
        this.ipAddress = data.getString("IPADDRESS");
        this.publik = data.getInt("ISPUBLIC") == 1;
        this.reverseName = data.getString("RDNS_NAME");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.getId());
        builder.append(": ");
        builder.append(this.getIpAddress());

        return builder.toString();
    }
}

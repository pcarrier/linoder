package com.github.pcarrier.linoder.model;

import lombok.Getter;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/*
TODO: support for BACKUPWEEKLYDAY, BACKUPWINDOW
TODO: support for STATUS
*/

public class Linode extends BaseModel {
    @Getter
    private final int id;
    @Getter
    private final String label;
    @Getter
    private final int dataCenterId;
    @Getter
    private final boolean watchDogged;
    @Getter
    private final int disk;
    @Getter
    private final int ram;
    @Getter
    private final int xfer;
    @Getter
    private final String displayGroup;
    @Getter
    private final boolean bandwidthInMonitored;
    @Getter
    private final int bandwidthInThreshold;
    @Getter
    private final boolean bandwidthOutMonitored;
    @Getter
    private final int bandwidthOutThreshold;
    @Getter
    private final boolean bandwidthQuotaMonitored;
    @Getter
    private final int bandwidthQuotaThreshold;
    @Getter
    private final boolean diskIOMonitored;
    @Getter
    private final int diskIOThreshold;
    @Getter
    private final boolean cpuMonitored;
    @Getter
    private final int cpuThreshold;
    @Getter
    private final boolean backupEnabled;

    public boolean hasDisplayGroup() {
        return (this.displayGroup != null && this.displayGroup.length() > 0);
    }


    public Linode(JSONObject data) throws JSONException {
        super(data);
        this.label = data.getString("LABEL");
        this.dataCenterId = data.getInt("DATACENTERID");
        this.id = data.getInt("LINODEID");
        this.watchDogged = data.getInt("WATCHDOG") == 1;
        this.disk = data.getInt("TOTALHD");
        this.ram = data.getInt("TOTALRAM");
        this.xfer = data.getInt("TOTALXFER");
        this.displayGroup = data.getString("LPM_DISPLAYGROUP");
        this.backupEnabled = data.getInt("BACKUPSENABLED") == 1;
        this.bandwidthInMonitored = data.getInt("ALERT_BWIN_ENABLED") == 1;
        this.bandwidthOutMonitored = data.getInt("ALERT_BWOUT_ENABLED") == 1;
        this.bandwidthQuotaMonitored = data.getInt("ALERT_BWQUOTA_ENABLED") == 1;
        this.cpuMonitored = data.getInt("ALERT_CPU_ENABLED") == 1;
        this.diskIOMonitored = data.getInt("ALERT_DISKIO_ENABLED") == 1;
        this.bandwidthInThreshold = data.getInt("ALERT_BWIN_THRESHOLD");
        this.bandwidthOutThreshold = data.getInt("ALERT_BWOUT_THRESHOLD");
        this.bandwidthQuotaThreshold = data.getInt("ALERT_BWQUOTA_THRESHOLD");
        this.cpuThreshold = data.getInt("ALERT_CPU_THRESHOLD");
        this.diskIOThreshold = data.getInt("ALERT_DISKIO_THRESHOLD");
    }
}

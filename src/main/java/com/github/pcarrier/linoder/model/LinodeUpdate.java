package com.github.pcarrier.linoder.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class LinodeUpdate {
    @Getter
    @Setter
    private String label = null;
    @Getter
    @Setter
    private Boolean watchDogged = null;
    @Getter
    @Setter
    private String displayGroup = null;
    @Getter
    @Setter
    private Boolean bandwidthInMonitored = null;
    @Getter
    @Setter
    private Integer bandwidthInThreshold = null;
    @Getter
    @Setter
    private Boolean bandwidthOutMonitored = null;
    @Getter
    @Setter
    private Integer bandwidthOutThreshold = null;
    @Getter
    @Setter
    private Boolean bandwidthQuotaMonitored = null;
    @Getter
    @Setter
    private Integer bandwidthQuotaThreshold = null;
    @Getter
    @Setter
    private Boolean diskIOMonitored = null;
    @Getter
    @Setter
    private Integer diskIOThreshold = null;
    @Getter
    @Setter
    private Boolean cpuMonitored = null;
    @Getter
    @Setter
    private Integer cpuThreshold = null;
    @Getter
    @Setter
    private Boolean backupEnabled = null;
    @Getter
    @Setter
    private Integer backupWindow = null;
    @Getter
    @Setter
    private Integer backupWeeklyDay = null;

    public Map<String, String> toParams() {
        final HashMap<String, String> res = new HashMap<String, String>();

        if (label != null)
            res.put("Label", label);
        if (displayGroup != null)
            res.put("lpm_displayGroup", displayGroup);
        if (cpuMonitored != null)
            res.put("Alert_cpu_enabled", cpuMonitored ? "1" : "0");
        if (diskIOMonitored != null)
            res.put("Alert_diskio_enabled", diskIOMonitored ? "1" : "0");
        if (bandwidthInMonitored != null)
            res.put("Alert_bwin_enabled", bandwidthInMonitored ? "1" : "0");
        if (bandwidthOutMonitored != null)
            res.put("Alert_bwout_enabled", bandwidthOutMonitored ? "1" : "0");
        if (bandwidthQuotaMonitored != null)
            res.put("Alert_bwquota_enabled", bandwidthQuotaMonitored ? "1" : "0");
        if (watchDogged != null)
            res.put("Alert_bwquota_enabled", watchDogged ? "1" : "0");
        if (cpuThreshold != null)
            res.put("Alert_cpu_threshold", Integer.toString(cpuThreshold));
        if (diskIOThreshold != null)
            res.put("Alert_diskio_threshold", Integer.toString(diskIOThreshold));
        if (bandwidthInThreshold != null)
            res.put("Alert_bwin_threshold", Integer.toString(bandwidthInThreshold));
        if (bandwidthOutThreshold != null)
            res.put("Alert_bwout_threshold", Integer.toString(bandwidthOutThreshold));
        if (bandwidthQuotaThreshold != null)
            res.put("Alert_bwquota_threshold", Integer.toString(bandwidthQuotaThreshold));
        if (backupWeeklyDay != null)
            res.put("backupWeeklyDay", Integer.toString(backupWeeklyDay));
        if (backupWindow != null)
            res.put("backupWindow", Integer.toString(backupWindow));

        return res;
    }
}

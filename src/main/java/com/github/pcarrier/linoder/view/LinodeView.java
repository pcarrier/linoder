package com.github.pcarrier.linoder.view;

import com.github.pcarrier.linoder.model.Address;
import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.model.Linode;

import java.util.Map;

public class LinodeView {
    public String renderSimpleLinode(Linode linode) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Linode ");
        builder.append(linode.getId());
        builder.append(": ");
        if (linode.hasDisplayGroup()) {
            builder.append(linode.getDisplayGroup());
            builder.append(" / ");
        }
        builder.append(linode.getLabel());
        return builder.toString();
    }

    public String renderCompleteLinode(Linode linode, Map<Integer, DataCenter> datacenters, Map<Integer, Address> addresses) {
        final StringBuilder builder = new StringBuilder();
        final AddressView addrView = new AddressView();

        builder.append("Linode ");
        builder.append(linode.getId());
        builder.append(": ");
        if (linode.hasDisplayGroup()) {
            builder.append(linode.getDisplayGroup());
            builder.append(" / ");
        }
        builder.append(linode.getLabel());
        builder.append("\n  in ");
        builder.append(datacenters.get(linode.getDataCenterId()).toString());
        builder.append("\n  ");
        builder.append(linode.getDisk());
        builder.append("GB disk, ");
        builder.append(linode.getRam());
        builder.append("MB RAM, ");
        builder.append(linode.getXfer());
        builder.append("GB XFER\n  ");
        if (linode.isBackupEnabled())
            builder.append("with ");
        else
            builder.append("NO ");
        builder.append("backups, ");
        if (linode.isWatchDogged())
            builder.append("with ");
        else
            builder.append("NO ");
        builder.append("watchdog\n");

        for (Address address : addresses.values()) {
            builder.append(addrView.renderAddress(address));
            builder.append("\n");
        }

        return builder.toString();
    }
}

package com.github.pcarrier.linoder.view;

import com.github.pcarrier.linoder.model.Address;

public class AddressView {
    public String renderAddress(Address address) {
        final StringBuilder builder = new StringBuilder();

        builder.append("IP ");
        builder.append(address.getId());
        builder.append(": ");
        builder.append(address.getIpAddress());
        builder.append(" (");
        if (address.isPublik())
            builder.append("public, ");
        else
            builder.append("private, ");
        builder.append(address.getReverseName());
        builder.append(")");

        return builder.toString();
    }
}

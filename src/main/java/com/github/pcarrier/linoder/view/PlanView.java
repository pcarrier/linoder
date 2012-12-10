package com.github.pcarrier.linoder.view;

import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.model.Plan;

import java.util.Map;

public class PlanView {
    public String renderSimplePlan(Plan plan) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Plan ");
        builder.append(plan.getId());
        builder.append(": ");
        builder.append(plan.getLabel());
        builder.append(" ($");
        builder.append(plan.getPrice());
        builder.append(", ");
        builder.append(plan.getRam());
        builder.append("MB RAM, ");
        builder.append(plan.getDisk());
        builder.append("GB disk, ");
        builder.append(plan.getXfer());
        builder.append("GB XFER)");

        return builder.toString();
    }

    public String renderCompletePlan(Plan plan, Map<Integer, DataCenter> datacenters) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Plan ");
        builder.append(plan.getId());
        builder.append(": ");
        builder.append(plan.getLabel());
        builder.append(" ($");
        builder.append(plan.getPrice());
        builder.append(", ");
        builder.append(plan.getRam());
        builder.append("MB RAM, ");
        builder.append(plan.getDisk());
        builder.append("GB disk, ");
        builder.append(plan.getXfer());
        builder.append("GB XFER)");

        return builder.toString();
    }
}

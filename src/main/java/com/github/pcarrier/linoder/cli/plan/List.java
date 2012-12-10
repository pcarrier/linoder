package com.github.pcarrier.linoder.cli.plan;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.model.Plan;
import com.github.pcarrier.linoder.view.PlanView;
import io.airlift.command.Command;

import java.util.Map;

@Command(name = "list", description = "List plans")
public class List extends BaseCommand {
    @Override
    public void execute() throws RemoteCallException {
        final PlanView view = new PlanView();

        Map<Integer, DataCenter> datacenters = null;
        if (verbose)
            datacenters = getApi().getAvailableDataCenters();

        for (Plan plan : getApi().getAvailablePlans().values()) {
            if (verbose)
                System.out.println(view.renderCompletePlan(plan, datacenters));
            else
                System.out.println(view.renderSimplePlan(plan));
        }
    }
}

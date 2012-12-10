package com.github.pcarrier.linoder.cli.linode;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.model.Linode;
import com.github.pcarrier.linoder.model.LinodeUpdate;
import com.github.pcarrier.linoder.model.Plan;
import com.github.pcarrier.linoder.view.LinodeView;
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Command(name = "clone", description = "Clone")
public class Clone extends BaseCommand {
    @Option(name = "-l", description = "label for the newly create VM (defaults to none)")
    public String label;

    @Option(name = "-b", description = "boot the newly create VM")
    public boolean boot;

    @Option(name = "-D", description = "data center to create in (defaults to the same DC)")
    public Integer dcId;

    @Option(name = "-p", description = "plan (defaults to first available plan)")
    public Integer planId;

    @Option(name = "-P", description = "payment plan (1, 12 or 24, defaults to 1)")
    public Integer paymentId;

    @Arguments(description = "ID or label of the VM to clone")
    public List<String> origIdOrLabel;

    @Override
    public void execute() throws RemoteCallException {
        final LinodeView view = new LinodeView();
        final Map<Integer, DataCenter> datacenters = getApi().getAvailableDataCenters();

        if (origIdOrLabel.size() != 1) {
            System.err.println("One argument is required");
            System.exit(1);
        }

        Linode orig = getApi().getLinode(origIdOrLabel.get(0));

        if (dcId == null)
            dcId = orig.getDataCenterId();

        if (paymentId == null)
            paymentId = 1;

        if (planId == null) {
            SortedMap<Integer, Plan> plans = getApi().getAvailablePlans();
            planId = plans.get(plans.firstKey()).getId();
        }

        int newLinodeId = getApi().cloneLinode(orig.getId(), dcId, planId, paymentId);

        if (label != null) {
            final LinodeUpdate update = new LinodeUpdate();
            update.setLabel(label);
            getApi().updateLinode(newLinodeId, update);
        }

        System.out.println(view.renderCompleteLinode(getApi().getLinode(newLinodeId), datacenters, getApi().getAddresses(newLinodeId)));

        if (boot)
            getApi().bootLinode(newLinodeId);
    }
}

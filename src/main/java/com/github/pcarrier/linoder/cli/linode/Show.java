package com.github.pcarrier.linoder.cli.linode;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.model.Linode;
import com.github.pcarrier.linoder.view.LinodeView;
import io.airlift.command.Arguments;
import io.airlift.command.Command;

import java.util.SortedMap;

@Command(name = "show", description = "Show linodes by label or ID")
public class Show extends BaseCommand {
    @Arguments
    public java.util.List<String> labelOrIDs;

    @Override
    public void execute() throws RemoteCallException {
        boolean failed = false;
        LinodeView view = new LinodeView();

        SortedMap<Integer, DataCenter> datacenters = null;
        if (verbose)
            datacenters = getApi().getAvailableDataCenters();

        for (String labelOrID : labelOrIDs) {
            Linode linode = getApi().getLinode(labelOrID);
            if (linode == null) {
                System.err.printf("Couldn't find the linode %s\n", labelOrID);
                failed = true;
            } else {
                if (verbose)
                    System.out.println(view.renderCompleteLinode(linode, datacenters, getApi().getAddresses(linode.getId())));
                else
                    System.out.println(view.renderSimpleLinode(linode));
            }
        }

        if (failed)
            System.exit(1);
    }
}

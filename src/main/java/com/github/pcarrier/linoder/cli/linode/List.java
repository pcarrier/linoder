package com.github.pcarrier.linoder.cli.linode;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.model.Linode;
import com.github.pcarrier.linoder.view.LinodeView;
import io.airlift.command.Command;

import java.util.Map;
import java.util.SortedMap;

@Command(name = "list", description = "List linodes")
public class List extends BaseCommand {
    @Override
    public void execute() throws RemoteCallException {
        SortedMap<Integer, Linode> linodes = getApi().getLinodes();

        Map<Integer, DataCenter> datacenters = null;
        if (verbose) {
            datacenters = getApi().getAvailableDataCenters();
        }

        LinodeView view = new LinodeView();

        for (Linode linode : linodes.values()) {
            if (verbose)
                System.out.println(view.renderCompleteLinode(linode, datacenters, getApi().getAddresses(linode.getId())));
            else
                System.out.println(view.renderSimpleLinode(linode));
        }
    }
}

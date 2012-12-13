package com.github.pcarrier.linoder.cli.datacenter;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.DataCenter;
import com.github.pcarrier.linoder.view.DataCenterView;
import io.airlift.command.Command;

@Command(name = "list", description = "List data centers")
public class List extends BaseCommand {
    @Override
    public void execute() throws RemoteCallException {
        final DataCenterView view = new DataCenterView();
        for (DataCenter dc : getApi().getAvailableDataCenters().values())
            System.out.println(view.renderSimpleDataCenter(dc));
    }
}

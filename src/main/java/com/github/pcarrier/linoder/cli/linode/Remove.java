package com.github.pcarrier.linoder.cli.linode;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.Linode;
import io.airlift.command.Arguments;
import io.airlift.command.Command;

@Command(name = "rm", description = "Remove linodes")
public class Remove extends BaseCommand {
    @Arguments(description = "IDs or labels of the VMs to clone")
    public java.util.List<String> idOrLabelList;

    @Override
    protected void execute() throws RemoteCallException {
        boolean failed = false;
        for (String idOrLabel : idOrLabelList) {
            try {
                Linode linode = getApi().getLinode(idOrLabel);
                getApi().deleteLinode(linode.getId(), true);
            } catch (Exception e) {
                e.printStackTrace();
                failed = true;
            }
        }
        if (failed)
            System.exit(1);
    }
}

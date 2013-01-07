package com.github.pcarrier.linoder.cli.linode;

import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.github.pcarrier.linoder.cli.BaseCommand;
import com.github.pcarrier.linoder.model.Linode;
import io.airlift.command.Arguments;
import io.airlift.command.Command;

import java.util.List;

@Command(name = "clone", description = "Boot a linode")
public class Boot extends BaseCommand {
    @Arguments(description = "ID or label of the VM to clone")
    public List<String> idOrLabel;

    @Override
    public void execute() throws RemoteCallException {
        if (idOrLabel.size() != 1) {
            System.err.println("One argument is required");
            System.exit(1);
        }

        Linode linode = getApi().getLinode(idOrLabel.get(0));

        getApi().bootLinode(linode.getId());
    }
}

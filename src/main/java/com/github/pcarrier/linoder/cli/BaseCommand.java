package com.github.pcarrier.linoder.cli;

import com.github.pcarrier.linoder.api.API;
import com.github.pcarrier.linoder.api.APIBuilder;
import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import lombok.Getter;

public abstract class BaseCommand implements Runnable {
    @Option(type = OptionType.GLOBAL, name = "-v", description = "Verbose mode (show detailed infos)")
    public boolean verbose;

    @Option(type = OptionType.GLOBAL, name = "-d", description = "Debug mode (show API requests and replies)")
    public boolean debug;

    @Option(type = OptionType.GLOBAL, name = "-k", description = "API key from https://manager.linode.com/profile/")
    public String api_key;

    @Getter
    private API api = null;

    @Override
    public final void run() {
        /* TODO: find the key by logging in if key not available */
        if (api_key == null) {
            System.err.println("Unknown API key!");
            System.exit(44);
        }

        api = new APIBuilder().withKey(api_key).withDebug(debug).build();

        try {
            execute();
        } catch (RemoteCallException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected void execute() throws RemoteCallException {
        throw new UnsupportedOperationException();
    }
}

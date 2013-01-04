package com.github.pcarrier.linoder.cli;

import com.github.pcarrier.linoder.api.API;
import com.github.pcarrier.linoder.api.APIBuilder;
import com.github.pcarrier.linoder.api.exceptions.RemoteCallException;
import com.google.common.io.Files;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class BaseCommand implements Runnable {

    @Option(type = OptionType.GLOBAL, name = "-v", description = "Verbose mode (show detailed infos)")
    public boolean verbose;

    @Option(type = OptionType.GLOBAL, name = "-d",
            description = "Debug mode (show API requests and replies)")
    public boolean debug;

    @Option(type = OptionType.GLOBAL, name = "-k",
            description = "API key from https://manager.linode.com/profile/")
    public String api_key;

    @Getter
    private API api = null;

    @Override
    public final void run() {
        if (api_key == null) {
            final File homeKeyFile = new File(new File(System.getProperty("user.home")), ".linodekey");

            try {
                api_key = Files.readFirstLine(homeKeyFile, Charset.defaultCharset());
            } catch (IOException e) {
                System.err.println("Unknown API key! Please put it in " + homeKeyFile.getPath() + " or use -k");
                System.exit(44);
            }
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

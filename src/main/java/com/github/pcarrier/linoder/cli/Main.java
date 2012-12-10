package com.github.pcarrier.linoder.cli;

import com.github.pcarrier.linoder.cli.datacenter.List;
import com.github.pcarrier.linoder.cli.linode.Clone;
import com.github.pcarrier.linoder.cli.linode.Remove;
import com.github.pcarrier.linoder.cli.linode.Show;
import io.airlift.command.Cli;
import io.airlift.command.Help;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Cli.CliBuilder<Runnable> builder = Cli.buildCli("linoder", Runnable.class)
                .withDescription("Control linode.com through its API")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class);

        builder.withGroup("linode")
                .withDescription("Operations on linodes")
                .withDefaultCommand(com.github.pcarrier.linoder.cli.linode.List.class)
                .withCommands(Clone.class, com.github.pcarrier.linoder.cli.linode.List.class, Show.class, Remove.class);

        builder.withGroup("datacenter")
                .withDescription("Operations on data centers")
                .withDefaultCommand(List.class)
                .withCommands(List.class);

        builder.withGroup("plan")
                .withDescription("Operations on plans")
                .withDefaultCommand(com.github.pcarrier.linoder.cli.plan.List.class)
                .withCommands(com.github.pcarrier.linoder.cli.plan.List.class);

        builder.build().parse(args).run();
    }
}

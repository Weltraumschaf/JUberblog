/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
package de.weltraumschaf.juberblog.cmd.install;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.BaseSubCommand;
import de.weltraumschaf.juberblog.opt.InstallOptions;
import java.io.File;
import java.io.IOException;

/**
 * Installation command.
 *
 * Creates a scaffold directory in specified location.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class InstallSubCommand extends BaseSubCommand<InstallOptions> {

    /**
     * Command line options.
     */
    private InstallOptions options;
    /**
     * Used to copy the scaffold.
     */
    private final Scaffold scaffold;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public InstallSubCommand(final IO io) {
        super(io);
        scaffold = new Scaffold(io);
    }

    @Override
    protected void run() throws ApplicationException {
        if (getOptions().isHelp()) {
            return;
        }

        final String location = options.getLocation().trim();
        final File target = validateLocation(location);
        io.println(String.format("Install scaffold to '%s'...", location));

        try {
            scaffold.setVerbose(getOptions().isVerbose());
            scaffold.copyFiles(target);
        } catch (IOException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Can't install scaffold!", ex);
        }
    }

    @Override
    public void setOptions(final InstallOptions opt) {
        Validate.notNull(opt, "Options must not be null!");
        options = opt;
    }

    @Override
    public InstallOptions getOptions() {
        return options;
    }

    /**
     * Injection point for a custom provider.
     *
     * @param srcJar must not be {@code null}
     */
    void setSrcJar(final Scaffold.SourceJarProvider srcJar) {
        scaffold.setSrcJar(srcJar);
    }

    /**
     * Validate instalation directory.
     *
     * Validates that given string is not empty and is an existing directory.
     *
     * @param location must not be {@code null} or empty
     * @return never {@code null}
     * @throws ApplicationException if target does not exist or is not a directory
     */
    private File validateLocation(final String location) throws ApplicationException {
        Validate.notNull(location, "Location must not be null!");

        if (location.isEmpty()) {
            throw new ApplicationException(
                    ExitCodeImpl.MISSING_ARGUMENT,
                    "Empty location given! Please specify a valid direcotry as installation location.",
                    null);
        }

        final File target = new File(location);

        if (!target.exists()) {
            throw new ApplicationException(
                    ExitCodeImpl.BAD_ARGUMENT,
                    String.format("Install location '%s' does not exist!", location),
                    null);
        }

        if (!target.isDirectory()) {
            throw new ApplicationException(
                    ExitCodeImpl.BAD_ARGUMENT,
                    String.format("Install location '%s' is not a directory!", location),
                    null);
        }

        return target;
    }

}

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
package de.weltraumschaf.juberblog.core;

import com.beust.jcommander.Parameter;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import java.util.Objects;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Options {

    /**
     * Command line usage.
     */
    private static final String USAGE
            = SubCommand.Name.CREATE.name().toLowerCase() + "|"
            + SubCommand.Name.INSTALL.name().toLowerCase() + "|"
            + SubCommand.Name.PUBLISH.name().toLowerCase()
            + " [-h] [-v] -c <file> -l <dir>";
    /**
     * Help description.
     */
    private static final String DESCRIPTION = "Commandline tool to manage your blog.";
    /**
     * Help example.
     */
    private static final String EXAMPLE = "TODO";

    /**
     * Command line options parser.
     */
    private static final JCommanderImproved<Options> PROVIDER
            = new JCommanderImproved<>(Constants.COMMAND_NAME.toString(), Options.class);

    @Parameter(
            names = {"-h", "--help"},
            description = "Show this help.")
    private boolean help;
    @Parameter(
            names = {"-v", "--version"},
            description = "Show the version.")
    private boolean version;
    /**
     * Where is the blog installed.
     */
    @Parameter(
            names = {"-l", "--location"},
            //            required = true,
            description = "Location of the blog installation.")
    private String location = "";
    /**
     * Configuration file argument.
     */
    @Parameter(
            names = {"-c", "--config"},
            //            required = true,
            description = "Config file to use.")
    private String configurationFile = "";

    public static Options gatherOptions(final String[] args) {
        return PROVIDER.gatherOptions(args);
    }

    public static String helpMessage() {
        return PROVIDER.helpMessage(USAGE, DESCRIPTION, EXAMPLE);
    }

    public static String usage() {
        return String.format("Usage: %s %s", Constants.COMMAND_NAME.toString(), USAGE);
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public String getLocation() {
        return location;
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configurationFile, help, location, version);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Options)) {
            return false;
        }

        final Options other = (Options) obj;
        return Objects.equals(configurationFile, other.configurationFile)
                && Objects.equals(help, other.help)
                && Objects.equals(location, other.location)
                && Objects.equals(version, other.version);
    }

}

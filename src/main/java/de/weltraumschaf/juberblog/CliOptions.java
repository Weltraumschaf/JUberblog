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
package de.weltraumschaf.juberblog;

import com.beust.jcommander.Parameter;
import org.apache.commons.lang3.Validate;

/**
 * Command line options.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliOptions {

    private final String subCommandName;

    /**
     * Help flag.
     */
    @Parameter(names = {"-h", "--help" }, description = "Show this message.", help = true)
    private boolean help;
    /**
     * Verbose flag.
     */
    @Parameter(names = {"-v", "--verbose" }, description = "Tell you more.")
    private boolean verbose;
    /**
     * Configuration file argument.
     */
    @Parameter(names = {"-c", "--config" }, description = "Config file to use. [required]")
    private String configurationFile = "";
    /**
     * Purge flag.
     */
    @Parameter(names = {"-p", "--purge" }, description = "Regenerate all blog posts and sites.")
    private boolean purge;
    /**
     * Quiet flag.
     */
    @Parameter(names = {"-q", "--quiet" }, description = "Be quiet and don't post to social networks.")
    private boolean quiet;
    /**
     * Site flag.
     */
    @Parameter(names = {"-s", "--site" }, description = "Publish sites. / Create sites.")
    private boolean sites;
    /**
     * Draft flag.
     */
    @Parameter(names = {"-d", "--draft" }, description = "Publish drafts. / Create draft.")
    private boolean draft;
    /**
     * Title argument.
     */
    @Parameter(names = {"-t", "--title" }, description = "Title of the blog post.")
    private String title = "";
    /**
     * Where to install the blog scaffold.
     */
    @Parameter(names = {"-l", "--location" }, description = "Install location.")
    private String location = "";

    /**
     * Dedicated constructor.
     *
     * @param subCommandName must not be {@code null}
     */
    public CliOptions(final String subCommandName) {
        super();
        Validate.notNull(subCommandName, "Sub command must not be null!");
        this.subCommandName = subCommandName;
    }

    public String getSubCommandName() {
        return subCommandName;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

    public boolean isPurge() {
        return purge;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public boolean isSites() {
        return sites;
    }

    public boolean isDraft() {
        return draft;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

}

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

/**
 * Command line options.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliOptions {

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
    private String configurationFile;
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
    private String title;

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
}

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
package de.weltraumschaf.juberblog.opt;

import com.beust.jcommander.Parameter;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishOptions extends CreateAndPublishOptions {

    /**
     * Purge flag.
     */
    @Parameter(names = {"-p", "--purge"}, description = "Regenerate all blog posts and sites.")
    private boolean purge;
    /**
     * Quiet flag.
     */
    @Parameter(names = {"-q", "--quiet"}, description = "Be quiet and don't post to social networks.")
    private boolean quiet;
    /**
     * Site flag.
     */
    @Parameter(names = {"-s", "--site"}, description = "Publish sites.")
    private boolean sites;

    public PublishOptions() {
        super();
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
}

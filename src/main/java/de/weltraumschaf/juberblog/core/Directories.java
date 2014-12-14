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

import de.weltraumschaf.commons.validate.Validate;
import java.nio.file.Path;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Directories {

    private final Path postsData;
    private final Path sitesData;
    private final Path output;
    private final Path postsOutput;
    private final Path siteOutput;

    public Directories(
            final Path postsData,
            final Path sitesData,
            final Path output,
            final Path postsOutput,
            final Path siteOutput)
    {
        super();
        this.postsData = Validate.notNull(postsData, "postsData");
        this.sitesData = Validate.notNull(sitesData, "sitesData");
        this.output = Validate.notNull(output, "output");
        this.postsOutput = Validate.notNull(postsOutput, "postsOutput");
        this.siteOutput = Validate.notNull(siteOutput, "siteOutput");
    }

    public Path getPostsData() {
        return postsData;
    }

    public Path getSitesData() {
        return sitesData;
    }

    public Path getOutput() {
        return output;
    }

    public Path getPostsOutput() {
        return postsOutput;
    }

    public Path getSiteOutput() {
        return siteOutput;
    }

}

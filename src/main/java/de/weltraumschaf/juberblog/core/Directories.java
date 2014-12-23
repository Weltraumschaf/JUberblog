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
import java.util.Objects;

/**
 * Holds the directories to read and write the blog files.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Directories {

    /**
     * Where to find the post's Markdown files.
     */
    private final Path postsData;
    /**
     * Where to find the site's Markdown files.
     */
    private final Path sitesData;
    /**
     * Where to store published files.
     */
    private final Path output;
    /**
     * Where to store published posts.
     */
    private final Path postsOutput;
    /**
     * Where to store published sites.
     */
    private final Path siteOutput;

    /**
     * Dedicated constructor.
     *
     * @param postsData must not be {@code null}
     * @param sitesData must not be {@code null}
     * @param output must not be {@code null}
     * @param postsOutput must not be {@code null}
     * @param siteOutput must not be {@code null}
     */
    public Directories(
            final Path postsData,
            final Path sitesData,
            final Path output,
            final Path postsOutput,
            final Path siteOutput) {
        super();
        this.postsData = Validate.notNull(postsData, "postsData");
        this.sitesData = Validate.notNull(sitesData, "sitesData");
        this.output = Validate.notNull(output, "output");
        this.postsOutput = Validate.notNull(postsOutput, "postsOutput");
        this.siteOutput = Validate.notNull(siteOutput, "siteOutput");
    }

    /**
     * Where to find the posts data files.
     *
     * @return never {@code null}
     */
    public Path getPostsData() {
        return postsData;
    }

    /**
     * Where to find the sites data files.
     *
     * @return never {@code null}
     */
    public Path getSitesData() {
        return sitesData;
    }

    /**
     * Where to store published output.
     *
     * @return never {@code null}
     */
    public Path getOutput() {
        return output;
    }

    /**
     * Where to store published posts.
     *
     * @return never {@code null}
     */
    public Path getPostsOutput() {
        return postsOutput;
    }

    /**
     * Where to store published sites.
     *
     * @return never {@code null}
     */
    public Path getSiteOutput() {
        return siteOutput;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                postsData.toString(),
                sitesData.toString(),
                output.toString(),
                postsOutput.toString(),
                siteOutput.toString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Directories)) {
            return false;
        }

        final Directories other = (Directories) obj;

        return Objects.equals(postsData.toString(), other.postsData.toString())
                && Objects.equals(sitesData.toString(), other.sitesData.toString())
                && Objects.equals(output.toString(), other.output.toString())
                && Objects.equals(postsOutput.toString(), other.postsOutput.toString())
                && Objects.equals(siteOutput.toString(), other.siteOutput.toString());
    }

    @Override
    public String toString() {
        return "Directories{"
                + "postsData=" + postsData.toString() + ", "
                + "sitesData=" + sitesData.toString() + ", "
                + "output=" + output.toString() + ", "
                + "postsOutput=" + postsOutput.toString() + ", "
                + "siteOutput=" + siteOutput.toString()
                + '}';
    }

}

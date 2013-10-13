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
 * Options for create commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateOptions extends CreateAndPublishOptions {

    /**
     * Site flag.
     */
    @Parameter(names = {"-s", "--site" }, description = "Create site draft.")
    private boolean site;
    /**
     * Title argument.
     */
    @Parameter(names = {"-t", "--title" }, description = "Title of the blog post.")
    private String title = "";

    /**
     * Whether to create a site or a post.
     *
     * @return {@code true} for site, {@code false} for post
     */
    public boolean isSite() {
        return site;
    }

    /**
     * Get title.
     *
     * @return never {@code null}, maybe empty
     */
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "CreateOptions{" + super.toString() + ", site: " + site + ", title: " + title + '}';
    }

}

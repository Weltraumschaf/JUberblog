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
import de.weltraumschaf.commons.validate.Validate;

/**
 * Options for create commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateOptions extends CreateAndPublishOptions {

    /**
     * Draft flag.
     */
    @Parameter(names = {"-d", "--draft" }, description = "Create site/post as draft.")
    private boolean draft;

    /**
     * Site flag.
     */
    @Parameter(names = {"-s", "--site" }, description = "Create site.")
    private boolean site;
    /**
     * Title argument.
     */
    @Parameter(names = {"-t", "--title" }, description = "Title of the blog post.")
    private String title;

    /**
     * Default constructor.
     */
    public CreateOptions() {
        this(false, false, "", false, false, "");
    }

    /**
     * Dedicated constructor.
     *
     * @param help {@code true} if help is wanted
     * @param verbose {@code true} if verbosity is wanted
     * @param configurationFile must not be {@code null}
     * @param draft whether to create a draft or not
     * @param site {@code true} to create a site, {@code false} to create a post
     * @param title must not be {@code null}
     */
    public CreateOptions(
        final boolean help,
        final boolean verbose,
        final String configurationFile,
        final boolean draft,
        final boolean site,
        final String title) {
        super(help, verbose, configurationFile);
        this.draft = draft;
        this.site = site;
        this.title = Validate.notNull(title, "Parameter 'title' must not be null!");
    }

    /**
     * Whether to create a draft.
     *
     * @return {@code true} for draft, {@code false} for not
     */
    public boolean isDraft() {
        return draft;
    }

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

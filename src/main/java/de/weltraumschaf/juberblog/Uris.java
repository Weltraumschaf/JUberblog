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

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.Validate;

/**
 * Provides URIs based on a base URI.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Uris {

    /**
     * Name of sites sub directory.
     */
    private static final String SITES_DIR = Constants.SITES_DIR.toString() + "/";
    /**
     * Name of posts sub directory.
     */
    private static final String POSTS_DIR = Constants.POSTS_DIR.toString() + "/";
    /**
     * Name of drafts sub directory.
     */
    private static final String DRAFTS_DIR = Constants.DRAFTS_DIR.toString() + "/";
    /**
     * Base URI.
     */
    private final URI base;

    /**
     * Convenience constructor.
     *
     * @param config must not be {@code null}
     * @throws URISyntaxException if {@link BlogConfiguration#getBaseUri()} is malformed
     */
    public Uris(final BlogConfiguration config) throws URISyntaxException {
        this(config.getBaseUri());
    }

    /**
     * Convenience constructor.
     *
     * @param base must not be {@code null}
     * @throws URISyntaxException is {@code base} is malformed
     */
    public Uris(final String base) throws URISyntaxException {
        this(new URI(base));
    }

    /**
     * Dedicated constructor.
     *
     * @param base must not be {@code null}
     */
    public Uris(final URI base) {
        super();
        Validate.notNull(base, "Base must not be null!");
        this.base = base;
    }

    /**
     * Base URI for published posts.
     *
     * @return never {@code null}
     */
    public URI posts() {
        return base.resolve(POSTS_DIR);
    }

    /**
     * Base URI for published sites.
     *
     * @return never {@code null}
     */
    public URI sites() {
        return base.resolve(SITES_DIR);
    }

    /**
     * Base URI for drafts.
     *
     * @return never {@code null}
     */
    public URI drafts() {
        return base.resolve(DRAFTS_DIR);
    }

    /**
     * Base URI for draft sites.
     *
     * @return never {@code null}
     */
    public URI draftSites() {
        return drafts().resolve(SITES_DIR);
    }

    /**
     * Base URI for draft posts.
     *
     * @return never {@code null}
     */
    public URI draftPosts() {
        return drafts().resolve(POSTS_DIR);
    }

}

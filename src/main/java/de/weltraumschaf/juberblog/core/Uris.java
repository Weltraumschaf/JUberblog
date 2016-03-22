package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.commons.uri.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides URIs based on a base URI.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Uris {

    /**
     * Name of sites sub directory.
     */
    private static final String SITES_DIR = "/" + Constants.SITES_DIR.toString();
    /**
     * Name of posts sub directory.
     */
    private static final String POSTS_DIR = "/" + Constants.POSTS_DIR.toString();
    /**
     * Name of drafts sub directory.
     */
    private static final String DRAFTS_DIR = "/" + Constants.DRAFTS_DIR.toString();
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
    public Uris(final Configuration config) throws URISyntaxException {
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
        this.base = Validate.notNull(base, "Base must not be null!");
    }

    /**
     * Base URI for published posts.
     *
     * @return never {@code null}
     */
    public URI posts() {
        return new UriBuilder()
                .uri(base)
                .path(POSTS_DIR)
                .build();
    }

    /**
     * Constructs URI for an post.
     *
     * @param filename must not be {@code null} or empty
     * @return never {@code null}
     */
    public URI post(final String filename) {
        return new UriBuilder()
                .uri(posts())
                .path(Validate.notEmpty(filename, "filename"))
                .build();
    }

    /**
     * Base URI for published sites.
     *
     * @return never {@code null}
     */
    public URI sites() {
        return new UriBuilder()
                .uri(base)
                .path(SITES_DIR)
                .build();
    }

    /**
     * Constructs URI for an post.
     *
     * @param filename must not be {@code null} or empty
     * @return never {@code null}
     */
    public URI site(final String filename) {
        return new UriBuilder()
                .uri(sites())
                .path(Validate.notEmpty(filename, "filename"))
                .build();
    }

    /**
     * Base URI for drafts.
     *
     * @return never {@code null}
     */
    public URI drafts() {
        return new UriBuilder()
                .uri(base)
                .path(DRAFTS_DIR)
                .build();
    }

    /**
     * Base URI for draft sites.
     *
     * @return never {@code null}
     */
    public URI draftSites() {
        return new UriBuilder()
                .uri(drafts())
                .path(SITES_DIR)
                .build();
    }

    /**
     * Base URI for draft posts.
     *
     * @return never {@code null}
     */
    public URI draftPosts() {
        return new UriBuilder()
                .uri(drafts())
                .path(POSTS_DIR)
                .build();
    }

}

package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Contains paths to all necessary templates.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Templates {

    /**
     * Outer layout template.
     */
    private final Path layoutTemplate;
    /**
     * Inner template for posts.
     */
    private final Path postTemplate;
    /**
     * Inner template for sites.
     */
    private final Path siteTemplate;
    /**
     * Template for feed XML.
     */
    private final Path feedTemplate;
    /**
     * Inner template for index.
     */
    private final Path indexTemplate;
    /**
     * Template for site map XML.
     */
    private final Path siteMapTemplate;
    /**
     * Template to create Markdown data files.
     */
    private final Path createSiteOrPostTemplate;

    /**
     * Dedicated constructor.
     *
     * @param layoutTemplate must not be {@code null}
     * @param postTemplate must not be {@code null}
     * @param siteTemplate must not be {@code null}
     * @param feedTemplate must not be {@code null}
     * @param indexTemplate must not be {@code null}
     * @param siteMapTemplate must not be {@code null}
     * @param createSiteOrPostTemplate must not be {@code null}
     */
    public Templates(
            final Path layoutTemplate,
            final Path postTemplate,
            final Path siteTemplate,
            final Path feedTemplate,
            final Path indexTemplate,
            final Path siteMapTemplate,
            final Path createSiteOrPostTemplate) {
        super();
        this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
        this.postTemplate = Validate.notNull(postTemplate, "postTemplate");
        this.siteTemplate = Validate.notNull(siteTemplate, "siteTemplate");
        this.feedTemplate = Validate.notNull(feedTemplate, "feedTemplate");
        this.indexTemplate = Validate.notNull(indexTemplate, "indexTemplate");
        this.siteMapTemplate = Validate.notNull(siteMapTemplate, "siteMapTemplate");
        this.createSiteOrPostTemplate = Validate.notNull(createSiteOrPostTemplate, "createSiteOrPostTemplate");
    }

    /**
     * Get the outer layout template.
     *
     * @return never {@code null}
     */
    public Path getLayoutTemplate() {
        return layoutTemplate;
    }

    /**
     * Get the inner post template.
     *
     * @return never {@code null}
     */
    public Path getPostTemplate() {
        return postTemplate;
    }

    /**
     * Get the inner site template.
     *
     * @return never {@code null}
     */
    public Path getSiteTemplate() {
        return siteTemplate;
    }

    /**
     * Get the feed XML template.
     *
     * @return never {@code null}
     */
    public Path getFeedTemplate() {
        return feedTemplate;
    }

    /**
     * Get the inner index template.
     *
     * @return never {@code null}
     */
    public Path getIndexTemplate() {
        return indexTemplate;
    }

    /**
     * Get the site map XML template.
     *
     * @return never {@code null}
     */
    public Path getSiteMapTemplate() {
        return siteMapTemplate;
    }

    /**
     * Get the Markdown template to create posts/sites.
     *
     * @return never {@code null}
     */
    public Path getCreateSiteOrPostTemplate() {
        return createSiteOrPostTemplate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                layoutTemplate,
                postTemplate,
                siteTemplate,
                feedTemplate,
                indexTemplate,
                siteMapTemplate,
                createSiteOrPostTemplate
        );
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Templates)) {
            return false;
        }

        final Templates other = (Templates) obj;
        return Objects.equals(layoutTemplate, other.layoutTemplate)
                && Objects.equals(postTemplate, other.postTemplate)
                && Objects.equals(siteTemplate, other.siteTemplate)
                && Objects.equals(feedTemplate, other.feedTemplate)
                && Objects.equals(indexTemplate, other.indexTemplate)
                && Objects.equals(siteMapTemplate, other.siteMapTemplate)
                && Objects.equals(createSiteOrPostTemplate, other.createSiteOrPostTemplate);
    }

    @Override
    public String toString() {
        return "Templates{"
                + "layoutTemplate=" + layoutTemplate + ", "
                + "postTemplate=" + postTemplate + ", "
                + "siteTemplate=" + siteTemplate + ", "
                + "feedTemplate=" + feedTemplate + ", "
                + "indexTemplate=" + indexTemplate + ", "
                + "siteMapTemplate=" + siteMapTemplate + ", "
                + "createSiteOrPostTemplate=" + createSiteOrPostTemplate
                + '}';
    }

}

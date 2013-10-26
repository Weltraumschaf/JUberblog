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
package de.weltraumschaf.juberblog.formatter;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.weltraumschaf.juberblog.template.Template;
import de.weltraumschaf.juberblog.template.VarName;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.Validate;

/**
 * Formats an RSS feed.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class FeedFormatter implements Formatter {

    /**
     * Template for XML.
     */
    private static final String TEMPLATE = "feed.ftl";
    /**
     * used to render XML.
     */
    private final Template content;
    /**
     * Data model.
     */
    private final Feed feed;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@literal nul}
     * @param feed must not be {@literal nul}
     * @throws IOException on any template IO error
     */
    public FeedFormatter(final Configuration templateConfiguration, final Feed feed) throws IOException {
        super();
        Validate.notNull(templateConfiguration, "Template configuration must not be null!");
        Validate.notNull(feed, "Feedmust not be null!");
        content = new Template(templateConfiguration, TEMPLATE);
        this.feed = feed;
    }

    @Override
    public String format() throws IOException, TemplateException {
        content.assignVariable("feed", feed);
        return content.render();
    }

    @Override
    public void setEncoding(final String encoding) {
        content.assignVariable(VarName.ENCODING, encoding);
    }

    /**
     * Models a RSS feed.
     */
    public static final class Feed {

        /**
         * Feed title.
         */
        private final String title;
        /**
         * Feed URI.
         */
        private final String link;
        /**
         * Feed description.
         */
        private final String description;
        /**
         * Feed language.
         */
        private final String language;
        /**
         * Feed generation.
         */
        private final String lastBuildDate;
        /**
         * Feed items.
         */
        private final List<FeedItem> items = Lists.newArrayList();

        /**
         * Dedicated constructor.
         *
         * @param title must not be {@literal null} or empty
         * @param link must not be {@literal null} or empty
         * @param description must not be {@literal null} or empty
         * @param language must not be {@literal null} or empty
         * @param lastBuildDate must not be {@literal null} or empty
         */
        public Feed(String title, String link, String description, String language, String lastBuildDate) {
            super();
            Validate.notEmpty(title, "Title must not be null or empty!");
            Validate.notEmpty(link, "Link must not be null or empty!");
            Validate.notEmpty(description, "Description must not be null or empty!");
            Validate.notEmpty(language, "Language must not be null or empty!");
            Validate.notEmpty(lastBuildDate, "LastBuildDate must not be null or empty!");
            this.title = title;
            this.link = link;
            this.description = description;
            this.language = language;
            this.lastBuildDate = lastBuildDate;
        }

        /**
         * Get the feed title.
         *
         * @return Never {@literal null} or empty
         */
        public String getTitle() {
            return title;
        }

        /**
         * Get the feed URI.
         *
         * @return Never {@literal null} or empty
         */
        public String getLink() {
            return link;
        }

        /**
         * Get the feed description.
         *
         * @return Never {@literal null} or empty
         */
        public String getDescription() {
            return description;
        }

        /**
         * Get the feed language.
         *
         * @return Never {@literal null} or empty
         */
        public String getLanguage() {
            return language;
        }

        /**
         * Get the last build date.
         *
         * @return Never {@literal null} or empty
         */
        public String getLastBuildDate() {
            return lastBuildDate;
        }

        /**
         * Get the feed items.
         *
         * @return never {@literal null}
         */
        public List<FeedItem> getItems() {
            return items;
        }

        /**
         * Add feed item.
         *
         * @param item must not be {@code null}
         * @return never {@code null}
         */
        public boolean add(final FeedItem item) {
            return items.add(item);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(title, link, description, language, lastBuildDate, items);
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Feed)) {
                return false;
            }

            final Feed other = (Feed) obj;
            return Objects.equal(title, other.title)
                    && Objects.equal(link, other.link)
                    && Objects.equal(description, other.description)
                    && Objects.equal(language, other.language)
                    && Objects.equal(lastBuildDate, other.lastBuildDate)
                    && Objects.equal(items, other.items);
        }

    }

    /**
     * Models a RSS feed item.
     */
    public static final class FeedItem {

        /**
         * Title of blog post.
         */
        private final String title;
        /**
         * URI of blog post.
         */
        private final String link;
        /**
         * Description of blog post.
         */
        private final String description;
        /**
         * Publishing date of blog post.
         */
        private final String pubDate;
        /**
         * Publishing date of blog post.
         */
        private final String dcDate;

        /**
         * Dedicated constructor.
         *
         * @param title must not be {@literal null} or empty
         * @param link must not be {@literal null} or empty
         * @param description must not be {@literal null} or empty
         * @param pubDate must not be {@literal null} or empty
         * @param dcDate must not be {@literal null} or empty
         */
        public FeedItem(String title, String link, String description, String pubDate, String dcDate) {
            super();
            Validate.notEmpty(title, "Title must not be null or empty!");
            Validate.notEmpty(link, "Link must not be null or empty!");
            Validate.notEmpty(description, "Description must not be null or empty!");
            Validate.notEmpty(pubDate, "PubDate must not be null or empty!");
            Validate.notEmpty(dcDate, "DcDate must not be null or empty!");
            this.title = title;
            this.link = link;
            this.description = description;
            this.pubDate = pubDate;
            this.dcDate = dcDate;
        }

        /**
         * Get the blog post title.
         *
         * @return Never {@literal null} or empty
         */
        public String getTitle() {
            return title;
        }

        /**
         * Get the blog post URI.
         *
         * @return Never {@literal null} or empty
         */
        public String getLink() {
            return link;
        }

        /**
         * Get the blog post description.
         *
         * @return Never {@literal null} or empty
         */
        public String getDescription() {
            return description;
        }

        /**
         * Get the blog post publishing date.
         *
         * @return Never {@literal null} or empty
         */
        public String getPubDate() {
            return pubDate;
        }

        /**
         * Get the blog post publishing date.
         *
         * @return Never {@literal null} or empty
         */
        public String getDcDate() {
            return dcDate;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(title, link, description, pubDate, dcDate);
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof FeedItem)) {
                return false;
            }

            final FeedItem other = (FeedItem) obj;
            return Objects.equal(title, other.title)
                    && Objects.equal(link, other.link)
                    && Objects.equal(description, other.description)
                    && Objects.equal(pubDate, other.pubDate)
                    && Objects.equal(dcDate, other.dcDate);
        }
    }

}

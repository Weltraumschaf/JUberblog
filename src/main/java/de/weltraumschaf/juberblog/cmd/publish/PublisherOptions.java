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
package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.BlogConfiguration;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.model.PublishedPages;
import freemarker.template.Configuration;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class PublisherOptions {

    private Directories dirs;
    private Configuration templateConfig;
    private String baseUri;
    private Version version;
    private BlogConfiguration blogConfig;
    private boolean publishSites;
    private boolean publishPages;

    private PublisherOptions() {
        super();
    }

    Directories getDirs() {
        return dirs;
    }

    Configuration getTemplateConfig() {
        return templateConfig;
    }

    String getBaseUri() {
        return baseUri;
    }

    Version getVersion() {
        return version;
    }

    BlogConfiguration getBlogConfig() {
        return blogConfig;
    }

    boolean isPublishSites() {
        return publishSites;
    }

    boolean isPublishPages() {
        return publishPages;
    }

    final static class Builder {

        private Directories dirs;
        private Configuration templateConfig;
        private String baseUri;
        private Version version;
        private BlogConfiguration blogConfig;
        private boolean publishSites;
        private boolean publishPages;

        Builder create() {
            return new Builder();
        }

        PublisherOptions product() {
            final PublisherOptions prdocut = new PublisherOptions();
            prdocut.baseUri = baseUri;
            prdocut.blogConfig = blogConfig;
            prdocut.dirs = dirs;
            prdocut.version = version;
            prdocut.templateConfig = templateConfig;
            prdocut.publishSites = publishSites;
            prdocut.publishPages = publishPages;
            return prdocut;
        }

        Builder dirs(final Directories dirs) {
            this.dirs = Validate.notNull(dirs, "dirs");
            return this;
        }

        Builder templateConfig(final Configuration templateConfig) {
            this.templateConfig = Validate.notNull(templateConfig, "templateConfig");
            return this;
        }

        Builder baseUri(final String baseUri) {
            this.baseUri = Validate.notEmpty(baseUri, "baseUri");
            return this;
        }

        Builder version(final Version version) {
            this.version = Validate.notNull(version, "version");
            return this;
        }

        Builder blogConfig(final BlogConfiguration blogConfig) {
            this.blogConfig = Validate.notNull(blogConfig, "blogConfig");
            return this;
        }

        Builder publishSites(final boolean onOff) {
            this.publishSites = onOff;
            return this;
        }

        Builder publishPages(final boolean onOff) {
            this.publishPages = onOff;
            return this;
        }

    }
}

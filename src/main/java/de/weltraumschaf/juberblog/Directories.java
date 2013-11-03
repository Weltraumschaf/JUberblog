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

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Provides file system for all necessary directories.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Directories {

    /**
     * Name of sites sub directory.
     */
    private static final String SITES_DIR = "sites";
    /**
     * Name of posts sub directory.
     */
    private static final String POSTS_DIR = "posts";
    /**
     * Name of drafts sub directory.
     */
    private static final String DRAFTS_DIR = "drafts";
    private static final FileSystem DEFAULT_FILES_YSTEM = FileSystems.getDefault();
    /**
     * A default file system.
     */
    private final FileSystem fs;
    private final String dataDir;
    private final String templateDir;
    private final String htdocsDir;

    /**
     *
     *
     * @param config must not be {@literal null}
     */
    public Directories(final BlogConfiguration config) {
        this(config.getDataDir(), config.getTemplateDir(), config.getHtdocs());
    }

    public Directories(final String dataDir, final String templateDir, final String htdocsDir) {
        this(dataDir, templateDir, htdocsDir, DEFAULT_FILES_YSTEM);
    }

    /**
     * Dedicated constructor.
     *
     * @param dataDir
     * @param templateDir
     * @param htdocsDir
     */
    public Directories(final String dataDir, final String templateDir, final String htdocsDir, final FileSystem fs) {
        super();
        this.dataDir = dataDir;
        this.templateDir = templateDir;
        this.htdocsDir = htdocsDir;
        this.fs = fs;
    }

    /**
     * Get the data directory path.
     *
     * @return never {@literal null}
     */
    public Path data() {
        return fs.getPath(dataDir);
    }

    /**
     * Get the directory where the sites are stored.
     *
     * @return never {@literal null}
     */
    public Path dataSites() {
        return data().resolve(SITES_DIR);
    }

    /**
     * Get the directory where the posts are stored.
     *
     * @return never {@literal null}
     */
    public Path dataPosts() {
        return data().resolve(POSTS_DIR);
    }

    /**
     * Get the directory where the drafts are stored.
     *
     * @return never {@literal null}
     */
    private Path dataDrafts() {
        return data().resolve(DRAFTS_DIR);
    }

    /**
     * Get the directory where the site drafts are stored.
     *
     * @return never {@literal null}
     */
    public Path dataDraftSites() {
        return dataDrafts().resolve(SITES_DIR);
    }

    /**
     * Get the directory where the post drafts are stored.
     *
     * @return never {@literal null}
     */
    public Path dataDraftPosts() {
        return dataDrafts().resolve(POSTS_DIR);
    }

    /**
     * Get the template directory path.
     *
     * @return never {@literal null}
     */
    public Path templates() {
        return fs.getPath(templateDir);
    }

    /**
     * Get the htdocs directory path.
     *
     * @return never {@literal null}
     */
    public Path htdocs() {
        return fs.getPath(htdocsDir);
    }

    /**
     * Get the directory to where sites will be published.
     *
     * @return never {@literal null}
     */
    public Path htdocsSites() {
        return htdocs().resolve(SITES_DIR);
    }

    /**
     * Get the directory to where posts will be published.
     *
     * @return never {@literal null}
     */
    public Path htdocsPosts() {
        return htdocs().resolve(POSTS_DIR);
    }

    /**
     * Get the directory to where drafts will be published.
     *
     * @return never {@literal null}
     */
    private Path htdocsDrafts() {
        return htdocs().resolve(DRAFTS_DIR);
    }

    /**
     * Get the directory to where site drafts will be published.
     *
     * @return never {@literal null}
     */
    public Path htdocsDraftSites() {
        return htdocsDrafts().resolve(SITES_DIR);
    }

    /**
     * Get the directory to where post drafts will be published.
     *
     * @return never {@literal null}
     */
    public Path htdocsDraftPosts() {
        return htdocsDrafts().resolve(DRAFTS_DIR);
    }

}

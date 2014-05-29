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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.files.FilenameFilters;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.formatter.HtmlFormatter;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * Publish all files from data directory.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Publisher implements Command {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Publisher.class);
    /**
     * Used to find data and write published files.
     */
    private final Directories dirs;
    /**
     * USed for HTML generation.
     */
    private final Configuration templateConfig;
    /**
     * Base URI of the blog.
     */
    private final String baseUri;
    /**
     * Whether to generate sites.
     */
    private boolean sites;
    /**
     * Whether to purge already generated files.
     */
    private boolean purge;
    /**
     * {@code true} if data already read.
     *
     * Used for lazy load {@link #sitesData} and {@link #postsData}.
     */
    private boolean dataRead;
    /**
     * Data about sites to publish.
     *
     * Lazy loaded.
     */
    private Collection<DataFile> sitesData;
    /**
     * Data about posts to publish.
     *
     * Lazy loaded.
     */
    private Collection<DataFile> postsData;

    /**
     * Dedicated constructor.
     *
     * @param dirs must not be {@code null}
     * @param templateConfig must not be {@code null}
     * @param baseUri must not be {@code null} or empty
     */
    public Publisher(final Directories dirs, final Configuration templateConfig, final String baseUri) {
        super();
        this.dirs = Validate.notNull(dirs, "dirs");
        this.templateConfig = Validate.notNull(templateConfig, "templateConfig");
        this.baseUri = Validate.notEmpty(baseUri, "baseUri");
    }

    /**
     * Read the data to publish.
     *
     * @throws FileNotFoundException if data files can't be read
     */
    void readData() throws FileNotFoundException {
        if (dataRead) {
            return;
        }

        sitesData = Lists.newArrayList();

        for (final File f : dirs.dataSites().toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            sitesData.add(new DataFile(f));
        }

        postsData = Lists.newArrayList();

        for (final File f : dirs.dataPosts().toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            postsData.add(new DataFile(f));
        }

        dataRead = true;
    }

    /**
     * Get the data for sites to publish.
     *
     * @return never {@code null}
     * @throws FileNotFoundException if data files can't be read
     */
    Collection<DataFile> getSitesData() throws FileNotFoundException {
        readData();
        return sitesData;
    }

    /**
     * Get the data for posts to publish.
     *
     * @return never {@code null}
     * @throws FileNotFoundException if data files can't be read
     */
    Collection<DataFile> getPostsData() throws FileNotFoundException {
        readData();
        return postsData;
    }

    @Override
    public void execute() throws PublishingSubCommandExcpetion {
        final Collection<Page> publishedSites = isSites() ? publishSites() : Lists.<Page>newArrayList();
        final Collection<Page> publishedPosts = publisPosts();
        updateIndexes(publishedSites, publishedPosts);
    }

    /**
     * Whether to publish sites or not.
     *
     * @param sites {@code true} to publish sites
     */
    public void setSites(boolean sites) {
        this.sites = sites;
    }

    /**
     * Whether to publish sites or not.
     *
     * @return {@code true} for publish sites
     */
    private boolean isSites() {
        return sites;
    }

    /**
     * Whether to purge published files or not.
     *
     * @param purge {@code true} to purge files
     */
    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    /**
     * Whether to purge published files or not.
     *
     * @return {@code true} for purge files
     */
    private boolean isPurge() {
        return purge;
    }

    /**
     * Publish sites.
     *
     * @return never {@code null}
     * @throws PublishingSubCommandExcpetion if site data files can't be read
     */
    private Collection<Page> publishSites() throws PublishingSubCommandExcpetion {
        LOG.info("Publish sites...");

        try {
            return publishFiles(
                    Formatters.Type.SITE,
                    getSitesData(),
                    dirs.htdocsSites());
        } catch (FileNotFoundException ex) {
            throw new PublishingSubCommandExcpetion("Can't read sites data files: " + ex.getMessage(), ex);
        }
    }

    /**
     * Publish posts.
     *
     * @return never {@code null}
     * @throws PublishingSubCommandExcpetion if site data files can't be read
     */
    private Collection<Page> publisPosts() throws PublishingSubCommandExcpetion {
        LOG.info("Publish posts...");

        try {
            return publishFiles(
                    Formatters.Type.POST,
                    getPostsData(),
                    dirs.htdocsPosts());
        } catch (FileNotFoundException ex) {
            throw new PublishingSubCommandExcpetion("Can't read posts data files: " + ex.getMessage(), ex);
        }
    }

    /**
     * Update indexes.
     *
     * @param sites must not be {@code null}
     * @param posts must not be {@code null}
     */
    private void updateIndexes(final Collection<Page> sites, final Collection<Page> posts) {
        updateHomeSite();
        updateSiteMap();
        updateFeed();
    }

    /**
     * Publish all files in given directory with given layout.
     *
     * @param type must not be {@literal null}
     * @param data must not be {@literal null}
     * @param outputDir must not be {@literal null}
     * @return never {@code null}
     * @throws PublishingSubCommandExcpetion if can't render template
     */
    private Collection<Page> publishFiles(
        final Formatters.Type type,
        final Collection<DataFile> data,
        final Path outputDir)
        throws PublishingSubCommandExcpetion {
        Validate.notNull(type, "type");
        Validate.notNull(data, "data");
        Validate.notNull(outputDir, "outputDir");
        LOG.debug(String.format("Pubish files from '%s'...", data));
        final List<Page> published = Lists.newArrayList();

        for (final DataFile file : data) {
            published.add(publishFile(type, file, outputDir));
        }

        return published;
    }

    /**
     * Publish a given file with a given layout.
     *
     * @param type must not be {@literal null}
     * @param data must not be {@literal null} or empty
     * @param outputDir must not be {@literal null}
     * @return never {@code null}
     * @throws PublishingSubCommandExcpetion if can't render template
     */
    private Page publishFile(final Formatters.Type type, final DataFile data, final Path outputDir)
            throws PublishingSubCommandExcpetion {
        Validate.notNull(type, "Layout must not be null!");
        Validate.notNull(data, "File name must not be null or empty!");
        Validate.notNull(outputDir, "Output dir must not be null!");
        final String targetFileName = data.getSlug() + ".html";
        LOG.info(String.format("Publishing file '%s'...", targetFileName));
        final Path target = outputDir.resolve(targetFileName);
        final SiteMapUrl.ChangeFrequency frequencey;
        final SiteMapUrl.Priority priority;

        switch (type) {
            case POST:
                frequencey = SiteMapUrl.ChangeFrequency.DAILY;
                priority = SiteMapUrl.Priority.POST;
                break;
            case SITE:
                frequencey = SiteMapUrl.ChangeFrequency.WEEKLY;
                priority = SiteMapUrl.Priority.SITE;
                break;
            default:
                frequencey = SiteMapUrl.ChangeFrequency.MONTHLY;
                priority = SiteMapUrl.Priority.SITE;
                break;
        }


        if (publishedFileExists(target.toFile())) {
            LOG.info(String.format("File %s already exists.", target));

            if (isPurge()) {
                LOG.info("Purge option is true. File will be republished.");
            } else {
                LOG.info("Skip file.");
                try {
                    return new Page(
                        data.getHeadline(),
                        createUri(data),
                        "TODO",
                        new DateTime(),
                        data,
                        frequencey,
                        priority);
                } catch (final URISyntaxException | IOException ex) {
                    throw new PublishingSubCommandExcpetion(ex.getMessage(), ex);
                }
            }
        }

        try {
            final String html = format(type, data);
            LOG.info(String.format("Write published file to '%s'.", target));
            Files.createFile(target);
            Files.write(
                    target,
                    html.getBytes(Constants.DEFAULT_ENCODING.toString()),
                    StandardOpenOption.WRITE);
            return new Page(
                data.getHeadline(),
                createUri(data),
                html,
                new DateTime(),
                data,
                frequencey,
                priority);
        } catch (final IOException | TemplateException | URISyntaxException ex) {
            throw new PublishingSubCommandExcpetion(
                    String.format("Error occured during publishing: %s!", ex.getMessage()),
                    ex);
        }
    }

    /**
     * Format data to HTML.
     *
     * @param type must not be {@code null}
     * @param data must not be {@code null}
     * @return never {@code null}
     * @throws TemplateException if any template rendering error occurs
     * @throws IOException if template files can't be read
     */
    private String format(final Formatters.Type type, final DataFile data) throws TemplateException, IOException {
        final HtmlFormatter fmt;

        if (type == Formatters.Type.POST) {
            fmt = Formatters.createPostFormatter(templateConfig, data.getMarkdown());
        } else if (type == Formatters.Type.SITE) {
            fmt = Formatters.createSiteFormatter(templateConfig, data.getMarkdown());
        } else {
            throw new IllegalArgumentException(String.format("Unsupported type %s!", type));
        }

        fmt.setTitle(data.getHeadline());
        fmt.setEncoding(Constants.DEFAULT_ENCODING.toString());
        fmt.setBaseUri(baseUri);
        fmt.setDescription(data.getDescription());
        fmt.setKeywords(data.getKeywords());

        return fmt.format();
    }

    /**
     * Checks if a file to be published already exists.
     *
     * @param file must not be {@literal null}
     * @return {@literal true} if file already exists, else {@literal false}
     */
    boolean publishedFileExists(final File file) {
        return file.exists();
    }

    /**
     * Update the home site.
     */
    private void updateHomeSite() {
        LOG.info("Update home site...");
        new HomeSiteGenerator().execute();
    }

    /**
     * Update the site map.
     */
    private void updateSiteMap() {
        LOG.info("Update site map...");
        new SiteMapGenerator(templateConfig).execute();
    }

    /**
     * Update the RSS feed.
     */
    private void updateFeed() {
        LOG.info("Update feed...");
//        new FeedGenerator(templateConfig).execute();
    }

    private URI createUri(final DataFile data) throws URISyntaxException {
        return new URI("");
    }

}

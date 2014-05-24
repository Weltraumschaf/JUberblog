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
     * {@literal true} if data already read.
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

    public Publisher(final Directories dirs, final Configuration templateConfig, final String baseUri) {
        super();
        this.dirs = dirs;
        this.templateConfig = templateConfig;
        this.baseUri = baseUri;
    }

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

    Collection<DataFile> getSitesData() throws FileNotFoundException {
        readData();
        return sitesData;
    }

    Collection<DataFile> getPostsData() throws FileNotFoundException {
        readData();
        return postsData;
    }

    @Override
    public void execute() throws PublishingSubCommandExcpetion {
        final List<Page> publishedSites = isSites() ? publishSites() : Lists.<Page>newArrayList();
        final List<Page> publishedPosts = publisPosts();
        updateIndexes(publishedSites, publishedPosts);
    }

    public void setSites(boolean sites) {
        this.sites = sites;
    }

    private boolean isSites() {
        return sites;
    }

    public void setPurge(boolean purga) {
        this.purge = purga;
    }

    private boolean isPurge() {
        return purge;
    }

    private List<Page> publishSites() throws PublishingSubCommandExcpetion {
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

    private List<Page> publisPosts() throws PublishingSubCommandExcpetion {
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

    private void updateIndexes(final List<Page> sites, final List<Page> posts) {
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
     * @throws PublishingSubCommandExcpetion if can't render template
     */
    private List<Page> publishFiles(final Formatters.Type type, final Collection<DataFile> data, final Path outputDir)
            throws PublishingSubCommandExcpetion {
        Validate.notNull(type, "Layout must not be null!");
        Validate.notNull(data, "Dirname must not be null!");
        Validate.notNull(outputDir, "Output dir must not be null!");
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
                } catch (URISyntaxException | IOException ex) {
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

    private String format(final Formatters.Type type, final DataFile data) throws RuntimeException, TemplateException, IOException {
        final HtmlFormatter fmt;

        if (type == Formatters.Type.POST) {
            fmt = Formatters.createPostFormatter(templateConfig, data.getMarkdown());
        } else if (type == Formatters.Type.SITE) {
            fmt = Formatters.createSiteFormatter(templateConfig, data.getMarkdown());
        } else {
            throw new RuntimeException(); // TODO beter making!
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

    private void updateHomeSite() {
        LOG.info("Update home site...");
        new HomeSiteGenerator().execute();
    }

    private void updateSiteMap() {
        LOG.info("Update site map...");
        new SiteMapGenerator(templateConfig).execute();
    }

    private void updateFeed() {
        LOG.info("Update feed...");
//        new FeedGenerator(templateConfig).execute();
    }

    private URI createUri(DataFile data) throws URISyntaxException {
        return new URI("");
    }

}

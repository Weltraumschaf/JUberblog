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
package de.weltraumschaf.juberblog.cmd;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.weltraumschaf.juberblog.files.FilenameFilters;
import de.weltraumschaf.juberblog.files.PublishedFile;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;

/**
 * Generates the site map XML.
 *
 * 1. read all .html in htdocs/sites and htdocs/posts
 * 2. generate model
 * 3 generateXML
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class SiteMapGenerator implements Command {

    /**
     * Directories with published files.
     */
    private final Map<String, File> directoriesWithPublishedFiles = Maps.newHashMap();

    @Override
    public void execute() {

    }

    /**
     * Add directory to find published files with their base URI.
     *
     * @param baseUri must not be {@code nul} or empty
     * @param directory must not be {@code null}, must be directory
     */
    public void addDirecotry(final String baseUri, final File directory) {
        Validate.notEmpty(baseUri, "BaseURI must not be null or empty!");
        Validate.notNull(directory, "Directory must not be null!");
        Validate.isTrue(directory.isDirectory(), "Directory must be directory!");
        directoriesWithPublishedFiles.put(baseUri, directory);
    }

    Collection<PublishedFile> findPublishedFiles() {
        final List<PublishedFile> found= Lists.newArrayList();

        for (final Map.Entry<String, File> entry : directoriesWithPublishedFiles.entrySet()) {
            for (final File publishedFile : entry.getValue().listFiles(FilenameFilters.findHtmlFiles())) {
                found.add(new PublishedFile(entry.getKey(), publishedFile));
            }
        }

        return found;
    }

    public String getResult() {
        return "";
    }

}

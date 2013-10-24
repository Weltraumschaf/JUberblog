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

import de.weltraumschaf.juberblog.Headline;
import de.weltraumschaf.juberblog.MetaData;
import de.weltraumschaf.juberblog.Preprocessor;
import de.weltraumschaf.juberblog.Slug;
import de.weltraumschaf.juberblog.formatter.Formatter;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class DataProcessor {


    private final Preprocessor metaDataParser = new Preprocessor();
    private final Slug slugger = new Slug();
    private final Headline headliner = new Headline();
    private final InputStream input;
    private final Formatter fmt;
    /**
     * Lazy computed.
     */
    private MetaData metaData;
    /**
     * Lazy computed.
     */
    private String slug;
    /**
     * Lazy computed.
     */
    private String html;
    /**
     * Lazy computed.
     */
    private String markdown;
    /**
     * Lazy computed.
     */
    private String headline;

    /**
     * Dedicated constructor.
     *
     * @param dataFile must not be {@code null}
     * @param fmt must not be {@code null}
     */
    public DataProcessor(final InputStream dataFile, final Formatter fmt) {
        super();
        Validate.notNull(dataFile, "Data file must not be empty!");
        Validate.notNull(fmt, "Formatter file must not be empty!");
        this.input = dataFile;
        this.fmt = fmt;
    }

    /**
     * Get the processed HTML.
     *
     * @return never {@code null}
     * @throws IOException on any read error of data or template file
     * @throws TemplateException on any template error
     */
    public String getHtml() throws IOException, TemplateException {
        if (null == html) {
            getMetaData();
            // TOOO Assign template variables.
            html = fmt.format(markdown);
        }

        return html;
    }

    /**
     * Get the processed slug.
     *
     * @return never {@code null}
     * @throws IOException on any read error of data or template file
     */
    public String getSlug() throws IOException {
        if (null == slug) {
            getHeadline();
            slug = slugger.generate(headline);
        }

        return slug;
    }

    /**
     * Get the processed meta data.
     *
     * @return never {@code null}
     * @throws IOException on any read error of data or template file
     */
    public MetaData getMetaData() throws IOException {
        if (null == metaData) {
            final String raw = IOUtils.toString(input);
            IOUtils.closeQuietly(input);
            markdown = metaDataParser.process(raw);
            metaData = metaDataParser.getMetaData();
        }

        return metaData;
    }

    /**
     * Get the processed headline.
     *
     * @return never {@code null}
     * @throws IOException on any read error of data or template file
     */
    public String getHeadline() throws IOException {
        if (null == headline) {
            getMetaData();
            headline = headliner.find(markdown);
        }

        return headline;
    }

}

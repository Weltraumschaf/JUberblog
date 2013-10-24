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

import de.weltraumschaf.juberblog.Constants;
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

    /**
     * Parses meta data.
     */
    private final Preprocessor metaDataParser = new Preprocessor();
    /**
     * Generates slug.
     */
    private final Slug slugger = new Slug();
    /**
     * Extracts headline.
     */
    private final Headline headliner = new Headline();
    /**
     * To read data from.
     */
    private final InputStream input;
    /**
     * Formats HTML.
     */
    private final Formatter fmt;
    /**
     * Base URI used in templates.
     */
    private final String baseUri;
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
     * @param baseUri must not be {@code null} or empty
     */
    public DataProcessor(final InputStream dataFile, final Formatter fmt, final String baseUri) {
        super();
        Validate.notNull(dataFile, "Data file must not be empty!");
        Validate.notNull(fmt, "Formatter file must not be empty!");
        Validate.notEmpty(baseUri, "BaseUri must not be null or empty!");
        this.input = dataFile;
        this.fmt = fmt;
        this.baseUri = baseUri;
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
            fmt.setTitle(getHeadline());
            fmt.setEncoding(Constants.DEFAULT_ENCODING.toString());
            fmt.setBaseUri(baseUri);
            fmt.setDescription(metaData.getDescription());
            fmt.setKeywords(metaData.getKeywords());
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

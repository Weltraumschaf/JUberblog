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
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.formatter.HtmlFormatter;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

/**
 * Processes a data file.
 *
 * Processing includes:
 * <ul>
 * <li>meta data processing</li>
 * <li>headline extraction</li>
 * <li>slug generation</li>
 * <li>format HTML</li>
 * </ul>
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
    private final Formatters.Type type;
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
    private final Configuration templateConfig;

    /**
     * Dedicated constructor.
     *
     * @param dataFile must not be {@literal null}
     * @param type must not be {@literal null}
     * @param baseUri must not be {@literal null} or empty
     * @param templateConfig must not be {@literal null} or empty
     */
    public DataProcessor(final InputStream dataFile, final Formatters.Type type, final String baseUri, final Configuration templateConfig) {
        super();
        Validate.notNull(dataFile, "Data file must not be empty!");
        Validate.notNull(type, "Formatter file must not be empty!");
        Validate.notEmpty(baseUri, "BaseUri must not be null or empty!");
        this.input = dataFile;
        this.type = type;
        this.baseUri = baseUri;
        this.templateConfig = templateConfig;
    }

    /**
     * Get the processed HTML.
     *
     * @return never {@literal null}
     * @throws IOException on any read error of data or template file
     * @throws TemplateException on any template error
     */
    public String getHtml() throws IOException, TemplateException {
        if (null == html) {
            getMetaData();
            final HtmlFormatter fmt;

            if (type == Formatters.Type.POST) {
                fmt = Formatters.createPostFormatter(templateConfig, markdown);
            } else if (type == Formatters.Type.SITE) {
                fmt = Formatters.createSiteFormatter(templateConfig, markdown);
            } else {
                throw new RuntimeException(); // TODO beter making!
            }

            fmt.setTitle(getHeadline());
            fmt.setEncoding(Constants.DEFAULT_ENCODING.toString());
            fmt.setBaseUri(baseUri);
            fmt.setDescription(metaData.getDescription());
            fmt.setKeywords(metaData.getKeywords());
            html = fmt.format();
        }

        return html;
    }

    /**
     * Get the processed slug.
     *
     * @return never {@literal null}
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
     * @return never {@literal null}
     * @throws IOException on any read error of data or template file
     */
    public MetaData getMetaData() throws IOException {
        if (null == metaData) {
            final String raw = IOUtils.toString(input);
            markdown = metaDataParser.process(raw);
            metaData = metaDataParser.getMetaData();
        }

        return metaData;
    }

    /**
     * Get the processed headline.
     *
     * @return never {@literal null}
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

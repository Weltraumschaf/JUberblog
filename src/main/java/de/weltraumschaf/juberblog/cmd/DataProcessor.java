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

    private final InputStream input;
    private final Preprocessor metaDataParser = new Preprocessor();
    private final Slug slugger = new Slug();
    private final Formatter fmt;
    private boolean hasProcessed;
    private MetaData metaData = MetaData.DEFAULT;
    private String slug = "";
    private String html = "";

    public DataProcessor(final InputStream dataFile, final Formatter fmt) {
        super();
        Validate.notNull(dataFile, "Data file must not be empty!");
        Validate.notNull(fmt, "Formatter file must not be empty!");
        this.input = dataFile;
        this.fmt = fmt;
    }

    public void process() throws IOException, TemplateException {
        if (hasProcessed) {
            return;
        }

        final String raw = IOUtils.toString(input);
        IOUtils.closeQuietly(input);
        final String markdown = metaDataParser.process(raw);
        metaData = metaDataParser.getMetaData();
        slug = slugger.generate(markdown);
        // TOOO Assign template variables.
        html = fmt.format(markdown);
        hasProcessed = true;
    }

    public String getHtml() {
        assertProccessed();
        return html;
    }

    public String getSlug() {
        assertProccessed();
        return slug;
    }

    public MetaData getMetaData() {
        assertProccessed();
        return metaData;
    }

    private void assertProccessed() {
        if (hasProcessed) {
            return;
        }

        throw new IllegalStateException("Not processed yet! Invoke #process() first.");
    }
}

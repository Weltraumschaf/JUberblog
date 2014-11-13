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

import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.template.Configurations;
import de.weltraumschaf.juberblog.template.TemplateDirectories;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link SiteFormatter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SiteFormatterTest {

    private static final String FIXTURE_PACKAGE = Constants.PACKAGE_BASE.toString() + "/formatter";

    @Test
    public void format() throws IOException, URISyntaxException, TemplateException {
        final InputStream markdownFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/site.md");
        final HtmlFormatter sut = new SiteFormatter(
                Configurations.forTests(),
                IOUtils.toString(markdownFile),
                TemplateDirectories.scaffold());
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/site.html");
        final String formatedHtml = sut.format();
        assertThat(formatedHtml, is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(markdownFile);
        IOUtils.closeQuietly(htmlFile);
    }

}

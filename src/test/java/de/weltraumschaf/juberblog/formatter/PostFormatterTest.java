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
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link PostFormatter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PostFormatterTest {

    private static final String FIXTURE_PACKAGE = Constants.PACKAGE_BASE.toString() + "/formatter";

    @Test
    public void format() throws IOException, URISyntaxException, TemplateException {
        final InputStream markdownFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/post.md");
        final PostFormatter sut = new PostFormatter(
                Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR),
                IOUtils.toString(markdownFile));
        sut.setDateFormatted("date");
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/post.html");
        final String formatedHtml = sut.format();
        assertThat(formatedHtml, is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(markdownFile);
        IOUtils.closeQuietly(htmlFile);
    }

}

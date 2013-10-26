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
import org.junit.Ignore;

/**
 * Tests for {@link FeedFormatter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FeedFormatterTest {

    private static final String FIXTURE_PACKAGE = Constants.PACKAGE_BASE.toString() + "/formatter";
    private final FeedFormatter.Feed feed = new FeedFormatter.Feed(
            "The Title",
            "http://www.blog.de/",
            "The description.",
            "en",
            "Sat, 07 Apr 2012 00:16:23 +0200");

    @Test
    public void format_emptyItems() throws IOException, URISyntaxException, TemplateException {
        final FeedFormatter sut = new FeedFormatter(Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR), feed);
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/feed_empty.html");
        assertThat(sut.format(), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

    @Test
    public void format_oneItem() throws IOException, URISyntaxException, TemplateException {
        feed.add(new FeedFormatter.FeedItem("title1", "link1", "desc1", "date1", "dcDate1"));
        final FeedFormatter sut = new FeedFormatter(Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR), feed);
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/feed_one.html");
        assertThat(sut.format(), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

    @Test @Ignore
    public void format_threeItems() throws IOException, URISyntaxException, TemplateException {
        feed.add(new FeedFormatter.FeedItem("title1", "link1", "desc1", "date1", "dcDate1"));
        feed.add(new FeedFormatter.FeedItem("title2", "link2", "desc2", "date2", "dcDate2"));
        feed.add(new FeedFormatter.FeedItem("title3", "link3", "desc3", "date3", "dcDate3"));
        final FeedFormatter sut = new FeedFormatter(Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR), feed);
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/feed_three.html");
        assertThat(sut.format(), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

}

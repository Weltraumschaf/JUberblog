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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.Options;
import de.weltraumschaf.freemarkerdown.PreProcessor;
import de.weltraumschaf.freemarkerdown.PreProcessors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest {

    private static final String BASE = "/de/weltraumschaf/juberblog/";

    private Path createPath(final String name) throws URISyntaxException {
        return Paths.get(getClass().getResource(BASE + name).toURI());
    }

    @Test
    public void testSomeMethod() throws URISyntaxException, UnsupportedEncodingException, IOException {
        // http://www.adam-bien.com/roller/abien/entry/listing_directory_contents_with_jdk
        final Renderer renderer = new Renderer(createPath("layout.ftl"), createPath("post.ftl"));

        final String html = renderer.render(createPath("2014-05-30T21.29.20_This-is-the-First-Post.md"));

        assertThat(html, is(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <article>\n"
                + "    <h3>This is the First Post</h3>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
    }

    final class Renderer {

        private static final String ENCODING = "utf-8";

        private final FreeMarkerDown fmd = FreeMarkerDown.create();
        private final Layout outerTemplate;
        private final Layout innerTemplate;

        public Renderer(final Path outerTemplate, final Path  innerTemplate) throws IOException {
            super();
            this.outerTemplate = fmd.createLayout(outerTemplate, ENCODING, Options.WITHOUT_MARKDOWN);
            this.innerTemplate = fmd.createLayout(innerTemplate, ENCODING, Options.WITHOUT_MARKDOWN);
            this.outerTemplate.assignTemplateModel("content", this.innerTemplate);
        }

        String render(final Path content) throws IOException {
            innerTemplate.assignTemplateModel("content", fmd.createFragemnt(content, ENCODING));
            outerTemplate.assignVariable("name", "NAME");
            outerTemplate.assignVariable("description", "DESCRIPTION");

            final Map<String, String> keyValues = Maps.newHashMap();
            final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues);
            fmd.register(processor);

            return fmd.render(outerTemplate);
        }
    }

}

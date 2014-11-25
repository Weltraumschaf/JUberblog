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
import de.weltraumschaf.freemarkerdown.PreProcessor;
import de.weltraumschaf.freemarkerdown.PreProcessors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
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

    @Test
    public void testSomeMethod() throws URISyntaxException, UnsupportedEncodingException, IOException {
        final FreeMarkerDown fmd = FreeMarkerDown.create();
        final URI input = getClass().getResource("/de/weltraumschaf/juberblog/2014-05-30T21.29.20_This-is-the-First-Post.md").toURI();
        final Fragment content = fmd.createFragemnt(new String(Files.readAllBytes(Paths.get(input)), "utf-8"));

        final Map<String, String> keyValues = Maps.newHashMap();
        final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues);
        fmd.register(processor);

        assertThat(fmd.render(content), is(
                "<h2>This is the First Post</h2>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod "
                + "tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua at vero "
                + "eos et accusam et justo duo dolores et ea rebum stet clita kasd gubergren no sea "
                + "takimata sanctus est lorem ipsum dolor sit amet.</p>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod "
                + "tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua at vero "
                + "eos et accusam et justo duo dolores et ea rebum stet clita kasd gubergren no sea "
                + "takimata sanctus est lorem ipsum dolor sit amet.</p>"));
    }

}

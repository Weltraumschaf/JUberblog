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

import static de.weltraumschaf.juberblog.JUberblogTestCase.ENCODING;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;

/**
 * Tests for {@link Renderer}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class RendererTest extends JUberblogTestCase {

    @Test
    public void render() throws URISyntaxException, UnsupportedEncodingException, IOException {
        final Renderer renderer = new Renderer(createPath("layout.ftl"), createPath("post.ftl"), ENCODING);

        final Renderer.RendererResult result = renderer.render(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md"));

        assertThat(result.getRenderedContent(), is(
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
        assertThat(result.getMetaData().size(), is(1));
        assertThat(result.getMetaData(), hasEntry("Description", "This is the first post."));
    }

    @Test
    @Ignore("TODO Implement RendererTest#render_pathIsNull()")
    public void render_pathIsNull() {
    }

    @Test
    @Ignore("TODO Implement RendererTest#render_pathDoesNotExist()")
    public void render_pathDoesNotExist() {
    }

    @Test
    @Ignore("TODO Implement RendererTest#render_pathIsNotDirecotry()")
    public void render_pathIsNotDirecotry() {
    }

}

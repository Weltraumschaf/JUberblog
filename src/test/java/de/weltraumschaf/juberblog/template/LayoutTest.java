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

package de.weltraumschaf.juberblog.template;

import de.weltraumschaf.juberblog.Constants;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Layout}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LayoutTest {
    /**
     * Freemarker templates resource directory.
     */
    private static final String TEMPLATE_DIRECTORRY = Constants.PACKAGE_BASE.toString() + "/template";
    /**
     * Tested template.
     */
    private static final String TEMPLATE = "layout.ftl";

    @Test
    public void render_withContent() throws IOException, URISyntaxException, TemplateException {
        final Layout sut = new Layout(Configurations.forTests(TEMPLATE_DIRECTORRY), TEMPLATE);
        sut.setContent("foobar");
        assertThat(sut.render(), is(equalTo("\n\n\n<content>foobar</content>")));
        sut.setTitle("foo");
        sut.setEncoding("bar");
        sut.setDescription("baz");
        assertThat(sut.render(), is(equalTo("foo\nbar\nbaz\n<content>foobar</content>")));
    }

    @Test
    public void render_withContentObject() throws IOException, URISyntaxException, TemplateException {
        final Layout sut = new Layout(Configurations.forTests(TEMPLATE_DIRECTORRY), TEMPLATE);
        final Template content = new Template(Configurations.forTests(TEMPLATE_DIRECTORRY), "template.ftl");
        sut.setContent(content);
        assertThat(sut.render(), is(equalTo("\n\n\n<content>\n\n</content>")));
        sut.setTitle("foo");
        sut.setEncoding("bar");
        sut.setDescription("baz");
        assertThat(sut.render(), is(equalTo("foo\nbar\nbaz\n<content>foo\nbar\nbaz</content>")));
    }

    @Test
    public void render_withOutContent() throws IOException, URISyntaxException, TemplateException {
        final Layout sut = new Layout(Configurations.forTests(TEMPLATE_DIRECTORRY), TEMPLATE);
        assertThat(sut.render(), is(equalTo("\n\n\n<content></content>")));
        sut.setTitle("foo");
        sut.setEncoding("bar");
        sut.setDescription("baz");
        assertThat(sut.render(), is(equalTo("foo\nbar\nbaz\n<content></content>")));
    }

}

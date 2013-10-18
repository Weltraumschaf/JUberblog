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

import static de.weltraumschaf.juberblog.template.TemplateTest.configureTemplates;
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

    @Test
    public void render_withContent() throws IOException, URISyntaxException, TemplateException {
        final Layout sut = new Layout(configureTemplates(), "layout.ftl");
        sut.setContent("foobar");
        assertThat(sut.render(), is(equalTo("<content>foobar</content>")));
    }

    @Test
    public void render_withOutContent() throws IOException, URISyntaxException, TemplateException {
        final Layout sut = new Layout(configureTemplates(), "layout.ftl");
        assertThat(sut.render(), is(equalTo("<content></content>")));
    }

}

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
import de.weltraumschaf.juberblog.filter.Filter;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Template}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TemplateTest {

    /**
     * Freemarker templates resource directory.
     */
    private static final String TEMPLATE_DIRECTORRY = "/de/weltraumschaf/juberblog/template";
    private static final String TEMPLATE = "template.ftl";

    static Configuration configureTemplates() throws IOException, URISyntaxException {
        final Configuration cfg = new Configuration();
        final File templateDir = new File(TemplateTest.class.getResource(TEMPLATE_DIRECTORRY).toURI());
        cfg.setDirectoryForTemplateLoading(templateDir);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding(Constants.DEFAULT_ENCODING.toString());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setIncompatibleImprovements(Constants.FREEMARKER_VERSION);
        return cfg;
    }

    @Test
    public void render_withVariables() throws IOException, URISyntaxException, TemplateException {
        final Template sut = new Template(configureTemplates(), TEMPLATE);
        sut.assignVariable("title", "foo");
        sut.assignVariable("encoding", "bar");
        sut.assignVariable("description", "baz");
        assertThat(sut.render(), is(equalTo("foo\nbar\nbaz")));
    }

    @Test
    public void getVariable_emptyByDefault() throws IOException, URISyntaxException, TemplateException {
        final Template sut = new Template(configureTemplates(), TEMPLATE);
        assertThat(sut.getVariable("foo"), is(equalTo((Object) "")));
    }

    @Test
    public void addPostFilter() throws IOException, URISyntaxException, TemplateException {
        final Template sut = new Template(configureTemplates(), TEMPLATE);
        sut.assignVariable("title", "foo");
        sut.assignVariable("encoding", "bar");
        sut.assignVariable("description", "baz");
        sut.addPostFilter(new Filter() {

            @Override
            public String apply(final String input) {
                return input + "1";
            }
        });
        assertThat(sut.render(), is(equalTo("foo\nbar\nbaz1")));
    }
}

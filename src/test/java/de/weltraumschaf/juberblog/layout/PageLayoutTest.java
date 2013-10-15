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
package de.weltraumschaf.juberblog.layout;

import de.weltraumschaf.juberblog.Constants;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link PageLayout}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PageLayoutTest {

    /**
     * Freemarker templates resource directory.
     */
    private static final String TEMPLATE_DIRECTORRY = "/de/weltraumschaf/juberblog/layout";

    @Test
    public void renderLAyout() throws IOException, URISyntaxException, TemplateException {
        final PageLayout layout = new PageLayout(configureTemplates(), "layout.ftl");
        layout.setTitle("foo");
        layout.setDescription("bar");
        final InputStream htmlFile = getClass().getResourceAsStream(TEMPLATE_DIRECTORRY + "/layout.html");
        assertThat(layout.render("baz"), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

    private Configuration configureTemplates() throws IOException, URISyntaxException {
        final Configuration cfg = new Configuration();
        final File templateDir = new File(getClass().getResource(TEMPLATE_DIRECTORRY).toURI());
        cfg.setDirectoryForTemplateLoading(templateDir);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding(Constants.DEFAULT_ENCODING.toString());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setIncompatibleImprovements(Constants.FREEMARKER_VERSION);
        return cfg;
    }

}

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

import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.Constants;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;

/**
 * Factory to create template configurations.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Configurations {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Configurations.class);

    /**
     * Hidden for pure static factory.
     */
    private Configurations() {
        super();
    }

    /**
     * Creates a configuration with an exception handler which logs and rethrows exceptions.
     *
     * @return always new instance, never {@code null}
     */
    public static Configuration forTests()  {
        final Configuration cfg = configureTemplates();
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        return cfg;
    }

    /**
     * Creates a configuration with an exception handler which ignores template errors.
     *
     * @return always new instance, never {@code null}
     */
    public static Configuration forProduction()  {
        final Configuration cfg = configureTemplates();
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    /**
     * Creates a configuration with an exception handler which add template errors as HTML to output.
     *
     * @return always new instance, never {@code null}
     */
    public static Configuration forDrafts() {
        final Configuration cfg = configureTemplates();
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        return cfg;
    }

    /**
     * Configure templates and returns configuration.
     *
     * @param templateDirectory must not be {@code null} or empty
     * @return never {@code null}
     * @throws IOException if template directory can't be read
     * @throws URISyntaxException if template directory URI can't be created from class loader
     */
    private static Configuration configureTemplates() {
        final Configuration cfg = FreeMarkerDown.createConfiguration();
        cfg.setDefaultEncoding(Constants.DEFAULT_ENCODING.toString());
        return cfg;
    }

}

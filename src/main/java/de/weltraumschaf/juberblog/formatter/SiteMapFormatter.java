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

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.template.Template;
import de.weltraumschaf.juberblog.template.VarName;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;

/**
 * Formats a site map to a string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class SiteMapFormatter implements Formatter {

    /**
     * Template for XML.
     */
    private static final String TEMPLATE = "site_map.ftl";
    /**
     * used to render XML.
     */
    private final Template content;
    /**
     * The formatted site map.
     */
    private final SiteMap siteMap;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null}
     * @param siteMap must not be {@code null}
     * @throws IOException on any template I/O error
     */
    public SiteMapFormatter(final Configuration templateConfiguration, final SiteMap siteMap) throws IOException {
        super();
        Validate.notNull(templateConfiguration, "Template configuration must not be null!");
        Validate.notNull(siteMap, "SiteMap not be null!");
        content = new Template(templateConfiguration, TEMPLATE);
        this.siteMap = siteMap;
    }

    @Override
    public void setEncoding(String encoding) {
        content.assignVariable(VarName.ENCODING, encoding);
    }

    @Override
    public String format() throws IOException, TemplateException {
        content.assignVariable("urls", siteMap.getUrls());
        return content.render();
    }

}

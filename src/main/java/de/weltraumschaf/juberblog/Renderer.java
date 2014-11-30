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
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.Options;
import de.weltraumschaf.freemarkerdown.PreProcessor;
import de.weltraumschaf.freemarkerdown.PreProcessors;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Renderer {

    private final String encoding;

    private final FreeMarkerDown fmd = FreeMarkerDown.create();
    private final Layout outerTemplate;
    private final Layout innerTemplate;

    public Renderer(final Path outerTemplate, final Path innerTemplate, final String encoding) throws IOException {
        super();
        this.encoding = Validate.notEmpty(encoding, "encoding");
        this.outerTemplate = fmd.createLayout(outerTemplate, encoding, Options.WITHOUT_MARKDOWN);
        this.innerTemplate = fmd.createLayout(innerTemplate, encoding, Options.WITHOUT_MARKDOWN);
        this.outerTemplate.assignTemplateModel("content", this.innerTemplate);
    }

    String render(final Path content) throws IOException {
        innerTemplate.assignTemplateModel("content", fmd.createFragemnt(content, encoding));
        outerTemplate.assignVariable("name", "NAME");
        outerTemplate.assignVariable("description", "DESCRIPTION");

        final Map<String, String> keyValues = Maps.newHashMap();
        final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues);
        fmd.register(processor);

        return fmd.render(outerTemplate);
    }
}

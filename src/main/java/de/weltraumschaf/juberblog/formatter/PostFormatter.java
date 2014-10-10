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

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.template.Template;
import de.weltraumschaf.juberblog.template.VarName;
import freemarker.template.Configuration;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Formats a blog post.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class PostFormatter extends BaseFormatter {

    /**
     * Template variable for feature switching.
     */
    private static final Map<String, Boolean> FEATURES = Maps.newHashMap();

    static {
        FEATURES.put("rating", Boolean.FALSE);
        FEATURES.put("comments", Boolean.FALSE);
    }

    /**
     * Global available variable names.
     */
    private static final Set<VarName> GLOBAL_VARIABLE_NAMES = new HashSet<>(Arrays.asList(
            VarName.DATE_FORMATTED, VarName.PREV_POST, VarName.NEXT_POST));
    /**
     * Post template file name.
     */
    private static final String TEMPLATE = "post.ftl";

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null} or empty
     * @param markdown must not be {@code null}
     * @throws IOException if template file can't be read
     */
    public PostFormatter(final Configuration templateConfiguration, final String markdown) throws IOException {
        super(templateConfiguration, TEMPLATE, markdown);
        content.assignVariable("features", FEATURES);
        Template.initializeVaribales(content, GLOBAL_VARIABLE_NAMES);
    }

    /**
     * Set the date when post was formatted.
     *
     * @param date must not be {@code null}
     */
    public void setDateFormatted(final String date) {
        Validate.notNull(date, "Date must not be null");
        content.assignVariable(VarName.DATE_FORMATTED, date);
    }

    /**
     * Set the previous post URI.
     *
     * @param prevPost must not be {@code null}
     */
    public void setPrevPost(final String prevPost) {
        Validate.notNull(prevPost, "Prev post must not be null");
        content.assignVariable(VarName.PREV_POST, prevPost);
    }

    /**
     * Set the next post URI.
     *
     * @param nextPost must not be {@code null}
     */
    public void setNextPost(final String nextPost) {
        Validate.notNull(nextPost, "Next post must not be null");
        content.assignVariable(VarName.NEXT_POST, nextPost);
    }

}

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

import com.google.common.collect.Maps;
import de.weltraumschaf.juberblog.template.Template;
import freemarker.template.Configuration;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Formats a blog post.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PostFormatter extends BaseFormatter {

    private static final Map<String, Boolean> FEATURES = Maps.newHashMap();

    static {
        FEATURES.put("rating", Boolean.FALSE);
        FEATURES.put("comments", Boolean.FALSE);
    }
    private static final String DATE_FORMATTED = "date_formatted";
    private static final String PREV_POST = "prevPost";
    private static final String NEXT_POST = "nextPost";
    private static final List<String> GLOBAL_VARIABLE_NAMES = Arrays.asList(DATE_FORMATTED, PREV_POST, NEXT_POST);
    /**
     * Post template file name.
     */
    private static final String TEMPLATE = "post.ftl";

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null} or empty
     * @throws IOException if template file can't be read
     */
    public PostFormatter(final Configuration templateConfiguration) throws IOException {
        super(templateConfiguration, TEMPLATE);
        content.assignVariable("features", FEATURES);
        Template.initializeVaribales(content, GLOBAL_VARIABLE_NAMES);
    }

    public void setDateFormatted(final String date) {
        content.assignVariable(DATE_FORMATTED, date);
    }

    public void setPrevPost(final String prevPost) {
        content.assignVariable(PREV_POST, prevPost);
    }

    public void setNextPost(final String nextPost) {
        content.assignVariable(NEXT_POST, nextPost);
    }

}

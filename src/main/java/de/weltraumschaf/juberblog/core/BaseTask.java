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

package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Page.Pages;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Common functionality for all tasks.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 * @param <R> type of return value
 * @param <P> type of previous result
 */
public abstract class BaseTask<R, P> implements Task<R, P> {

    /**
     * Token type class of result from previous task.
     */
    private final Class<P> typeForPreviusResult;

    /**
     * Dedicated constructor.
     *
     * @param typeForPreviusResult must not be {@code null}
     */
    public BaseTask(final Class<P> typeForPreviusResult) {
        super();
        this.typeForPreviusResult = Validate.notNull(typeForPreviusResult, "typeForPreviusResult");
    }

    @Override
    public final Class<P> getDesiredTypeForPreviusResult() {
        return typeForPreviusResult;
    }

    /**
     * Encapsulates unsafe cast into single location to minimize impact of suppressing the warning.
     *
     * @param <T> generic type
     * @param aClass may be {@code null}
     * @return may be {@code null}
     */
    @SuppressWarnings("unchecked")
    static final <T> Class<T> castClass(final Class<?> aClass) {
        return (Class<T>) aClass;
    }

    /**
     * Convert collection of pages into plain java collections for assigning them to the templates.
     *
     * @param pages must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    protected final Collection<Map<String, String>> convert(final Pages pages) {
        final Collection<Map<String, String>> items = Lists.newArrayList();

        for (final Page page : pages) {
            items.add(convert(page));
        }

        return Collections.unmodifiableCollection(items);
    }

    /**
     * Converts a single page into plain java collections for assigning them to the templates.
     *
     * @param page must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    protected Map<String, String> convert(final Page page) {
        return Collections.emptyMap();
    }
}

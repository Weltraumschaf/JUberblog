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

package de.weltraumschaf.juberblog.filter;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import java.util.List;

/**
 * Chains single filters.
 *
 * Filters will be applied on input in same order like tey were added.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FilterChain implements Filter {

    /**
     * Holds added filters.
     */
    private final List<Filter> filters = Lists.newArrayList();

    /**
     * Add an filter.
     *
     * Filters may be added multiple times.
     *
     * @param filter must not be {@code null}
     */
    public void add(final Filter filter) {
        Validate.notNull(filter, "Filter must not be null!");
        filters.add(filter);
    }

    /**
     * {@inheritDoc}
     *
     * @param input must not be {@code null}
     * @return same as input if no filters added
     */
    @Override
    public String apply(final String input) {
        Validate.notNull(input, "Input must not be null!");
        String processed = input;

        for (final Filter filter : filters) {
            processed = filter.apply(processed);
        }

        return processed;
    }

}

package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Convenience type to collect pages.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Pages extends ArrayList<Page> {

    /**
     * Default constructor.
     */
    public Pages() {
        super();
    }

    /**
     * Convert collection of pages into plain java collections for assigning them to the templates.
     *
     * @param converter must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    public Collection<Map<String, String>> convert(final PageConverter converter) {
        Validate.notNull(converter, "converter");
        final Collection<Map<String, String>> items = Lists.newArrayList();

        for (final Page page : this) {
            items.add(converter.convert(page));
        }

        return Collections.unmodifiableCollection(items);
    }

}

package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.juberblog.content.Page;

import java.util.Map;

/**
 * Converts a single page to a map of strings assignable to templates.
 *
 * XXX Move to comtent package.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface PageConverter {

    /**
     * Converts a single page object.
     *
     * @param page must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    Map<String, Object> convert(Page page);
}

package de.weltraumschaf.juberblog.core;

import java.util.Map;

/**
 * Converts a single page to a map of strings assignable to templates.
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
    Map<String, String> convert(Page page);
}

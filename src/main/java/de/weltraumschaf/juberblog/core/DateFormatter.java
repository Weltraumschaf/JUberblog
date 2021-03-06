package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Formats Joda time objects to various predefined formats.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class DateFormatter {

    /**
     * Hidden for pure static utility class.
     */
    private DateFormatter() {
        super();
    }

    /**
     * Formats the given date with a given format.
     *
     * @param time must not be {@code null}
     * @param format must not be {@code null}
     * @return never {@code null}
     */
    public static String format(final DateTime time, final Format format) {
        return Validate.notNull(time, "time")
                .toString(Validate.notNull(format, "format").getFormatter());
    }

    /**
     * Predefined format patterns.
     *
     * See http://www.joda.org/joda-time/key_format.html
     */
    public enum Format {

        /**
         * Publishing date format for RSS files.
         */
        RSS_PUBLISH_DATE_FORMAT("EEE, dd MMM yyyy HH:mm:ss Z"),
        /**
         * DC publishing date format.
         *
         * http://www.w3.org/TR/NOTE-datetime
         */
        W3C_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ssZZ");
        /**
         * USed to format date time.
         */
        private final DateTimeFormatter formatter;

        /**
         * Dedicated constructor.
         *
         * @param pattern must not be {@code nul} or empty
         */
        Format(final String pattern) {
            this.formatter = DateTimeFormat.forPattern(Validate.notEmpty(pattern, "pattern"));
        }

        /**
         * Get the formatter.
         *
         * @return never {@code null}
         */
        private DateTimeFormatter getFormatter() {
            return formatter;
        }
    }
}

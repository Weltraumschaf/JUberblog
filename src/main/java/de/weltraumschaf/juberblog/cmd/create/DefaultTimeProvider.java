package de.weltraumschaf.juberblog.cmd.create;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Joda Time based default implementation.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class DefaultTimeProvider implements TimeProvider {

    /**
     * Length of the year in string representation.
     */
    private static final int LENGTH_YEAR = 4;
    /**
     * Length of all other time pats in string representation.
     */
    private static final int LENGTH_OTHERS = 2;
    /**
     * Formatter for {@literal yyyy-mm-ddThh.mm.ss}.
     */
    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendYear(LENGTH_YEAR, LENGTH_YEAR)
                .appendLiteral('-')
                .appendMonthOfYear(LENGTH_OTHERS)
                .appendLiteral('-')
                .appendDayOfMonth(LENGTH_OTHERS)
                .appendLiteral('T')
                .appendHourOfDay(LENGTH_OTHERS)
                .appendLiteral('.')
                .appendMinuteOfHour(LENGTH_OTHERS)
                .appendLiteral('.')
                .appendSecondOfMinute(LENGTH_OTHERS)
                .toFormatter();

    @Override
    public String nowAsString() {
        return new DateTime().toString(formatter);
    }

}

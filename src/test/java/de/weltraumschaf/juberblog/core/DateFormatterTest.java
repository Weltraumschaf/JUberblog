package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.juberblog.core.DateFormatter.Format;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link DateFormatter}.
 *
 * @author Sven Strittmatter
 */
public class DateFormatterTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void format_throwsExceptionIfDateIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'time'");

        DateFormatter.format(null, Format.RSS_PUBLISH_DATE_FORMAT);
    }

    @Test
    public void format_throwsExceptionIfFormatIsNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'format'");

        DateFormatter.format(new DateTime(), null);
    }

    @Test
    public void format() {
        assertThat(
                DateFormatter.format(new DateTime(123456789L), Format.RSS_PUBLISH_DATE_FORMAT),
                is("Fri, 02 Jan 1970 11:17:36 +0100"));
    }

}

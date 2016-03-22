package de.weltraumschaf.juberblog.core;

import java.net.URI;
import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Page}.
 *
 * @author Sven Strittmatter
 */
public class PageTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Page.class).verify();
    }

    @Test
    public void toStringContainsAllProperties() {
        final Page sut = new Page(
                "title",
                URI.create("http://juberblog.local"),
                "desc",
                new DateTime(0L),
                Page.Type.POST);

        assertThat(
                sut.toString(),
                is("Page{"
                        + "title=title, "
                        + "link=http://juberblog.local, "
                        + "description=desc, "
                        + "publishingDate=1970-01-01T01:00:00.000+01:00, "
                        + "type=POST}"));
    }

}

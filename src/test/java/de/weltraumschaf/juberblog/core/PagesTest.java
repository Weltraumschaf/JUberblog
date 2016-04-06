package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.guava.Maps;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link Pages}.
 */
public class PagesTest {

    private final Pages sut = new Pages();

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Pages.class).verify();
    }

    @Test(expected = NullPointerException.class)
    public void convert_pages_nullGiven() {
        assertThat(sut.convert(null).size(), is(0));
    }

    @Test
    public void convert_pages_emptyGiven() {
        assertThat(sut.convert(new Converter()).size(), is(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void convert_pages_returnUnmodifiable() {
        sut.convert(new Converter()).add(mock(Map.class));
    }
    @Test
    public void convert_pages() {
        sut.add(new Page("title one", URI.create("/foo"), "", new DateTime(), PageType.POST));
        sut.add(new Page("title two", URI.create("/bar"), "", new DateTime(), PageType.POST));

        Collection<Map<String, String>> result = sut.convert(new Converter());

        assertThat(result, hasSize(2));
        assertThat(new ArrayList<>(result).get(0).size(), is(1));
        assertThat(new ArrayList<>(result).get(0), hasEntry("title", "title one"));
        assertThat(new ArrayList<>(result).get(1).size(), is(1));
        assertThat(new ArrayList<>(result).get(1), hasEntry("title", "title two"));
    }

    private static class Converter implements PageConverter {
        @Override
        public Map<String, String> convert(final Page page) {
            final Map<String, String> result = Maps.newHashMap();
            result.put("title", page.getTitle());
            return result;
        }
    }
}
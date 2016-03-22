package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.juberblog.core.Page.Pages;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseTask}.
 *
 * @author Sven Strittmatter
 */
public class BaseTaskTest {

    private final BaseTask sut = new BaseTaskStub();

    @Test(expected = NullPointerException.class)
    public void construct_withNullType() {
        new BaseTaskStub(null);
    }

    @Test
    public void getDesiredTypeForPreviusResult() {
        assertThat(sut.getDesiredTypeForPreviusResult().getSimpleName(), is("Void"));
    }

    @Test(expected = NullPointerException.class)
    public void convert_pages_nullGiven() {
        assertThat(sut.convert((Pages) null).size(), is(0));
    }

    @Test
    public void convert_pages_emptyGiven() {
        assertThat(sut.convert(new Pages()).size(), is(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void convert_pages_returnUnmodifiable() {
        sut.convert(new Pages()).add(mock(Map.class));
    }
    @Test
    public void convert_pages() {
        final Pages pages = new Pages();
        pages.add(new Page("title one", URI.create("/foo"), "", new DateTime(), Page.Type.POST));
        pages.add(new Page("title two", URI.create("/bar"), "", new DateTime(), Page.Type.POST));

        Collection<Map<String, String>> result = new BaseTaskWithConvertStub().convert(pages);

        assertThat(result, hasSize(2));
        assertThat(new ArrayList<>(result).get(0).size(), is(1));
        assertThat(new ArrayList<>(result).get(0), hasEntry("title", "title one"));
        assertThat(new ArrayList<>(result).get(1).size(), is(1));
        assertThat(new ArrayList<>(result).get(1), hasEntry("title", "title two"));
    }

    @Test
    public void convert_page_defaultImplementation() {
        assertThat(sut.convert((Page) null).size(), is(0));
    }

    private static class BaseTaskStub extends BaseTask<Void, Void> {

        public BaseTaskStub() {
            this(Void.class);
        }

        public BaseTaskStub(final Class<Void> typeForPreviusResult) {
            super(typeForPreviusResult);
        }

        @Override
        public Void execute() throws Exception {
            return null;
        }

        @Override
        public Void execute(Void previusResult) throws Exception {
            return null;
        }

    }

    private static class BaseTaskWithConvertStub extends BaseTaskStub {
        protected Map<String, String> convert(final Page page) {
            final Map<String, String> result = Maps.newHashMap();
            result.put("title", page.getTitle());
            return result;
        }
    }
}

package de.weltraumschaf.juberblog.core;

import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Uris}.
 *
 * @author Sven Strittmatter
 */
public class UrisTest {

    private final Uris sut = new Uris("http://www.foobar.com/");

    public UrisTest() throws URISyntaxException {
        super();
    }

    @Test
    public void uris_asString() {
        assertThat(sut.posts().toString(), is(equalTo("http://www.foobar.com/posts")));
        assertThat(sut.sites().toString(), is(equalTo("http://www.foobar.com/sites")));
        assertThat(sut.drafts().toString(), is(equalTo("http://www.foobar.com/drafts")));
        assertThat(sut.draftPosts().toString(), is(equalTo("http://www.foobar.com/drafts/posts")));
        assertThat(sut.draftSites().toString(), is(equalTo("http://www.foobar.com/drafts/sites")));
    }

    @Test
    public void uris_asAsciiString() {
        assertThat(sut.posts().toASCIIString(), is(equalTo("http://www.foobar.com/posts")));
        assertThat(sut.sites().toASCIIString(), is(equalTo("http://www.foobar.com/sites")));
        assertThat(sut.drafts().toASCIIString(), is(equalTo("http://www.foobar.com/drafts")));
        assertThat(sut.draftPosts().toASCIIString(), is(equalTo("http://www.foobar.com/drafts/posts")));
        assertThat(sut.draftSites().toASCIIString(), is(equalTo("http://www.foobar.com/drafts/sites")));
    }

    @Test
    public void post() {
        assertThat(sut.post("foo.html").toString(), is(equalTo("http://www.foobar.com/posts/foo.html")));
    }

    @Test
    public void site() {
        assertThat(sut.site("foo.html").toString(), is(equalTo("http://www.foobar.com/sites/foo.html")));
    }

}

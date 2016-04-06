package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link Renderer}.
 *
 * @author Sven Strittmatter
 */
public class RendererTest extends BaseTestCase {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    final Renderer sut = new Renderer(
            createPath(SCAFOLD_PACKAGE_PREFIX + "layout.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "post.ftl"),
            ENCODING);

    public RendererTest() throws IOException, URISyntaxException {
        super();
    }

    @Test
    public void render() throws URISyntaxException, UnsupportedEncodingException, IOException {
        final Renderer.RendererResult result = sut.render(
                createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md"));

        assertThat(result.getRenderedContent(), is(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <article>\n"
                + "    <h3>This is the First Post</h3>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
        assertThat(result.getMarkdown(), is(
                "\n"
                + "\n"
                + "### This is the First Post\n"
                + "\n"
                + "Lorem ipsum  dolor sit amet consetetur  sadipscing elitr sed diam  nonumy eirmod\n"
                + "tempor invidunt ut labore et dolore magna aliquyam.\n"
                + "\n"
                + "Lorem ipsum  dolor sit amet consetetur  sadipscing elitr sed diam  nonumy eirmod\n"
                + "tempor invidunt ut labore et dolore magna aliquyam.\n"
                + "\n"));
        assertThat(result.getMetaData().size(), is(1));
        assertThat(result.getMetaData(), hasEntry("Description", "This is the first post."));
    }

    @Test
    public void render_pathIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'content'");

        sut.render(null);
    }

    @Test
    public void render_pathDoesNotExist() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Given path '/foo/ba/baz' does not exist!");

        sut.render(Paths.get("/foo/ba/baz"));
    }

    @Test
    public void render_pathIsDirecotry() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("is a directory");

        sut.render(tmp.getRoot().toPath());
    }

}

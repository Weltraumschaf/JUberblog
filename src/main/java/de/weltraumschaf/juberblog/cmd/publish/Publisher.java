package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.core.Page.SortByDateAscending;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Uris;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Publish pages from given data files into a given directory.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Publisher {

    /**
     * USed to extract headline.
     */
    private final Headline headline = new Headline();
    /**
     * Where to find data files.
     */
    private final Path inputDir;
    /**
     * Where to store published pages as file.
     */
    private final Path outputDir;
    /**
     * Used encoding for IO.
     */
    private final String encoding;
    /**
     * Base URL of published pages.
     */
    private final URI baseUrlForPages;
    /**
     * Renders the page (Markdown/templates).
     */
    private final Renderer renderer;
    /**
     * PageType of published pages.
     */
    private final PageType type;

    /**
     * Dedicated constructor.
     *
     * @param templates must not be {@code null}
     * @param directories must not be {@code null}
     * @param configuration must not be {@code null}
     * @param type must not be {@code null}
     * @throws IOException if any template can't be read
     */
    public Publisher(
            final Templates templates,
            final Directories directories,
            final Configuration configuration,
            final PageType type) throws IOException {
        super();
        Validate.notNull(templates, "templates");
        Validate.notNull(directories, "directories");
        Validate.notNull(configuration, "configuration");
        this.encoding = configuration.getEncoding();
        this.type = Validate.notNull(type, "type");
        this.baseUrlForPages = configuration.getBaseUri();

        if (type == PageType.POST) {
            this.inputDir = directories.getPostsData();
            this.outputDir = directories.getPostsOutput();
            this.renderer = new Renderer(
                    templates.getLayoutTemplate(),
                    templates.getPostTemplate(),
                    encoding
            );
        } else if (type == PageType.SITE) {
            this.inputDir = directories.getSitesData();
            this.outputDir = directories.getSitesOutput();
            this.renderer = new Renderer(
                    templates.getLayoutTemplate(),
                    templates.getSiteTemplate(),
                    encoding
            );
        } else {
            throw new IllegalArgumentException(String.format("Bad type '%s'!", type));
        }
    }

    /**
     * Runs the publishing.
     * <p>
     * The returned collection contains a meta data object for each published page.
     * </p>
     *
     * @return never {@code null}, unmodifiable
     * @throws IOException on any file IO error
     */
    public Pages publish() throws IOException {
        final Uris uris = new Uris(baseUrlForPages);
        final Pages publishedPages = new Pages();

        for (final DataFile foundData : FilesFinderByExtension.MARKDOWN.find(inputDir)) {
            final Renderer.RendererResult result = renderer.render(foundData.getPath());
            final String outputBaseName = foundData.getBareName() + FileNameExtension.HTML.getExtension();

            Files.write(outputDir.resolve(outputBaseName), result.getRenderedContent().getBytes(encoding));

            // XXX: Check if present, extract etc.
            final Map<String, String> metaData = result.getMetaData();
            final String description = metaData.containsKey("Description")
                    ? metaData.get("Description")
                    : ""; // TODO Extract excerpt from Markdown.

            final URI pageUrl;

            switch (type) {
                case SITE:
                    pageUrl = uris.site(outputBaseName);
                    break;
                case POST:
                    pageUrl = uris.post(outputBaseName);
                    break;
                default:
                    throw new IllegalStateException(String.format("Unsupported page type '%s'!", type));
            }
            // XXX: Emit errors if something of this is not available.
            publishedPages.add(new Page(
                    headline.find(result.getMarkdown()),
                    pageUrl,
                    description,
                    foundData.getCreationDate(),
                    type));
        }

        Collections.sort(publishedPages, new SortByDateAscending());
        return publishedPages;
    }

}

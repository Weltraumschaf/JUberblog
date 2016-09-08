package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.content.Page;
import de.weltraumschaf.juberblog.content.Pages;
import de.weltraumschaf.juberblog.content.Page.SortByDateAscending;
import de.weltraumschaf.juberblog.content.PageType;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Uris;
import de.weltraumschaf.juberblog.core.Verbose;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Publish pages from given data files into a given directory.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class Publisher {

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
    private final Verbose verbose;

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
        final BlogConfiguration configuration,
        final PageType type,
        final Version version,
        final Verbose verbose) throws IOException {
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
                configuration,
                version
            );
        } else if (type == PageType.SITE) {
            this.inputDir = directories.getSitesData();
            this.outputDir = directories.getSitesOutput();
            this.renderer = new Renderer(
                templates.getLayoutTemplate(),
                templates.getSiteTemplate(),
                configuration,
                version
            );
        } else {
            throw new IllegalArgumentException(String.format("Bad type '%s'!", type));
        }

        this.verbose = verbose;
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
            verbose.print("Publish file '%s'...", foundData.getPath());
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

        publishedPages.sort(new SortByDateAscending());
        return publishedPages;
    }

}

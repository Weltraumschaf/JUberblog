package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Interceptor;
import de.weltraumschaf.freemarkerdown.Interceptor.ExecutionPoint;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.freemarkerdown.PreProcessor;
import de.weltraumschaf.freemarkerdown.PreProcessors;
import de.weltraumschaf.freemarkerdown.TemplateModel;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Orchestrates the whole two step template and preprocessing stuff to render a post or site.
 *
 * <p>
 * Structure of template fragments:
 * </p>
 * <pre>
 * +-------------------+
 * |   outerTemplate   |
 * | +---------------+ |
 * | | innerTemplate | |
 * | |  +---------+  | |
 * | |  | content |  | |
 * | |  +---------+  | |
 * | +---------------+ |
 * +-------------------+
 * </pre>
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class Renderer {

    private static final String TPL_NAME_OUTER = "outerTemplate";
    private static final String TPL_NAME_INNER = "innerTemplate";
    private static final String TPL_NAME_CONTENT = "content";

    /**
     * Encoding used to transfer byte[] and string.
     */
    private final BlogConfiguration configuration;
    /**
     * Used to render the templates.
     *
     * XXX: Inject one instance from main app.
     */
    private final FreeMarkerDown fmd;
    /**
     * Used to collect key values from pre processor.
     */
    private final Map<String, String> keyValues = Maps.newHashMap();
    /**
     * Used to pre process key value instructions.
     */
    private final PreProcessor processor = PreProcessors.createKeyValueProcessor(keyValues, "juberblog");
    /**
     * Used to extract raw Markdown.
     */
    private final GetUnprocessedMarkdown interceptor = new GetUnprocessedMarkdown();
    /**
     * Outer part of the two step layout.
     */
    private final Layout layout;
    /**
     * Inner part of the two step layout.
     */
    private final Layout content;
    private final Version version;

    /**
     * Dedicated constructor.
     *
     * @param outerTemplate must not be {@code null}
     * @param innerTemplate must not be {@code null}
     * @param encoding must not be {@code null} or empty
     * @throws IOException if templates can't be read
     */
    public Renderer(final Path outerTemplate, final Path innerTemplate, final BlogConfiguration configuration, final Version version) throws IOException {
        super();
        this.configuration = Validate.notNull(configuration, "configuration");
        this.version = Validate.notNull(version, "version");
        this.fmd = FreeMarkerDown.create(configuration.getEncoding());
        this.layout = fmd.createLayout(outerTemplate, TPL_NAME_OUTER, RenderOptions.WITHOUT_MARKDOWN);
        this.content = fmd.createLayout(innerTemplate, TPL_NAME_INNER, RenderOptions.WITHOUT_MARKDOWN);
        this.layout.assignTemplateModel(TPL_NAME_CONTENT, this.content);
        fmd.register(interceptor, ExecutionPoint.BEFORE_RENDERING);
    }

    /**
     * Renders the given content fragment at given file location.
     * <p>
     * Throws {@link IllegalArgumentException} if given path does not exist or is a directory.
     * </p>
     *
     * @param contentFile must not be {@code null}
     * @return never {@code null}
     * @throws IOException if content file can't be read
     */
    RendererResult render(final Path contentFile) throws IOException {
        Validate.notNull(contentFile, "content");

        if (!Files.exists(contentFile)) {
            throw new IllegalArgumentException(String.format("Given path '%s' does not exist!", contentFile));
        }

        if (Files.isDirectory(contentFile)) {
            throw new IllegalArgumentException(String.format("Given path '%s' is a directory!", contentFile));
        }

        keyValues.clear();
        content.assignTemplateModel(TPL_NAME_CONTENT, fmd.createFragemnt(contentFile, TPL_NAME_CONTENT));
        layout.assignVariable(TemplateVariables.BLOG_DESCRIPTION, configuration.getDescription());
        layout.assignVariable(TemplateVariables.BLOG_TITLE, configuration.getTitle());
        layout.assignVariable(TemplateVariables.BASE_URL, configuration.getBaseUri());
        layout.assignVariable(TemplateVariables.BLOG_VERSION, version.getVersion());
        layout.assignVariable(TemplateVariables.LANGUAGE, configuration.getLanguage());
        layout.assignVariable(TemplateVariables.ENCODING, configuration.getEncoding());
        // FIXME Assign all key values as is to the template.
        layout.assignVariable(TemplateVariables.DESCRIPTION, ""); // FIXME Add from key values.
        layout.assignVariable(TemplateVariables.KEYWORDS, ""); // FIXME Add from key values.
        layout.assignVariable(TemplateVariables.NAVIGATION, ""); // FIXME Add from key values.
        fmd.register(processor);

        return new RendererResult(fmd.render(layout), interceptor.getMarkdown(), keyValues);
    }

    /**
     * Provides the rendering result.
     * <p>
     * This object is necessary to return multiple values at one time.
     * </p>
     */
    static final class RendererResult {

        /**
         * The rendered content (usually HTML).
         */
        private final String renderedContent;
        /**
         * Raw Markdown from rendered file.
         */
        private final String markdown;
        /**
         * Meta data found by pre processors of FreeMarkerDown.
         */
        private final Map<String, String> metaData;

        /**
         * Dedicated constructor.
         *
         * @param renderedContent must not be {@code null}
         * @param markdown must not be {@code null}
         * @param metaData must not be {@code null}, defensive copied
         */
        private RendererResult(
                final String renderedContent,
                final String markdown,
                final Map<String, String> metaData) {
            super();
            this.renderedContent = Validate.notNull(renderedContent, "renderedContent");
            this.markdown = Validate.notNull(markdown, "markdown");
            this.metaData = Maps.newHashMap(Validate.notNull(metaData, "metaData"));
        }

        /**
         * Get the rendered content.
         *
         * @return never {@code null}
         */
        String getRenderedContent() {
            return renderedContent;
        }

        /**
         * Get the Markdown from rendered data file.
         *
         * @return never {@code null}
         */
        String getMarkdown() {
            return markdown;
        }

        /**
         * Get found meta data.
         *
         * @return never {@code null}, unmodifiable
         */
        Map<String, String> getMetaData() {
            return Collections.unmodifiableMap(metaData);
        }

    }

    /**
     * Intercepts the rendering to collected the raw Markdown.
     */
    private static final class GetUnprocessedMarkdown implements Interceptor {

        /**
         * Collected Markdown.
         */
        private String markdown = "";

        @Override
        public void intercept(final ExecutionPoint point, final TemplateModel template, final String content) {
            final String name = template.getName();

            if (TPL_NAME_CONTENT.equals(name)) {
                markdown = content;
            }
        }

        /**
         * Returns the raw Markdown.
         *
         * @return never {@code null}
         */
        private String getMarkdown() {
            return markdown;
        }

    }
}

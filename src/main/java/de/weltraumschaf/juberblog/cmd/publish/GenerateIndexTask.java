package de.weltraumschaf.juberblog.cmd.publish;

import com.beust.jcommander.internal.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.PageConverter;
import de.weltraumschaf.juberblog.core.Task;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Verbose;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Task to generate the index site.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public class GenerateIndexTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    /**
     * Task configuration.
     */
    private final Config config;

    /**
     * Dedicated constructor.
     *
     * @param config must not be {@code null}
     * @param verbose must not be {@code null}
     */
    public GenerateIndexTask(final Config config, final Verbose verbose) {
        super(Pages.class, verbose);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(Pages previusResult) throws Exception {
        println("Generate index...");
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment content = fmd.createFragemnt(
            config.indexTemplate,
            config.encoding,
            config.indexTemplate.toString(),
            RenderOptions.WITHOUT_MARKDOWN);
        final Layout layout = fmd.createLayout(
            config.layoutTemplate,
            config.encoding,
            config.layoutTemplate.toString(),
            RenderOptions.WITHOUT_MARKDOWN);
        layout.assignTemplateModel(TemplateVariables.CONTENT, content);
        layout.assignVariable(TemplateVariables.KEYWORDS, "");
        layout.assignVariable(TemplateVariables.LANGUAGE, config.blog.getLanguage());
        layout.assignVariable(TemplateVariables.ENCODING, config.blog.getEncoding());
        layout.assignVariable(TemplateVariables.BLOG_TITLE, config.blog.getTitle());
        layout.assignVariable(TemplateVariables.BLOG_DESCRIPTION, config.blog.getDescription());
        layout.assignVariable(TemplateVariables.DESCRIPTION, config.blog.getDescription());
        layout.assignVariable(TemplateVariables.BASE_URL, config.blog.getBaseUri());
        content.assignVariable(TemplateVariables.POSTS, previusResult.convert(new ForIndexConverter()));
        Files.write(
            config.outputDir.resolve("index" + FileNameExtension.HTML.getExtension()),
            fmd.render(layout).getBytes(config.encoding)
        );

        return previusResult;
    }

    private static final class ForIndexConverter implements PageConverter {

        @Override
        public Map<String, Object> convert(final Page page) {
            Validate.notNull(page, "page");
            final Map<String, Object> item = Maps.newHashMap();
            item.put("title", page.getTitle());
            item.put("link", page.getLink().toString());
            item.put("description", page.getDescription());
            item.put("pubDate", page.getPublishingDate().toDate());
            return Collections.unmodifiableMap(item);
        }
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * Used to read/write files and as the encoding in the HTML.
         */
        private final String encoding;
        /**
         * Where to store {@literal index.html}.
         */
        private final Path outputDir;
        /**
         * The outer template of the {@literal index.html).
         */
        private final Path layoutTemplate;
        /**
         * The inner template of the {@literal index.html).
         */
        private final Path indexTemplate;
        private final BlogConfiguration blog;

        /**
         * Dedicated constructor.
         *
         * @param templates must not be {@code null}
         * @param directories must not be {@code null}
         * @param configuration must not be {@code null}
         */
        public Config(
            final Templates templates,
            final Directories directories,
            final BlogConfiguration configuration) {
            super();
            Validate.notNull(templates, "templates");
            Validate.notNull(directories, "directories");
            Validate.notNull(configuration, "configuration");
            this.encoding = configuration.getEncoding();
            this.outputDir = directories.getOutput();
            this.layoutTemplate = templates.getLayoutTemplate();
            this.indexTemplate = templates.getIndexTemplate();
            this.blog = configuration;
        }

    }
}

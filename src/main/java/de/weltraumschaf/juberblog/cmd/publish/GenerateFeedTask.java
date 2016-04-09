package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.DateFormatter.Format;
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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Generates the RSS feed.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class GenerateFeedTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

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
    public GenerateFeedTask(final Config config, final Verbose verbose) {
        super(Pages.class, verbose);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(final Pages previusResult) throws Exception {
        println("Generate feed...");
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment template = fmd.createFragemnt(
            config.template,
            config.encoding,
            config.template.toString(),
            RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable(TemplateVariables.ENCODING, config.encoding);
        template.assignVariable(TemplateVariables.BLOG_TITLE, config.title);
        template.assignVariable(TemplateVariables.LINK, config.link.toString());
        template.assignVariable(TemplateVariables.BLOG_DESCRIPTION, config.description);
        template.assignVariable(TemplateVariables.LANGUAGE, config.language);
        template.assignVariable(
            TemplateVariables.LAST_BUILD_DATE,
            DateFormatter.format(config.lastBuildDate, Format.RSS_PUBLISH_DATE_FORMAT));
        template.assignVariable(TemplateVariables.ITEMS, previusResult.convert(new ForFeedConverter()));

        Files.write(
            config.outputDir.resolve("feed" + FileNameExtension.XML.getExtension()),
            fmd.render(template).getBytes(config.encoding)
        );

        return previusResult;
    }

    private static final class ForFeedConverter implements PageConverter {

        @Override
        public Map<String, Object> convert(final Page page) {
            final Map<String, Object> item = Maps.newHashMap();
            item.put(TemplateVariables.TITLE.getVariableName(), page.getTitle());
            item.put(TemplateVariables.LINK.getVariableName(), page.getLink().toString());
            item.put(TemplateVariables.DESCRIPTION.getVariableName(), page.getDescription());
            item.put(
                TemplateVariables.PUB_DATE.getVariableName(),
                DateFormatter.format(page.getPublishingDate(), Format.RSS_PUBLISH_DATE_FORMAT));
            item.put(
                TemplateVariables.DC_DATE.getVariableName(),
                DateFormatter.format(page.getPublishingDate(), Format.W3C_DATE_FORMAT));
            return Collections.unmodifiableMap(item);
        }
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * The template of the XML.
         */
        private final Path template;
        /**
         * Where to store the XML.
         */
        private final Path outputDir;
        /**
         * Encoding to read/write files and for XML.
         */
        private final String encoding;
        /**
         * Blog title.
         */
        private final String title;
        /**
         * Blog URL.
         */
        private final URI link;
        /**
         * Blog description.
         */
        private final String description;
        /**
         * Blog language.
         */
        private final String language;
        /**
         * Date of last feed generation.
         */
        private final DateTime lastBuildDate;

        /**
         * Dedicated constructor.
         *
         * @param templates must not be {@code null}
         * @param directories must not be {@code null}
         * @param configuration must not be {@code null}
         * @param lastBuildDate must not be {@code null}
         */
        public Config(
            final Templates templates,
            final Directories directories,
            final BlogConfiguration configuration,
            final DateTime lastBuildDate) {
            super();
            Validate.notNull(configuration, "configuration");
            this.template = Validate.notNull(templates, "templates").getFeedTemplate();
            this.outputDir = Validate.notNull(directories, "directories").getOutput();
            this.encoding = configuration.getEncoding();
            this.title = configuration.getTitle();
            this.link = configuration.getBaseUri();
            this.description = configuration.getDescription();
            this.language = configuration.getLanguage();
            this.lastBuildDate = Validate.notNull(lastBuildDate, "lastBuildDate");
        }

    }
}

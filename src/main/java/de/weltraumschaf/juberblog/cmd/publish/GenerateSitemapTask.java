package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.PageConverter;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.Task;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Verbose;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Task to generate a site map XML.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public class GenerateSitemapTask extends BaseTask<Void, Pages> implements Task<Void, Pages> {

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
    public GenerateSitemapTask(final Config config, final Verbose verbose) {
        super(Pages.class, verbose);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Void execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Void execute(final Pages previusResult) throws Exception {
        println("Generate site map...");
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment template = fmd.createFragemnt(
            config.template,
            config.template.toString(),
            RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable(TemplateVariables.ENCODING, config.encoding);
        previusResult.sort(new Page.SortByDateAscending());
        template.assignVariable(TemplateVariables.URLS, previusResult.convert(new ForSitemapConverter()));

        Files.write(
            config.outputDir.resolve("site_map" + FileNameExtension.XML.getExtension()),
            fmd.render(template).getBytes(config.encoding)
        );

        return null;
    }

    private static final class ForSitemapConverter implements PageConverter {

        @Override
        public Map<String, Object> convert(final Page page) {
            final Map<String, Object> item = Maps.newHashMap();
            item.put(TemplateVariables.LOC.getVariableName(), page.getLink().toString());
            // XXX Introduce last mod date.
            item.put(TemplateVariables.LASTMOD.getVariableName(), DateFormatter.format(page.getPublishingDate(), DateFormatter.Format.W3C_DATE_FORMAT));

            if (page.getType() == PageType.POST) {
                item.put(TemplateVariables.CHANGEFREQ.getVariableName(), "daily");
                item.put(TemplateVariables.PRIORITY.getVariableName(), "0.8");
            } else {
                item.put(TemplateVariables.CHANGEFREQ.getVariableName(), "weekly");
                item.put(TemplateVariables.PRIORITY.getVariableName(), "0.5");
            }

            return Collections.unmodifiableMap(item);
        }
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * Template for site map XML.
         */
        private final Path template;
        /**
         * Where to store site map.
         */
        private final Path outputDir;
        /**
         * Used to read/write files and as the encoding in the HTML.
         */
        private final String encoding;

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
            this.template = Validate.notNull(templates, "templates").getSiteMapTemplate();
            this.outputDir = Validate.notNull(directories, "directories").getOutput();
            this.encoding = Validate.notNull(configuration, "configuration").getEncoding();
        }

    }
}

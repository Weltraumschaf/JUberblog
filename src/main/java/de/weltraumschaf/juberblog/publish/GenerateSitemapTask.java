package de.weltraumschaf.juberblog.publish;

import com.beust.jcommander.internal.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.Task;
import de.weltraumschaf.juberblog.core.Templates;
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
     */
    public GenerateSitemapTask(final Config config) {
        super(Pages.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Void execute() throws Exception {
        return execute(new Page.Pages());
    }

    @Override
    public Void execute(final Pages previusResult) throws Exception {
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment template = fmd.createFragemnt(
                config.template,
                config.encoding,
                config.template.toString(),
                RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable("encoding", config.encoding);
        template.assignVariable("urls", convert(previusResult));

        Files.write(
                config.outputDir.resolve("site_map" + FileNameExtension.XML.getExtension()),
                fmd.render(template).getBytes(config.encoding)
        );

        return null;
    }

    @Override
    protected Map<String, String> convert(final Page page) {
        final Map<String, String> item = Maps.newHashMap();
        item.put("loc", page.getLink().toString());
        // XXX Introduce last mod date.
        item.put("lastmod", DateFormatter.format(page.getPublishingDate(), DateFormatter.Format.W3C_DATE_FORMAT));

        if (page.getType() == Page.Type.POST) {
            item.put("changefreq", "daily");
            item.put("priority", "0.8");
        } else {
            item.put("changefreq", "weekly");
            item.put("priority", "0.5");
        }

        return Collections.unmodifiableMap(item);
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
                final Configuration configuration) {
            super();
            this.template = Validate.notNull(templates, "templates").getSiteMapTemplate();
            this.outputDir = Validate.notNull(directories, "directories").getOutput();
            this.encoding = Validate.notNull(configuration, "configuration").getEncoding();
        }

    }
}

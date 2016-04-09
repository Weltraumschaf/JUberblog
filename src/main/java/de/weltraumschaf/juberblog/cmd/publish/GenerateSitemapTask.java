package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.PageConverter;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.Task;
import java.nio.file.Files;
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
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public GenerateSitemapTask(final Registry registry) {
        super(Pages.class, registry);
    }

    @Override
    public Void execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Void execute(final Pages previusResult) throws Exception {
        println("Generate site map...");
        final Fragment template = fmd().createFragemnt(
            templates().getSiteMapTemplate(),
            templates().getSiteMapTemplate().toString(),
            RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable(TemplateVariables.ENCODING, configuration().getEncoding());
        previusResult.sort(new Page.SortByDateAscending());
        template.assignVariable(TemplateVariables.URLS, previusResult.convert(new ForSitemapConverter()));

        Files.write(
            directories().getOutput().resolve("site_map" + FileNameExtension.XML.getExtension()),
            fmd().render(template).getBytes(configuration().getEncoding())
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
}

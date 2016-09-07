package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.DateFormatter.Format;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.task.BaseTask;
import de.weltraumschaf.juberblog.core.PageConverter;
import de.weltraumschaf.juberblog.task.Task;
import java.nio.file.Files;
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
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public GenerateFeedTask(final Registry registry) {
        super(Pages.class, registry);
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(final Pages previousResult) throws Exception {
        println("Generate feed...");
        final Fragment template = fmd().createFragemnt(
            templates().getFeedTemplate(),
            templates().getFeedTemplate().toString(),
            RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable(TemplateVariables.ENCODING, configuration().getEncoding());
        template.assignVariable(TemplateVariables.BLOG_TITLE, configuration().getTitle());
        template.assignVariable(TemplateVariables.LINK, configuration().getBaseUri());
        template.assignVariable(TemplateVariables.BLOG_DESCRIPTION, configuration().getDescription());
        template.assignVariable(TemplateVariables.LANGUAGE, configuration().getLanguage());
        template.assignVariable(
            TemplateVariables.LAST_BUILD_DATE,
            DateFormatter.format(new DateTime("2014-12-08T20:17:00") // FIXME Change this.
                , Format.RSS_PUBLISH_DATE_FORMAT));
        template.assignVariable(TemplateVariables.ITEMS, previousResult.convert(new ForFeedConverter()));

        Files.write(directories().getOutput().resolve("feed" + FileNameExtension.XML.getExtension()),
            fmd().render(template).getBytes(configuration().getEncoding())
        );

        return previousResult;
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
}

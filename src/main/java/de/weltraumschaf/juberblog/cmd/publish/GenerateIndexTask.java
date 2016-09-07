package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.task.BaseTask;
import de.weltraumschaf.juberblog.core.PageConverter;
import de.weltraumschaf.juberblog.task.Task;
import java.nio.file.Files;
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
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public GenerateIndexTask(final Registry registry) {
        super(Pages.class, registry);
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(Pages previousResult) throws Exception {
        println("Generate index...");
        final Fragment content = fmd().createFragemnt(
            templates().getIndexTemplate(),
            RenderOptions.WITHOUT_MARKDOWN);
        final Layout layout = fmd().createLayout(
            templates().getLayoutTemplate(),
            RenderOptions.WITHOUT_MARKDOWN);
        layout.assignTemplateModel(TemplateVariables.CONTENT, content);
        layout.assignVariable(TemplateVariables.KEYWORDS, "");
        layout.assignVariable(TemplateVariables.LANGUAGE, configuration().getLanguage());
        layout.assignVariable(TemplateVariables.ENCODING, configuration().getEncoding());
        layout.assignVariable(TemplateVariables.BLOG_TITLE, configuration().getTitle());
        layout.assignVariable(TemplateVariables.BLOG_DESCRIPTION, configuration().getDescription());
        layout.assignVariable(TemplateVariables.BLOG_VERSION, version().getVersion());
        layout.assignVariable(TemplateVariables.DESCRIPTION, configuration().getDescription());
        layout.assignVariable(TemplateVariables.BASE_URL, configuration().getBaseUri());
        previousResult.sort(new Page.SortByDateDescending());
        content.assignVariable(TemplateVariables.POSTS, previousResult.convert(new ForIndexConverter()));
        Files.write(
            directories().getOutput().resolve("index" + FileNameExtension.HTML.getExtension()),
            fmd().render(layout).getBytes(configuration().getEncoding())
        );

        return previousResult;
    }

    private static final class ForIndexConverter implements PageConverter {

        @Override
        public Map<String, Object> convert(final Page page) {
            Validate.notNull(page, "page");
            final Map<String, Object> item = Maps.newHashMap();
            item.put(TemplateVariables.TITLE.getVariableName(), page.getTitle());
            item.put(TemplateVariables.LINK.getVariableName(), page.getLink().toString());
            item.put(TemplateVariables.DESCRIPTION.getVariableName(), page.getDescription());
            item.put(TemplateVariables.PUB_DATE.getVariableName(), page.getPublishingDate().toDate());
            return Collections.unmodifiableMap(item);
        }
    }

}

package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.app.App.Factory;
import de.weltraumschaf.juberblog.app.App.FactoryImpl;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.cmd.create.CreateSubCommand;
import de.weltraumschaf.juberblog.cmd.install.InstallSubCommand;
import de.weltraumschaf.juberblog.options.Options;
import de.weltraumschaf.juberblog.options.Options.Command;
import de.weltraumschaf.juberblog.cmd.publish.PublishSubCommand;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FactoryImpl}.
 *
 * @author Sven Strittmatter
 */
public class FactoryTest extends BaseTestCase {

    private final Directories directories = new Directories(
            Paths.get("."),
            Paths.get("."));
    private final Templates templates = new Templates(
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."));
    private final JUberblog registry = JUberblog.Builder.create()
                .directories(directories)
                .templates(templates)
                .configuration(new BlogConfiguration(createProperties()))
                .options(new Options())
                .io(mock(IO.class))
                .product();

    private final Factory sut = new FactoryImpl();

    @Test
    public void forName_CREATE() {
        assertThat(sut.forName(Command.CREATE, registry),
                is(instanceOf(CreateSubCommand.class)));
    }

    @Test
    public void forName_INSTALL() {
        assertThat(sut.forName(Command.INSTALL, registry),
                is(instanceOf(InstallSubCommand.class)));
    }

    @Test
    public void forName_PUBLISH() {
        assertThat(sut.forName(Command.PUBLISH, registry),
                is(instanceOf(PublishSubCommand.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void forName_UNKNOWN() {
        sut.forName(Command.NONE, registry);
    }
}

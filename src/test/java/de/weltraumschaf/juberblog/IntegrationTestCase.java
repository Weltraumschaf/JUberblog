package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.system.Exitable;
import de.weltraumschaf.juberblog.app.App;
import static org.mockito.Mockito.mock;

/**
 * Base class for integration tests .
 *
 * @author Sven Strittmatter
 */
public abstract class IntegrationTestCase extends BaseTestCase {

    private final Exitable exiter = mock(Exitable.class);

    protected final App createApp(final String[] args) {
        final App app = new App(args);
        app.setExiter(exiter);
        return app;
    }

    protected final Exitable getExiter() {
        return exiter;
    }

}

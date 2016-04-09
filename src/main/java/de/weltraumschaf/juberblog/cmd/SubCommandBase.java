package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Verbose;
import de.weltraumschaf.juberblog.options.Options;

/**
 * Common functionality for sub commands.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public abstract class SubCommandBase implements SubCommand, Registry {

    /**
     * Provides some important objects.
     */
    private final JUberblog registry;

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public SubCommandBase(final JUberblog registry) {
        super();
        this.registry = Validate.notNull(registry, "registry");
    }

    @Override
    public final Options options() {
        return registry.options();
    }

    @Override
    public final IO io() {
        return registry.io();
    }

    @Override
    public final Templates templates() {
        return registry.templates();
    }

    @Override
    public final Directories directories() {
        return registry.directories();
    }

    @Override
    public final BlogConfiguration configuration() {
        return registry.configuration();
    }

    @Override
    public final Version version() {
        return registry.version();
    }

    @Override
    public final FreeMarkerDown fmd() {
        return registry.fmd();
    }

    @Override
    public Verbose verbose() {
        return registry.verbose();
    }

    @Override
    public void execute() throws ApplicationException {
        validateArguments();
        doExecute();
    }

    protected final JUberblog registry() {
        return registry;
    }

    /**
     * Validates the required command line arguments.
     *
     * @throws ApplicationException if title is empty
     */
    protected void validateArguments() throws ApplicationException {}

    abstract protected void doExecute() throws ApplicationException;

}

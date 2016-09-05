package de.weltraumschaf.juberblog.cmd.install;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.options.InstallOptions;
import java.io.File;
import java.io.IOException;

/**
 * Installs a fresh blog.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class InstallSubCommand extends SubCommandBase {

    /**
     * Used to copy the scaffold.
     */
    private final Scaffold scaffold;

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public InstallSubCommand(final JUberblog registry) {
        super(registry);
        scaffold = new Scaffold(io());
    }

    private InstallOptions installOptions() {
        return options().getInstall();
    }

    @Override
    public void doExecute() throws ApplicationException {
        final String location = installOptions().getLocation().trim();
        final File target = validateLocation(location);
        io().println(String.format("Install scaffold to '%s'...", location));

        if (installOptions().isForce()) {
            scaffold.setType(InstallationType.OVERWRITE);
        } else if (installOptions().isUpdate()) {
            scaffold.setType(InstallationType.BACKUP);
        } else if (null != target.list() && target.list().length > 0) {
            throw new ApplicationException(
                ExitCodeImpl.FATAL,
                "Target directory not empty! Use -f to force install or -u to update.");
        }

        try {
            scaffold.setVerbose(installOptions().isVerbose());
            scaffold.copyFiles(target);
        } catch (IOException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Can't install scaffold!", ex);
        }
    }

    /**
     * Injection point for a custom provider.
     *
     * @param srcJar must not be {@code null}
     */
    void setSrcJar(final Scaffold.SourceJarProvider srcJar) {
        scaffold.setSrcJar(srcJar);
    }

    /**
     * Validates the required command line arguments.
     *
     * @throws ApplicationException if title is empty
     */
    @Override
    protected void validateArguments() throws ApplicationException {
        if (installOptions().isForce() && installOptions().isUpdate()) {
            throw new ApplicationException(
                ExitCodeImpl.BAD_ARGUMENT,
                "You must not use FORCE and UPGRADE flag together!");
        }
    }

    /**
     * Validate installation directory.
     *
     * Validates that given string is not empty and is an existing directory.
     *
     * @param location must not be {@code null} or empty
     * @return never {@code null}
     * @throws ApplicationException if target does not exist or is not a directory
     */
    private File validateLocation(final String location) throws ApplicationException {
        final File target = new File(Validate.notNull(location, "location"));

        if (!target.exists()) {
            throw new ApplicationException(
                ExitCodeImpl.BAD_ARGUMENT,
                String.format("Install location '%s' does not exist!", location));
        }

        if (!target.isDirectory()) {
            throw new ApplicationException(
                ExitCodeImpl.BAD_ARGUMENT,
                String.format("Install location '%s' is not a directory!", location));
        }

        return target;
    }
}

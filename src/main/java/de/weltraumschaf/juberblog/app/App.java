package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.JUberblog;
import com.beust.jcommander.ParameterException;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.create.CreateSubCommand;
import de.weltraumschaf.juberblog.cmd.install.InstallSubCommand;
import de.weltraumschaf.juberblog.options.Options;
import de.weltraumschaf.juberblog.options.Options.Command;
import de.weltraumschaf.juberblog.cmd.publish.PublishSubCommand;
import java.io.UnsupportedEncodingException;

/**
 * Main class invoked by JVM.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class App extends InvokableAdapter {

    /**
     * To obtain environment variables.
     */
    private final Environments.Env env;
    /**
     * Command line arguments.
     */
    private final Options options = new Options();
    /**
     * Provides sub commands.
     */
    private Factory subCommands = new FactoryImpl();

    /**
     * Convenience constructor with default environment.
     *
     * @param args must not be {@code null}
     */
    public App(final String[] args) {
        this(args, Environments.defaultEnv());
    }

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     * @param env must not be {@code null}
     */
    App(final String[] args, final Environments.Env env) {
        super(args);
        this.env = Validate.notNull(env, "env");
    }

    /**
     * Main entry point of VM.
     *
     * @param args CLI arguments from VM
     */
    public static void main(final String[] args) {
        final App invokable = new App(args);
        invokable.debug = invokable.isEnvDebug();

        try {
            InvokableAdapter.main(
                invokable,
                IOStreams.newDefault());
        } catch (final UnsupportedEncodingException ex) {
            handleFatalErrors(invokable, ex, ExitCodeImpl.CANT_READ_IO_STREAMS, "Can't create IO streams!\n");
        }
    }

    /**
     * Handles all not yet caught exceptions in main function.
     *
     * @param invokable must not be {@code null}
     * @param cause must not be {@code null}
     * @param code must not be {@code null}
     * @param prefix must not be {@code null}
     */
    private static void handleFatalErrors(
        final App invokable,
        final Throwable cause,
        final ExitCode code,
        final String prefix) {
        // CHECKSTYLE:OFF
        // At this point we do not have IO streams.
        System.err.print(Validate.notNull(prefix, "prefix"));
        System.err.println(Validate.notNull(cause, "cause").getMessage());

        if (Validate.notNull(invokable, "invokable").isEnvDebug()) {
            cause.printStackTrace(System.err);
        }

        // CHECKSTYLE:ON
        invokable.exit(Validate.notNull(code, "code").getCode());
    }

    @Override
    public void execute() throws Exception {
        final Command commandName;

        try {
            options.parse(getArgs());
            commandName = options.getParsedCommand();
        } catch (final ParameterException ex) {
            throw badArgumentError(ex, options.getParsedCommand());
        }

        if (Command.NONE == commandName) {
            executeMainCommand();
        } else {
            executeSubCommand(commandName);
        }
    }

    /**
     * Executes the main application code here.
     *
     * @throws ApplicationException on any application error such as bad arguments
     */
    private void executeMainCommand() throws ApplicationException {
        if (options.isHelp()) {
            showHelp();
            return;
        }

        if (options.getMain().isVersion()) {
            final Version version = JUberblog.generateWithDefaultConfig(options, getIoStreams()).version();
            showVersion(version);
            return;
        }

        throw badArgumentError();
    }

    /**
     * Executes a sub command named by fist CLI argument.
     *
     * @param commandName must not be {@code null}
     * @throws Exception if anything went wrong
     */
    private void executeSubCommand(final Command commandName) throws Exception {
        Validate.notNull(commandName, "commandName");
        final JUberblog registry;

        if (options.isHelp()) {
            showHelp(commandName);
            return;
        }

        switch (commandName) {
            case INSTALL:
                registry = JUberblog.generateWithDefaultConfig(options, getIoStreams());
                break;
            case CREATE: {
                final BlogConfiguration config = JUberblog.generateConfiguration(options.getCreate());
                registry = JUberblog.generate(options, getIoStreams(), config);
                break;
            }
            case PUBLISH: {
                final BlogConfiguration config = JUberblog.generateConfiguration(options.getPublish());
                registry = JUberblog.generate(options, getIoStreams(), config);
                break;
            }
            default:
                throw badArgumentError("Unsupported command!", commandName);
        }

        subCommands.forName(commandName, registry).execute();
    }

    /**
     * Throw a generic bad CLI argument error.
     *
     * @throws ApplicationException always
     */
    private ApplicationException badArgumentError() throws ApplicationException {
        return badArgumentError("Bad arguments!", Command.NONE);
    }

    /**
     * Throw bad CLI argument error with custom message.
     *
     * @param messageFormat additional error message
     */
    private ApplicationException badArgumentError(final String messageFormat, final Command name, final Object... args) {
        return badArgumentError(messageFormat, null, name, args);
    }

    /**
     * Throw a generic bad CLI argument error with exception for debug output the stack trace.
     *
     * @param ex may be {@code null}
     */
    private ApplicationException badArgumentError(final ParameterException ex, final Command name) {
        return badArgumentError("Bad arguments (cause: %s)!", ex, name, ex.getMessage());
    }

    /**
     * Throw bad CLI argument error with custom message with exception for debug output the stack trace.
     *
     * @param messageFormat additional error message
     * @param ex may be {@code null}
     */
    private ApplicationException badArgumentError(final String messageFormat, final ParameterException ex, final Command name, final Object... args) {
        return new ApplicationException(ExitCodeImpl.BAD_ARGUMENT, errorMessage(messageFormat, name, args), ex);
    }

    /**
     * Appends usage to given message.
     *
     * @param messageFormat must not be {@code null} or empty
     * @param name must not be {@code null}
     * @param args optional arguments for the message format string.
     * @return never {@code null} or empty
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    private String errorMessage(final String messageFormat, final Command name, final Object... args) {
        return new StringBuilder()
            .append(String.format(Validate.notEmpty(messageFormat, "messageFormat"), args))
            .append(Constants.DEFAULT_NEW_LINE.toString())
            .append("Usage: ")
            .append(Constants.COMMAND_NAME.toString())
            .append(' ')
            .append(options.usage(name))
            .toString();
    }

    /**
     * Show help message.
     */
    private void showHelp() {
        showHelp(Command.NONE);
    }

    private void showHelp(final Command name) {
        getIoStreams().println(options.help(name));
    }

    /**
     * Show version message.
     */
    private void showVersion(final Version version) {
        getIoStreams().println(version.getVersion());
    }

    /**
     * Whether debug output is enabled by environment variable.
     *
     * @return {@code true} for enabled, else {@code false}
     */
    boolean isEnvDebug() {
        return "true".equalsIgnoreCase(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString()).trim());
    }

    /**
     * Injection point for testing.
     *
     * @param newSubCommands must not be {@code null}
     */
    void injectFactory(final Factory newSubCommands) {
        this.subCommands = Validate.notNull(newSubCommands, "factory");
    }

    /**
     * Creates sub command instances.
     */
    public interface Factory {

        /**
         * Creates sub command for name.
         * <p>
         * Throws {@link IllegalArgumentException} for unsupported names.
         * </p>
         *
         * @param name must not be {@code null}
         * @param registry must not be {@code null}
         * @return never {@code null}, always new instance
         */
        SubCommand forName(final Command name, final JUberblog registry);
    }

    /**
     * Default implementation.
     */
    static final class FactoryImpl implements Factory {

        @Override
        public SubCommand forName(final Command name, final JUberblog registry) {
            switch (Validate.notNull(name, "name")) {
                case CREATE:
                    return new CreateSubCommand(registry);
                case INSTALL:
                    return new InstallSubCommand(registry);
                case PUBLISH:
                    return new PublishSubCommand(registry);
                default:
                    throw new IllegalArgumentException(String.format("Unsupported command name: '%s'!", name));
            }
        }
    }
}

/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
package de.weltraumschaf.juberblog;

import com.beust.jcommander.JCommander;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.cmd.SubCommands;
import de.weltraumschaf.juberblog.files.HomeDir;
import de.weltraumschaf.juberblog.files.LockFile;
import de.weltraumschaf.juberblog.opt.CreateOptions;
import de.weltraumschaf.juberblog.opt.InstallOptions;
import de.weltraumschaf.juberblog.opt.Options;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.parboiled.common.StringUtils;

/**
 * Main class.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class App extends InvokableAdapter {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(App.class);
    /**
     * Version information.
     */
    private final Version version;
    /**
     * To obtain environment variables.
     */
    private final Environments.Env env = Environments.defaultEnv();
    /**
     * Creates and holds the home directory.
     */
    private final HomeDir home = new HomeDir(env);

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     */
    public App(final String[] args) {
        super(args);
        version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
    }

    /**
     * Main entry point of VM.
     *
     * @param args cli arguments from VM
     */
    public static void main(final String[] args) {
        final App invokable = new App(args);

        try {
            InvokableAdapter.main(invokable, IOStreams.newDefault(), invokable.isDebug());
        } catch (final UnsupportedEncodingException ex) {
            // CHECKSTYLE:OFF
            // At this point we do not have IO streams.
            System.err.println("Can't create IO streams!");
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
            // CHECKSTYLE:ON
            invokable.exit(-1);
        }
    }

    /**
     * Determine if debug is enabled by environment variable {@link Constants#ENVIRONMENT_VARIABLE_DEBUG}.
     *
     * @return by default {@code false} if environment is not present or false
     */
    boolean isDebug() {
        final String debug = env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString());
        return "true".equalsIgnoreCase(debug.trim());
    }

    @Override
    public void execute() throws Exception {
        version.load();
        home.createIfNotExists();
        final LockFile lock = new LockFile(home.getPath().resolve("lock"));

        if (lock.exists()) {
            throw new ApplicationException(
                    ExitCodeImpl.FATAL, String.format("JUberblog is already running!%n"
                            + "If you are shure this is not the case delete the lock file '%s'.", lock.getPath()));
        }

        lock.create();
        registerShutdownHook(new Callable<Void>() {
            @Override
            public Void call() throws IOException {
                lock.remove();
                return null;
            }
        });

        final Arguments args = validateArguments();
        final SubCommands subCommandName = SubCommands.forSubCommandName(args.getFirstArgument());
        final SubCommand cmd = createSubcommand(subCommandName);
        parseOptions(subCommandName, args, cmd);
        cmd.execute();
    }

    /**
     * Generates help message for main command.
     *
     * @return never {@code null} or empty
     */
    private String helpMessage() {
        final StringBuilder help = new StringBuilder();
        help.append("Usage: ")
                .append(Constants.COMMAND_NAME)
                .append(" [-v|--version] [-h|--help] ")
                .append(StringUtils.join(SubCommands.implemented(), "|"));
        return help.toString();
    }

    /**
     * Validates the command line arguments and creates the argument object.
     *
     * @return never {@code null}
     * @throws ApplicationException if too few arguments given
     */
    Arguments validateArguments() throws ApplicationException {
        final Arguments args = new Arguments(getArgs());
        final String firstArgument = args.getFirstArgument().trim();

        if ("-h".equals(firstArgument) || "--help".equals(firstArgument)) {
            throw new ApplicationException(ExitCodeImpl.OK, helpMessage(), null);
        } else if ("-v".equals(firstArgument) || "--version".equals(firstArgument)) {
            throw new ApplicationException(ExitCodeImpl.OK, String.format("Version: %s", version), null);
        } else if (firstArgument.isEmpty()) {
            final StringBuilder errorMesage = new StringBuilder("No sub comamnd given!");
            errorMesage.append(Constants.DEFAULT_NEW_LINE)
                    .append(helpMessage());
            throw new ApplicationException(ExitCodeImpl.TOO_FEW_ARGUMENTS, errorMesage.toString(), null);
        }

        return args;
    }

    /**
     * Creates sub command object.
     *
     * @param subCommandType must not be {@code null}
     * @return never {@code null}
     * @throws ApplicationException if command is unknown
     */
    SubCommand createSubcommand(final SubCommands subCommandType) throws ApplicationException {
        Validate.notNull(subCommandType, "Sub command type must not be null!");

        try {
            return SubCommands.create(subCommandType, getIoStreams());
        } catch (final IllegalArgumentException ex) {
            throw new ApplicationException(
                    ExitCodeImpl.UNKNOWN_COMMAND,
                    ex.getMessage(),
                    ex);
        }
    }

    /**
     * Parses the command line options and set them to the command.
     *
     * Also checks if help is wanted.
     *
     * @param type must not be {@code null}
     * @param args must not be {@code null}
     * @param cmd must not be {@code null}
     * @throws ApplicationException if help is wanted
     */
    void parseOptions(final SubCommands type, final Arguments args, final SubCommand cmd)
            throws ApplicationException {
        Validate.notNull(type, "Type must not be null!");
        Validate.notNull(args, "Arguments must not be null!");
        Validate.notNull(cmd, "Sub command must not be null!");
        final JCommander optionsParser = new JCommander();
        optionsParser.setProgramName(Constants.COMMAND_NAME.toString());
        final Options opts;

        switch (type) {
            case CREATE:
                opts = new CreateOptions();
                optionsParser.addObject(opts);
                break;
            case INSTALL:
                opts = new InstallOptions();
                optionsParser.addObject(opts);
                break;
            case PUBLISH:
                opts = new PublishOptions();
                optionsParser.addObject(opts);
                break;
            default:
                LOG.warn(String.format("Unsupported sub command %s!", type));
                opts = null;
        }

        optionsParser.parse(args.getTailArguments());
        cmd.setOptions(opts);

        if (null != opts && opts.isHelp()) {
            final StringBuilder errorMessage = new StringBuilder();
            optionsParser.usage(errorMessage);
            throw new ApplicationException(
                    ExitCodeImpl.OK,
                    errorMessage.toString(),
                    null);
        }
    }

}

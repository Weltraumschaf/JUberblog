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
package de.weltraumschaf.juberblog.cmd;

import com.google.common.collect.Maps;
import de.weltraumschaf.commons.IO;
import java.util.Map;
import org.apache.commons.lang3.Validate;

/**
 * Available sub commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum SubCommands {

    /**
     * Name for {@link CreateSubCommand}.
     */
    CREATE("create"),
    /**
     * Name for {@link PublishSubCommand}.
     */
    PUBLISH("publish"),
    /**
     * Name for {@link InstallSubCommand}.
     */
    INSTALL("install");
    /**
     * Used to lookup type by name.
     */
    private static final Map<String, SubCommands> LOOKUP = Maps.newHashMap();
    static {
        for (final SubCommands cmd : values()) {
            LOOKUP.put(cmd.toString(), cmd);
        }
    }
    /**
     * Textual name of sub command.
     */
    private final String subCommandName;

    /**
     * Dedicated constructor.
     *
     * @param subCommandName must not be {@code null} or empty
     */
    SubCommands(final String subCommandName) {
        Validate.notEmpty(subCommandName, "Sub command name must not be null or empty!");
        this.subCommandName = subCommandName;
    }

    @Override
    public String toString() {
        return subCommandName;
    }

    /**
     * Factory to lookup type for a given name.
     *
     * Throws {@link IllegalArgumentException} if given name can't be looked up.
     *
     * @param name must not be {@code null}
     * @return never {@code null}
     */
    public static SubCommands forSubCommandName(final String name) {
        Validate.notNull(name, "Name must not be null!");
        final String normalizedName = name.trim().toLowerCase();

        if (LOOKUP.containsKey(normalizedName)) {
            return LOOKUP.get(normalizedName);
        }

        throw new IllegalArgumentException(String.format("Unknown command '%s'!", name));
    }

    /**
     * Create sub command for given type.
     *
     * Throws {@link IllegalArgumentException} if unsupported type was given.
     *
     * @param type must not be {@code null}
     * @param io must not be {@code null}
     * @return never {@code null}
     */
    public static SubCommand create(final SubCommands type, final IO io) {
        Validate.notNull(type, "Type must not be null!");
        Validate.notNull(io, "IO must not be null!");

        switch (type) {
            case CREATE:
                return new CreateSubCommand(io);
            case PUBLISH:
                return new PublishSubCommand(io);
            case INSTALL:
                return new InstallSubCommand(io);
            default:
                throw new IllegalArgumentException(String.format("Unknown command type '%s'!", type));
        }
    }
}

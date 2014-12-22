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

import de.weltraumschaf.commons.system.Exitable;
import de.weltraumschaf.juberblog.app.App;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
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

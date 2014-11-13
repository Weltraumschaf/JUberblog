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

package de.weltraumschaf.juberblog.template;

import de.weltraumschaf.juberblog.Constants;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TemplateDirectories {

    /**
     * Location of scaffold templates inside the JAR.
     */
    private static final String SCAFFOLD_TEMPLATE_DIR = Constants.PACKAGE_BASE.toString() + "/scaffold/templates";

    public static Path scaffold() throws URISyntaxException {
        return create(SCAFFOLD_TEMPLATE_DIR);
    }

    public static Path create(final String templateDirectory) throws URISyntaxException {
      if (templateDirectory.startsWith(Constants.PACKAGE_BASE.toString())) {
            // Read resources via class loader for tests.
            return new File(Configurations.class.getResource(templateDirectory).toURI()).toPath();
        } else {
            return new File(templateDirectory).toPath();
        }
    }
}

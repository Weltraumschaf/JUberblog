package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Enumerates some common file name extensions.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public enum FileNameExtension {

    /**
     * Extension for Markdown.
     */
    MARKDOWN(".md"),
    /**
     * Extension for HTML.
     */
    HTML(".html"),
    /**
     * Extension von XML.
     */
    XML(".xml");

    /**
     * The extension literal.
     */
    private final String extension;

    /**
     * Dedicated constructor.
     *
     * @param fileNameExtension must not be {@code null} or empty
     */
    FileNameExtension(final String fileNameExtension) {
        this.extension = Validate.notEmpty(fileNameExtension, "fileNameExtension");
    }

    /**
     * Get the extension literal with leading dot.
     *
     * @return never {@code null} or empty
     */
    public String getExtension() {
        return extension;
    }
}

package de.weltraumschaf.juberblog.cmd.install;

/**
 * Types of installation strategies.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
enum InstallationType {

    /**
     * Requires an empty directory to install the files.
     */
    FRESH,
    /**
     * Existing files will be overwritten.
     */
    OVERWRITE,
    /**
     * Existing files will be backed up.
     */
    BACKUP;
}

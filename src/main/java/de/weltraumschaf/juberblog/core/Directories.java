package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Holds the directories to read and write the blog files.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Directories {

    /**
     * Name of sites sub directory.
     */
    public static final String SITES_DIR_NAME = "sites";
    /**
     * Name of posts sub directory.
     */
    public static final String POSTS_DIR_NAME = "posts";
    /**
     * Name of drafts sub directory.
     */
    public static final String DRAFTS_DIR_NAME = "drafts";

    /**
     * Where to find the post's Markdown files.
     */
    private final Path postsData;
    /**
     * Where to find the site's Markdown files.
     */
    private final Path sitesData;
    /**
     * Where to find the draft post's Markdown files.
     */
    private final Path postsDraftData;
    /**
     * Where to find the draft site's Markdown files.
     */
    private final Path sitesDraftData;
    /**
     * Where to store published files.
     */
    private final Path output;
    /**
     * Where to store published posts.
     */
    private final Path postsOutput;
    /**
     * Where to store published sites.
     */
    private final Path siteOutput;

    /**
     * Dedicated constructor.
     *
     * @param dataDir must not be {@code null}
     * @param outputDir must not be {@code null}
     */
    public Directories(final Path dataDir, final Path outputDir) {
        super();
        Validate.notNull(dataDir, "dataDir");
        Validate.notNull(outputDir, "outputDir");
        this.postsData = dataDir.resolve(POSTS_DIR_NAME);
        this.sitesData = dataDir.resolve(SITES_DIR_NAME);
        this.postsDraftData = dataDir.resolve(DRAFTS_DIR_NAME).resolve(POSTS_DIR_NAME);
        this.sitesDraftData = dataDir.resolve(DRAFTS_DIR_NAME).resolve(SITES_DIR_NAME);
        this.output = outputDir;
        this.postsOutput = outputDir.resolve(POSTS_DIR_NAME);
        this.siteOutput = outputDir.resolve(SITES_DIR_NAME);
    }

    /**
     * Where to find the posts data files.
     *
     * @return never {@code null}
     */
    public Path getPostsData() {
        return postsData;
    }

    /**
     * Where to find the sites data files.
     *
     * @return never {@code null}
     */
    public Path getSitesData() {
        return sitesData;
    }

    /**
     * Where to find the sites drafts data files.
     *
     * @return never {@code null}
     */
    public Path getPostsDraftData() {
        return postsDraftData;
    }

    /**
     * Where to find the posts drafts data files.
     *
     * @return never {@code null}
     */
    public Path getSitesDraftData() {
        return sitesDraftData;
    }

    /**
     * Where to store published output.
     *
     * @return never {@code null}
     */
    public Path getOutput() {
        return output;
    }

    /**
     * Where to store published posts.
     *
     * @return never {@code null}
     */
    public Path getPostsOutput() {
        return postsOutput;
    }

    /**
     * Where to store published sites.
     *
     * @return never {@code null}
     */
    public Path getSitesOutput() {
        return siteOutput;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                postsData,
                sitesData,
                postsDraftData,
                sitesDraftData,
                output,
                postsOutput,
                siteOutput);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Directories)) {
            return false;
        }

        final Directories other = (Directories) obj;

        return Objects.equals(postsData, other.postsData)
                && Objects.equals(sitesData, other.sitesData)
                && Objects.equals(postsDraftData, other.postsDraftData)
                && Objects.equals(sitesDraftData, other.sitesDraftData)
                && Objects.equals(output, other.output)
                && Objects.equals(postsOutput, other.postsOutput)
                && Objects.equals(siteOutput, other.siteOutput);
    }

    @Override
    public String toString() {
        return "Directories{"
                + "postsData=" + postsData + ", "
                + "sitesData=" + sitesData + ", "
                + "postsDraftData=" + postsDraftData + ", "
                + "sitesDraftData=" + sitesDraftData + ", "
                + "output=" + output + ", "
                + "postsOutput=" + postsOutput + ", "
                + "siteOutput=" + siteOutput
                + '}';
    }

}

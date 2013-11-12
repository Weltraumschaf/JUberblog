/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.weltraumschaf.juberblog.jvfs;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;

/**
 *
 * @author Sven.Strittmatter
 */
final class JvfsDirectoryStream implements DirectoryStream<Path> {

    public JvfsDirectoryStream(final JVFSPath aThis, final Filter<? super Path> filter) {
        super();
    }

    @Override
    public Iterator<Path> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

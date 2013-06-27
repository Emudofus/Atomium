package org.atomium;

import org.atomium.metadata.MetadataRegistry;

/**
 * @author Blackrush
 */
public interface DatabaseProvider {
    DatabaseInterface get(String url, String user, String password, MetadataRegistry registry);
}

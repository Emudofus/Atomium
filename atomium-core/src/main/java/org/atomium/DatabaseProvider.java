package org.atomium;

/**
 * @author Blackrush
 */
public interface DatabaseProvider {
    DatabaseInterface get(String url, String user, String password, MetadataRegistry registry);
}

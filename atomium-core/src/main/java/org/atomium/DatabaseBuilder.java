package org.atomium;

import com.google.common.collect.Maps;
import org.atomium.metadata.MetadataRegistry;
import org.atomium.metadata.SimpleMetadataRegistry;

import java.util.Map;

/**
 * @author Blackrush
 */
public final class DatabaseBuilder {
    private static final Map<String, DatabaseProvider> providers = Maps.newHashMap();

    public static void register(String urlRegex, DatabaseProvider provider) {
        providers.put(urlRegex, provider);
    }

    public static DatabaseProvider forUrl(String url) {
        for (Map.Entry<String, DatabaseProvider> entry : providers.entrySet()) {
            if (url.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private String url, user, password;
    private MetadataRegistry registry;

    private DatabaseBuilder() {}

    public static DatabaseBuilder builder() {
        return new DatabaseBuilder();
    }

    public DatabaseBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public DatabaseBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public DatabaseBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public DatabaseBuilder setRegistry(MetadataRegistry registry) {
        this.registry = registry;
        return this;
    }

    public DatabaseInterface build() {
        setDefaults();

        DatabaseProvider provider = forUrl(url);

        if (provider != null) {
            return provider.get(url, user, password, registry);
        }

        throw new IllegalStateException("can not deduce database implementation by url");
    }

    private void setDefaults() {
        registry = new SimpleMetadataRegistry();
    }
}

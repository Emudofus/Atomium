package org.atomium;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.atomium.annotations.Table;
import org.atomium.metadata.InstantiationListener;
import org.atomium.metadata.Metadata;
import org.atomium.metadata.MetadataRegistry;

import java.io.IOException;
import java.util.Set;

/**
 * @author Blackrush
 */
public abstract class AtomiumModule extends AbstractModule {
    private final Set<Class<?>> modelClasses = Sets.newHashSet();

    protected abstract void register();

    protected final <T> void register(Class<T> target) {
        modelClasses.add(target);
    }

    protected final void register(String packageName) {
        try {
            ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());

            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClasses(packageName)) {
                Class<?> clazz = classInfo.load();

                if (clazz.isAnnotationPresent(Table.class)) {
                    modelClasses.add(clazz);
                }
            }
        } catch (IOException e) {
            addError(e);
        }
    }

    protected void onRegistryInjection(final Injector injector, MetadataRegistry registry) {
        for (Class<?> modelClass : modelClasses) {
            registry.register(modelClass);
        }

        registry.addInstantationListener(new InstantiationListener() {
            public <T> void listen(Metadata<T> meta, T instance) {
                injector.getMembersInjector(meta.getTarget()).injectMembers(instance);
            }
        });
    }

    @Override
    protected void configure() {
        register();
        requestInjection(this);
    }

    @Inject
    public final void triggerRegistryInjection(Injector injector, MetadataRegistry registry) {
        onRegistryInjection(injector, registry);
    }

    @Provides
    SessionInterface provideSession(DatabaseInterface db) {
        return db.createSession();
    }
}

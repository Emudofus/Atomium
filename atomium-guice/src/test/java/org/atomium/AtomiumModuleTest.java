package org.atomium;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.atomium.annotations.Column;
import org.atomium.annotations.PrimaryKey;
import org.atomium.metadata.Metadata;
import org.atomium.metadata.MetadataRegistry;
import org.atomium.metadata.SimpleMetadataRegistry;
import org.atomium.models.MyDummyModel;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Blackrush
 */
public class AtomiumModuleTest {
    private MetadataRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new SimpleMetadataRegistry();
    }

    public final Module BOOTSTRAP_MODULE = new AbstractModule() {
        @Override
        protected void configure() {
            bind(MetadataRegistry.class).toInstance(registry);
        }
    };

    @Test
    public void testRegisteredModels() throws Exception {
        Guice.createInjector(BOOTSTRAP_MODULE, new AtomiumModule() {
            @Override
            protected void register() {
                register(MyEntity.class);
            }
        });

        Metadata<MyEntity> myEntity = registry.get(MyEntity.class);
        assertThat(myEntity, notNullValue());

        Metadata<MyEntityThatNeedsRegistration> myEntityThatNeedsRegistration = registry.get(MyEntityThatNeedsRegistration.class);
        assertThat(myEntityThatNeedsRegistration, nullValue());
    }

    @Test
    public void testRegisterPackage() throws Exception {
        Guice.createInjector(BOOTSTRAP_MODULE, new AtomiumModule() {
            protected void register() {
                register("org.atomium.models");
            }
        });

        Metadata<MyDummyModel> myDummyModel = registry.get(MyDummyModel.class);
        assertThat(myDummyModel, notNullValue());
    }

    @Test
    public void testInjectedServices() throws Exception {
        final Object myService = "MY SERVICE LEL";

        Guice.createInjector(BOOTSTRAP_MODULE, new AtomiumModule() {
            protected void register() {
                register(MyInjectedEntity.class);
                bind(Object.class).annotatedWith(Names.named("my.service.name")).toInstance(myService);
            }
        });

        MyInjectedEntity instance = registry.get(MyInjectedEntity.class).createEmpty();
        assertThat(instance.myService, is(myService));
    }

    public static class MyEntity {
        @Column
        @PrimaryKey
        private int id;
    }

    public static class MyEntityThatNeedsRegistration {
        @Column
        @PrimaryKey
        private int id;
    }

    public static class MyInjectedEntity {
        @Column
        @PrimaryKey
        private int id;

        @Inject
        @Named("my.service.name")
        private Object myService;
    }
}

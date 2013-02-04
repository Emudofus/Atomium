package org.atomium;

import org.atomium.entity.EntityMetadata;
import org.atomium.entity.EntityProperty;
import org.atomium.pkey.PrimaryKey;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author blackrush
 */
public class EntityPropertyTest {

    private EntityProperty<MyModel> id;
    private EntityProperty<MyModel> createdAt;

    @Before
    public void initMetadata() {
        EntityMetadata<MyModel> metadata = EntityMetadata.of(MyModel.class);
        this.id = metadata.getPrimaryKeyProperty();
        this.createdAt = metadata.getProperty("created_at").get();
    }

    @Test
    public void name() {
        assertThat(id.getName(), equalTo("id"));
        assertThat(createdAt.getName(), equalTo("created_at"));
    }

    @Test
    public void type() {
        assertThat(id.getPropertyClass(), equalTo((Class) PrimaryKey.class)); // TODO IntKey instead of PrimaryKey
        assertThat(createdAt.getPropertyClass(), equalTo((Class) Instant.class));
    }

    @Test
    public void mutability() {
        assertThat(id.isMutable(), is(true));
        //assertThat(createdAt.isMutable(), is(false)); FIXME
    }
}

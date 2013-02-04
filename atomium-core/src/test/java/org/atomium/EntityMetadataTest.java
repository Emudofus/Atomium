package org.atomium;

import org.atomium.entity.EntityMetadata;
import org.junit.Before;
import org.junit.Test;

import static org.atomium.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author blackrush
 */
public class EntityMetadataTest {

    private EntityMetadata<MyModel> metadata;

    @Before
    public void initMetadata() {
        this.metadata = EntityMetadata.of(MyModel.class);
    }

    @Test
    public void validName() {
        assertThat(metadata.getName(), equalTo("my_custom_model"));
    }

    @Test
    public void properties() {
        assertThat(metadata.getPrimaryKeyProperty(), notNullValue());
        assertThat(metadata.getProperty("id"), isPresent());
        assertThat(metadata.getProperty("my_custom_name"), isPresent());
        assertThat(metadata.getProperty("created_at"), isPresent());
        assertThat(metadata.getProperty("persisted_at"), isPresent());
        assertThat(metadata.getProperty("deleted_at"), isPresent());
    }

    @Test
    public void findPropertyByType() {
        assertThat(metadata.getProperty(String.class).get().getName(), equalTo("my_custom_name"));
    }
}

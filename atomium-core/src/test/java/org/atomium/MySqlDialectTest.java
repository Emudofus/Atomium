package org.atomium;

import org.atomium.entity.EntityMetadata;
import org.atomium.persistence.Dialect;
import org.atomium.persistence.dialect.Dialects;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author blackrush
 */
public class MySqlDialectTest {

    private EntityMetadata<MyModel> metadata;
    private Dialect dialect;

    @Before
    public void initDialect() {
        this.metadata = EntityMetadata.of(MyModel.class);
        this.dialect = Dialects.mysql();
    }

    @Test
    public void select() {
        Query query = dialect.select(metadata);

        assertTrue(query.getCommand().equals(
                "SELECT `id`,`persisted_at`,`my_custom_name`,`deleted_at`,`created_at` FROM `my_custom_model`;"));
    }

    @Test
    public void insert() {
        MyModel instance = new MyModel();
        instance.setPersistedAt(Instant.now());
        instance.setName("coucou");

        Query query = dialect.insert(metadata, instance);

        assertThat(query.getCommand(), equalTo(
        "INSERT INTO `my_custom_model`(`id`,`persisted_at`,`my_custom_name`,`deleted_at`,`created_at`) " +
        "VALUES (?,?,?,?,?);"));
    }

    @Test
    public void delete() {
        MyModel instance = new MyModel();
        instance.setId(1);

        Query query = dialect.delete(metadata, instance);

        assertThat(query.getCommand(), equalTo("DELETE FROM `my_custom_model` WHERE `id`=?;"));
    }

    @Test
    public void update() {
        MyModel instance = new MyModel();
        instance.setPersistedAt(Instant.now());
        instance.setName("coucou");

        Query query = dialect.update(metadata, instance);

        assertThat(query.getCommand(), equalTo(
        "UPDATE `my_custom_model` SET " +
                "`persisted_at`=?," +
                "`my_custom_name`=?," +
                "`deleted_at`=?," +
                "`created_at`=?" +
        " WHERE `id`=?;"));
    }
}

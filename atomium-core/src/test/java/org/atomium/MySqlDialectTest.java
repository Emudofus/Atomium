package org.atomium;

import org.atomium.entity.EntityMetadata;
import org.atomium.persistence.Dialect;
import org.atomium.persistence.dialect.Dialects;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
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
        String query = dialect.select(metadata);

        assertTrue(query.equals(
        "SELECT `id`,`persisted_at`,`my_custom_name`,`deleted_at`,`created_at` FROM `my_custom_model`;"));
    }

    @Test
    public void insert() {
        MyModel instance = new MyModel();
        instance.setPersistedAt(Instant.now());
        instance.setName("coucou");
        String query = dialect.insert(singleton(instance), metadata);

        assertThat(query, equalTo(
        "INSERT INTO `my_custom_model`(`id`,`persisted_at`,`my_custom_name`,`deleted_at`,`created_at`) " +
        "VALUES ('" + instance.getId() + "','" + instance.getPersistedAt() + "','" + instance.getName() + "',NULL,'" + instance.getCreatedAt() + "');"));
    }

    @Test
    public void multipleInsert() {
        MyModel instance = new MyModel(), instance2 = new MyModel();
        instance.setId(1);
        instance.setPersistedAt(Instant.now());
        instance.setName("coucou");
        instance2.setId(2);
        instance2.setPersistedAt(Instant.now());
        instance2.setName("kikou");

        String query = dialect.insert(asList(instance, instance2), metadata);

        assertThat(query, equalTo(
        "INSERT INTO `my_custom_model`(`id`,`persisted_at`,`my_custom_name`,`deleted_at`,`created_at`) VALUES " +
        "('" + instance.getId() + "','" + instance.getPersistedAt() + "','" + instance.getName() + "',NULL,'" + instance.getCreatedAt() + "')," +
        "('" + instance2.getId() + "','" + instance2.getPersistedAt() + "','" + instance2.getName() + "',NULL,'" + instance2.getCreatedAt() + "');"));
    }

    @Test
    public void delete() {
        MyModel instance = new MyModel();
        instance.setId(1);

        String query = dialect.delete(singleton(instance), metadata);

        assertThat(query, equalTo("DELETE FROM `my_custom_model` WHERE `id`='1';"));
    }

    @Test
    public void multipleDelete() {
        MyModel instance = new MyModel(), instance2 = new MyModel();
        instance.setId(1);
        instance2.setId(2);

        String query = dialect.delete(asList(instance, instance2), metadata);

        assertThat(query, equalTo("DELETE FROM `my_custom_model` WHERE `id`='1' OR `id`='2';"));
    }

    @Test
    public void update() {
        MyModel instance = new MyModel();
        instance.setPersistedAt(Instant.now());
        instance.setName("coucou");
        String query = dialect.update(singleton(instance), metadata);

        assertThat(query, equalTo(
        "UPDATE `my_custom_model` SET " +
                "`persisted_at`='" + instance.getPersistedAt() + "'," +
                "`my_custom_name`='" + instance.getName() + "'," +
                "`deleted_at`=NULL," +
                "`created_at`='" + instance.getCreatedAt() + "'" +
        " WHERE `id`='" + instance.getId() + "';"));
    }

    @Test
    public void multipleUpdate() {
        MyModel instance = new MyModel(), instance2 = new MyModel();
        instance.setId(1);
        instance.setPersistedAt(Instant.now());
        instance.setName("coucou");
        instance2.setId(2);
        instance2.setPersistedAt(Instant.now());
        instance2.setName("kikou");

        String query = dialect.update(asList(instance, instance2), metadata);


        assertThat(query, equalTo(
        "UPDATE `my_custom_model` SET " +
                "`persisted_at`='" + instance.getPersistedAt() + "'," +
                "`my_custom_name`='" + instance.getName() + "'," +
                "`deleted_at`=NULL," +
                "`created_at`='" + instance.getCreatedAt() + "'" +
        " WHERE `id`='" + instance.getId() + "';" +

        "UPDATE `my_custom_model` SET " +
                "`persisted_at`='" + instance2.getPersistedAt() + "'," +
                "`my_custom_name`='" + instance2.getName() + "'," +
                "`deleted_at`=NULL," +
                "`created_at`='" + instance2.getCreatedAt() + "'" +
        " WHERE `id`='" + instance2.getId() + "';"));
    }
}

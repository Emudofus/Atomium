package org.atomium;

import org.atomium.dialects.DefaultSqlDialect;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Blackrush
 */
public class DefaultSqlDialectTest {
    @SuppressWarnings("UnusedDeclaration")
    static final class MyEntity {
        private int id;
        private String attr;

        @Column
        @PrimaryKey(autogenerated = true)
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Column
        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }
    }

    private static Connection connection;

    private Metadata<MyEntity> myEntity;
    private DefaultSqlDialect dialect;

    @BeforeClass
    public static void setUpConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    @AfterClass
    public static void tearDownConnection() throws Exception {
        connection.close();
    }

    @Before
    public void setUp() throws Exception {
        myEntity = new SimpleMetadataRegistry().register(MyEntity.class);
        dialect = new DefaultSqlDialect(JdbcDatabaseMetadata.fromJDBC(connection.getMetaData()));
    }

    @Test
    public void testMap() throws Exception {
        NamedValues values = NamedValues.of()
                .set("id", 1)
                .set("attr", "Hello World");

        MyEntity instance = dialect.map(myEntity, values);

        assertThat(instance.id, is(1));
        assertThat(instance.attr, is("Hello World"));
    }

    @Test
    public void testBuildStructure() throws Exception {
        SqlQuery query = dialect.buildStructure(myEntity);

        // use TEXT instead of VARCHAR(255) here because of SQLite
        assertThat(query.getCommand(), is("CREATE TABLE `myEntity`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `attr` TEXT NOT NULL);"));
        assertThat(query.getBoundValues().length(), is(0));
    }

    @Test
    public void testCreate() throws Exception {
        MyEntity entity = new MyEntity();
        entity.attr = "hello world";
        SqlQuery query = dialect.create(myEntity, entity);

        assertThat(query.getCommand(), is("INSERT INTO `myEntity`(`id`, `attr`) VALUES(:id, :attr);"));

        assertThat(query.getBoundValues().length(), is(2));
        assertThat(query.getBoundValue("attr"), is((Object) entity.attr));
        assertThat(query.getBoundValue("id"), nullValue());
    }

    @Test
    public void testRead() throws Exception {
        SqlQuery query = dialect.read(myEntity);
        assertThat(query.getCommand(), is("SELECT `id`, `attr` FROM `myEntity`;"));
        assertThat(query.getBoundValues().length(), is(0));
    }

    @Test
    public void testReadRef() throws Exception {
        Ref<MyEntity> ref = myEntity.getColumn("attr").getRef("lel");
        SqlQuery query = dialect.read(ref);

        assertThat(query.getCommand(), is("SELECT `id`, `attr` FROM `myEntity` WHERE `attr`=:attr;"));

        assertThat(query.getBoundValues().length(), is(1));
        assertThat(query.getBoundValue("attr"), is((Object) "lel"));
    }

    @Test
    public void testUpdate() throws Exception {
        SqlQuery query = dialect.update(myEntity, new MyEntity());

        assertThat(query.getCommand(), is("UPDATE `myEntity` SET `attr`=:attr WHERE `id`=:id;"));

        assertThat(query.getBoundValues().length(), is(2));
        assertThat(query.getBoundValue("attr"), nullValue());
        assertThat(query.getBoundValue("id"), is((Object) 0));
    }

    @Test
    public void testDelete() throws Exception {
        SqlQuery query = dialect.delete(myEntity, new MyEntity());

        assertThat(query.getCommand(), is("DELETE FROM `myEntity` WHERE `id`=:id;"));

        assertThat(query.getBoundValues().length(), is(1));
        assertThat(query.getBoundValue("id"), is((Object) 0));
    }

    @Test
    public void testDeleteRef() throws Exception {
        Ref<MyEntity> ref = myEntity.getColumn("attr").getRef("lel");
        SqlQuery query = dialect.delete(ref);

        assertThat(query.getCommand(), is("DELETE FROM `myEntity` WHERE `attr`=:attr;"));

        assertThat(query.getBoundValues().length(), is(1));
        assertThat(query.getBoundValue("attr"), is((Object) "lel"));
    }
}
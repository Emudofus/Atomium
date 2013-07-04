package org.atomium;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import org.atomium.annotations.Column;
import org.atomium.annotations.PrimaryKey;
import org.atomium.dialects.SqlDialects;
import org.atomium.metadata.Metadata;
import org.atomium.metadata.SimpleMetadataRegistry;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Blackrush
 */
public class JdbcSessionTest {
    private static Connection connection;
    private static JdbcDatabase database;
    private static Metadata<MyEntity> myEntity;
    private static int nextId;
    private JdbcSession session;

    @BeforeClass
    public static void setUpDatabase() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        database = JdbcDatabase.of(
                Suppliers.ofInstance(connection),
                SqlDialects.forDatabaseMetaData(connection.getMetaData()),
                new SimpleMetadataRegistry()
        );
        myEntity = database.getRegistry().register(MyEntity.class);

        try (Statement statement = connection.createStatement()) {
            statement.addBatch("CREATE TABLE myEntity(id INTEGER PRIMARY KEY AUTOINCREMENT, attr TEXT NOT NULL)");
            statement.addBatch("INSERT INTO myEntity(attr) VALUES('hello_world')");
            nextId++;
            statement.addBatch("INSERT INTO myEntity(attr) VALUES('bonjour_monde')");
            nextId++;
            statement.addBatch("INSERT INTO myEntity(attr) VALUES('hallo_welt')");
            nextId++;
            statement.addBatch("INSERT INTO myEntity(attr) VALUES('ciao_mondo')");
            nextId++;
            statement.executeBatch();
        }
    }

    @AfterClass
    public static void tearDownDatabase() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        session = database.createSession();
    }

    @After
    public void tearDown() throws Exception {
        //session.close();
    }

    @Test
    public void testFindOne() throws Exception {
        Ref<MyEntity> ref = database.ref(MyEntity.class, 1);
        MyEntity instance = session.findOne(ref);

        assertThat(instance.id, is(1));
        assertThat(instance.attr, is("hello_world"));
    }

    @Test
    public void testFind() throws Exception {
        Attribute<MyEntity, Comparable> attribute = myEntity.getPrimaryKey().asAttribute();
        Query<MyEntity> query = QueryFactory.greaterThanOrEqualTo(attribute, 3);
        Set<MyEntity> result = session.find(myEntity, query);

        List<Boolean> tests = Lists.newArrayList();
        for (MyEntity instance : result) {
            if (instance.id == 3 && instance.attr.equals("hallo_welt")) {
                tests.add(true);
            }
            if (instance.id == 4 && instance.attr.equals("ciao_mondo")) {
                tests.add(true);
            }
        }
        assertThat(tests.size(), is(2));
    }

    @Test
    public void testAll() throws Exception {
        Set<MyEntity> result = session.all(myEntity);
        assertThat(result.size(), is(4));
    }

    @Test
    public void testPersistCreate() throws Exception {
        MyEntity instance = new MyEntity();
        instance.attr = "hello_world TEST";
        nextId++;

        assertThat(myEntity.isPersisted(instance), is(false));

        session.persist(instance);
        assertThat(myEntity.isPersisted(instance), is(true));
        assertThat(instance.id, is(nextId));

        try (Statement statement = session.getConnection().createStatement()) {
            String query = "SELECT COUNT(id) AS count FROM myEntity WHERE attr='hello_world TEST'";
            try (ResultSet rset = statement.executeQuery(query)) {
                assertThat(rset.next(), is(true));
                assertThat(rset.getInt("count"), is(1));
            }
        }
    }

    @Test
    public void testPersistUpdate() throws Exception {
        MyEntity instance = session.findOne(myEntity.getPrimaryKey().getRef(2));
        instance.attr = "bonjour_monde TEST";

        assertThat(myEntity.isPersisted(instance), is(true));

        session.persist(instance);
        assertThat(myEntity.isPersisted(instance), is(true));

        try (Statement statement = session.getConnection().createStatement()) {
            String query = "SELECT COUNT(id) AS count FROM myEntity WHERE attr='bonjour_monde TEST'";
            try (ResultSet rset = statement.executeQuery(query)) {
                assertThat(rset.next(), is(true));
                assertThat(rset.getInt("count"), is(1));
            }
        }
    }

    @Test
    public void testRemove() throws Exception {
        MyEntity instance = session.findOne(myEntity.getPrimaryKey().getRef(nextId));

        assertThat(myEntity.isPersisted(instance), is(true));
        session.remove(instance);
        assertThat(myEntity.isPersisted(instance), is(false));

        try (Statement statement = session.getConnection().createStatement()) {
            String query = "SELECT COUNT(id) AS count FROM myEntity WHERE id=" + nextId;
            try (ResultSet rset = statement.executeQuery(query)) {
                assertThat(rset.next(), is(true));
                assertThat(rset.getInt("count"), is(0));
            }
        }
    }

    @Test
    public void testRemoveRef() throws Exception {
        boolean result;

        result = session.remove(myEntity.getPrimaryKey().getRef(0xCAFEBABE));
        assertThat(result, is(false));

        result = session.remove(myEntity.getPrimaryKey().getRef(1));
        assertThat(result, is(true));
    }

    public static class MyEntity {
        @Column
        @PrimaryKey(autogenerated = true)
        private int id;

        @Column
        private String attr;
    }
}

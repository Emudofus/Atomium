package org.atomium;

import org.atomium.annotations.Column;
import org.atomium.annotations.PrimaryKey;
import org.atomium.criterias.CriteriaInterface;
import org.atomium.criterias.Criterias;
import org.atomium.dialects.SqlDialects;
import org.atomium.metadata.SimpleMetadataRegistry;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Blackrush
 */
public class JdbcDatabaseTest {
    private static Connection connection;
    private static int nextId;

    @BeforeClass
    public static void setUpTests() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        Statement statement = connection.createStatement();
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

    @AfterClass
    public static void tearDownTests() throws Exception {
        connection.close();
    }

    private JdbcDatabase db;

    @Before
    public void setUp() throws Exception {
        db = JdbcDatabase.of(
                connection,
                SqlDialects.forDatabaseMetaData(connection.getMetaData()),
                new SimpleMetadataRegistry()
        );
        db.getRegistry().register(MyEntity.class);
    }

    @After
    public void tearDown() throws Exception {
        // don't 'db.close();' because we already do it in '@AfterClass void tearDownTests()'
    }

    @Test
    public void testRefPkey() throws Exception {
        Ref<MyEntity> ref = db.ref(MyEntity.class, 1);
        assertThat(ref.getColumn().isPrimaryKey(), is(true));
        assertThat(ref.getIdentifier(), is((Object) 1));
    }

    @Test
    public void testRefColumn() throws Exception {
        Ref<MyEntity> ref = db.ref(MyEntity.class, "attr", "wrong attr lel");
        assertThat(ref.getColumn().getName(), is("attr"));
        assertThat(ref.getIdentifier(), is((Object) "wrong attr lel"));
    }

    @Test
    public void testFindOneByRef() throws Exception {
        MyEntity instance;

        instance = db.findOne(db.ref(MyEntity.class, 1));
        assertThat(instance.getAttr(), is((Object) "hello_world"));

        instance = db.findOne(db.ref(MyEntity.class, 2));
        assertThat(instance.getAttr(), is((Object) "bonjour_monde"));
    }

    @Test
    public void testFindOneByPkey() throws Exception {
        MyEntity instance;

        instance = db.findOne(MyEntity.class, 1);
        assertThat(instance.getAttr(), is((Object) "hello_world"));

        instance = db.findOne(MyEntity.class, 2);
        assertThat(instance.getAttr(), is((Object) "bonjour_monde"));
    }

    @Test
    public void testFindOneByColumn() throws Exception {
        MyEntity instance;

        instance = db.findOne(MyEntity.class, "id", 1);
        assertThat(instance.getAttr(), is((Object) "hello_world"));

        instance = db.findOne(MyEntity.class, "id", 2);
        assertThat(instance.getAttr(), is((Object) "bonjour_monde"));
    }

    @Test
    public void testAll() throws Exception {
        Set<MyEntity> instances = db.all(MyEntity.class);

        boolean first = false, second = false;
        for (MyEntity instance : instances) {
            if (!first) first = instance.getId() == 1;
            if (!second) second = instance.getId() == 2;
        }
        assertTrue(first);
        assertTrue(second);
    }

    @Test
    public void testFind() throws Exception {
        CriteriaInterface criteria = Criterias.equal(Criterias.identifier("attr"), Criterias.value("hello_world"));
        Set<MyEntity> instances = db.find(MyEntity.class, criteria);

        assertThat(instances.size(), is(1));

        MyEntity instance = instances.iterator().next(); // get first
        assertThat(instance.getId(), is(1));
        assertThat(instance.getAttr(), is("hello_world"));
    }

    @Test
    public void testFindColumn() throws Exception {
        Set<MyEntity> instances = db.find(MyEntity.class, "attr", "hello_world");

        assertThat(instances.size(), is(1));

        MyEntity instance = instances.iterator().next();
        assertThat(instance.getId(), is(1));
        assertThat(instance.getAttr(), is("hello_world"));
    }

    @Test
    public void testPersist() throws Exception {
        MyEntity instance = new MyEntity();
        instance.setAttr("hola_mundo");
        db.persist(instance);
        assertThat(instance.getId(), is(++nextId));

        instance.setAttr("hola_mundo EDITED LEL");
        db.persist(instance);

        ResultSet rset = connection.createStatement().executeQuery("SELECT attr FROM myEntity WHERE id=" + nextId);
        rset.next();
        assertThat(rset.getString("attr"), is("hola_mundo EDITED LEL"));
    }

    @Test
    public void testRemove() throws Exception {
        ResultSet rset = connection.createStatement().executeQuery("SELECT COUNT(id) FROM myEntity WHERE id=3");
        rset.next();
        assertThat(rset.getInt(1), is(1));

        MyEntity instance = new MyEntity();
        instance.setId(3);

        db.remove(instance);

        rset = connection.createStatement().executeQuery("SELECT COUNT(id) FROM myEntity WHERE id=3");
        rset.next();
        assertThat(rset.getInt(1), is(0));
    }

    @Test
    public void testRemoveRef() throws Exception {
        Ref<MyEntity> ref = db.ref(MyEntity.class, 4);
        assertTrue(db.remove(ref));
    }

    public static class MyEntity {
        @Column
        @PrimaryKey(autogenerated = true)
        private int id;

        @Column
        private String attr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }
    }
}

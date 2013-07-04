Atomium [![build_status_img]][build_status]
=======

*A simple ORM for your Java projects*

## How to use

You can download the generated artifacts [here][downloads], compile yourself or use Maven dependency system.

#### Compile the artifacts

```bash
$ git clone https://github.com/Blackrush/Atomium.git
$ cd Atomium/
$ mvn clean install
$ mkdir build
$ mv ./atomium-*/target/atomium-*-*.jar ./build
```

You can find the artifacts inside the `build/` folder.

#### Use Maven dependency system inside your project

You need first to clone the Git repository and install the artifacts :

```bash
$ git clone https://github.com/Blackrush/Atomium.git
$ cd Atomium/
$ mvn clean install
```

Then add the dependencies into your `pom.xml` :

```xml
<dependencies>
  <dependency>
    <groupId>org.atomium</groupId>
    <artifactId>atomium-core</artifactId>
    <version>INSERT LAST VERSION HERE</version>
  </dependency>
  <dependency>
    <groupId>org.atomium</groupId>
    <artifactId>atomium-jdbc</artifactId>
    <version>INSERT LAST VERSION HERE</version>
  </dependency>
</dependencies>
```

This method is temporary, I will as soon as I can deploy the artifacts on Maven Central Repository and you will
just have to add the dependencies without clone the repository anymore.

#### Use Atomium inside your project

Atomium is divided in two parts : the metadata and the database. The metadata part simply holds data of entities and
extracts or exports data to the database. The database part handles database's connection and the way the data has to
be sent to the database.

Here is a example that uses JDBC drivers :

```java
// the metadata part
MetadataRegistry registry = new SimpleMetadataRegistry();
registry.register(MyEntity.class);

// the database part
Class.forName(driverName);
try (DatabaseInterface database = JdbcDatabase.of("jdbc:url", "user", "password", registry)) {
    try (SessionInterface session = database.createSession()) {
        MyEntity instance = session.findOne(MyEntity.class, 1);
        instance.setAttr("Hello World");
        
        session.persist(instance);
    }
}
```

Actually, `DatabaseInterface` simply holds infos to create a connection to the database while `SessionInterface` is
truly the connection to the database and contains all CRUD operations. Furthermore, `SessionInterface` is responsible
of data's cache.

Atomium uses [CQEngine] to keep the data in memory and retrieve them very efficiently. The way it has been implemented
makes it at least 10 times more faster than a naive implementation (more informations on CQEngine [wiki][cqengine-wiki]).

#### Integration with Guice

From [Guice homepage][Guice] :
_Put simply, Guice alleviates the need for factories and the use of new in your Java code. Think of Guice's `@Inject`
as the new new. You will still need to write factories in some cases, but your code will not depend directly on them.
Your code will be easier to change, unit test and reuse in other contexts._

*todo*

[build_status_img]: https://drone.io/github.com/Blackrush/Atomium/status.png
[build_status]: https://drone.io/github.com/Blackrush/Atomium/latest
[downloads]: https://drone.io/github.com/Blackrush/Atomium/files
[CQEngine]: https://code.google.com/p/cqengine/
[cqengine-wiki]: https://code.google.com/p/cqengine/wiki/Benchmark
[Guice]: https://code.google.com/p/google-guice/

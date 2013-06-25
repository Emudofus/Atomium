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

[build_status_img]: https://drone.io/github.com/Blackrush/Atomium/status.png
[build_status]: https://drone.io/github.com/Blackrush/Atomium/latest
[downloads]: https://drone.io/github.com/Blackrush/Atomium/files

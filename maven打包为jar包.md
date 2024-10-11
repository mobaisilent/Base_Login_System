#### 方法一

使用`maven-jar-plugin`和`maven-dependency-plugin`

首先，`maven-jar-plugin`的作用是配置mainClass和指定classpath。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>libs/</classpathPrefix>
                <mainClass>
                    org.baeldung.executable.ExecutableMavenJar
                </mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

> **addClasspath**: 是否在manifest文件中添加classpath。默认为false。如果为true，则会在manifest文件中添加classpath，这样在启动的时候就不用再手动指定classpath了。如下所示，文件中增加了`Class-Path`一行

```
Manifest-Version: 1.0                                                                                                                                         
Archiver-Version: Plexus Archiver
Built-By: michealyang
Class-Path: libs/jetty-server-9.4.7.v20170914.jar lib/javax.servlet-api
 -3.1.0.jar libs/jetty-http-9.4.7.v20170914.jar 
Created-By: Apache Maven 3.3.9
Build-Jdk: 1.8.0_162-ea
Main-Class: com.michealyang.jetty.embeded.EmbeddedJettyServer
```

> **classpathPrefix**: classpath的前缀。如上面的manifest文件中，Class-Path的值中，每个jar包的前缀都是**libs/**。本质上，这个配置的值是所依赖jar包所在的文件夹。配置正确了才能找到依赖
> **mainClass**: 指定启动时的Main Class

其次，`maven-dependency-plugin`会把所依赖的jar包copy到指定目录

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-dependencies</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
            <configuration>
                <outputDirectory>
                    ${project.build.directory}/libs
                </outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**executions**中的配置都很重要，按照上面的配置来就行了。**outputDirectory**指定了要将所依赖的jar包copy到哪个目录。要与`maven-jar-plugin`中的**classpathPrefix**一致。

执行如下命令，即可打包：

> mvn package

打包结果是，自己写的Class在jar包中，所依赖的jar包在libs目录中:

> ├── embedded-jetty-1.0.0-SNAPSHOT.jar
> ├── lib
> │ ├── jetty-server-9.4.7.v20170914.jar
> │ ├── jetty-http-9.4.7.v20170914.jar

执行如下命令即可启动jar包：

> java -jar embedded-jetty-1.0.0-SNAPSHOT.jar

#### 方法二 (推荐)

使用`maven-assembly-plugin`

`maven-assembly-plugin`可以将所有的东西都打包到一个jar包中。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
            <configuration>
                <archive>
                <manifest>
                    <mainClass>
                        com.michealyang.jetty.embeded.EmbeddedJettyServer
                    </mainClass>
                </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </execution>
    </executions>
</plugin>
```

执行mvn package后，会在target文件夹下生成两个jar包，一个是不带依赖的jar包，一个是后缀有-dependencies带有依赖的jar包，如：

May 31 16:42 embedded-jetty-1.0.0-SNAPSHOT-jar-with-dependencies.jar
May 31 16:42 embedded-jetty-1.0.0-SNAPSHOT.jar

启动时，直接执行即可：

java -jar embedded-jetty-1.0.0-SNAPSHOT-jar-with-dependencies.jar

优点
所有的东西都打到一个jar包中，很方便
缺点
配置项少，不自由。

#### 方法三

使用`maven-shade-plugin`

跟`maven-assembly-plugin`类似，都可以将所有的东西都打包到一个jar包中。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <shadedArtifactAttached>true</shadedArtifactAttached>
                <transformers>
                    <transformer implementation=
                      "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.michealyang.jetty.embeded.EmbeddedJettyServer</mainClass>
                </transformer>
            </transformers>
        </configuration>
        </execution>
    </executions>
</plugin>
```

执行mvn package后，会在target文件夹下生成两个jar包，一个是不带依赖的jar包，一个是后缀有-shaded带有依赖的jar包，如：

May 31 16:53 embedded-jetty-1.0.0-SNAPSHOT-shaded.jar
May 31 16:53 embedded-jetty-1.0.0-SNAPSHOT.jar

启动时，直接执行即可：

java -jar embedded-jetty-1.0.0-SNAPSHOT-jar-with-shaded.jar

优点
功能同maven-assembly-plugin，但比前者强大
缺点
配置起来太麻烦。当你需要高级功能的时候，更是麻烦的不要不要的。

#### 方法四

使用`onejar-maven-plugin`

```xml
<plugin>
    <groupId>com.jolira</groupId>
    <artifactId>onejar-maven-plugin</artifactId>
    <executions>
        <execution>
            <configuration>
                <mainClass>org.baeldung.executable.
                  ExecutableMavenJar</mainClass>
                <attachToBuild>true</attachToBuild>
                <filename>
                  ${project.build.finalName}.${project.packaging}
                </filename>
            </configuration>
            <goals>
                <goal>one-jar</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

优点
clean delegation model, allows classes to be at the top-level of the One Jar, supports external jars and can support Native libraries
缺点
not actively supported since 2012

方法五：使用spring-boot-maven-plugin
能同时打可执行jar包和war包
This allows to package executable jar or war archives and run an application “in-place”.

需要maven版本不低于3.2
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
            <configuration>
                <classifier>spring-boot</classifier>
                <mainClass>
                  org.baeldung.executable.ExecutableMavenJar
                </mainClass>
            </configuration>
        </execution>
    </executions>
</plugin>
```

两个重点：

goal要写成repackage
classifier要写成spring-boot
优点
dependencies inside a jar file, you can run it in every accessible location, advanced control of packaging your artifact, with excluding dependencies from the jar file etc., packaging of war files as well

缺点
添加了一些不必要的Spring和Spring Boot依赖

#### 方法六

使用`tomcat7-maven-plugin`

可打包成一个web工程类型的jar包。其实是内嵌了一个tomcat在里面。

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.0</version>
    <executions>
        <execution>
            <id>tomcat-run</id>
            <goals>
                <goal>exec-war-only</goal>
            </goals>
            <phase>package</phase>
            <configuration>
                <path>/</path>
                <enableNaming>false</enableNaming>
                <finalName>webapp.jar</finalName>
                <charset>utf-8</charset>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**优点**
只有一个jar包
**缺点**
打包出的文件很大。因为里面内嵌了一个tomcat
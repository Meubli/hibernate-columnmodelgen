# What is it

Annotation processor for javax.persistence.Column annotation.

Generate a class ( {Entity}Columns_ ) at compile time which contains all columns names from @Column(name="column")

# How to use it

## Maven

add it to your pom.xml as dependency and annotationProcessor:
```xml
    <dependencies> 
        <dependency>
            <groupId>com.meubli</groupId>
            <artifactId>hibernate-columnmodelgen</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>com.meubli</groupId>
                            <artifactId>hibernate-columnmodelgen</artifactId>
                            <version>1.0-SNAPSHOT</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

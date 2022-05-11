# Mybatis Generator Maven Plugin Extension

## Restriction

* You've already used official plugin in the target project will use this extension.
* Property targetRuntime of official plugin must be `MyBatis3`.

## Getting Started


1. Add plugin dependencies into pom.xml of your project.

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.4.1</version>
            <configuration>
                <configurationFile>
                    ${Your mybatis generator's configuration file}
                </configurationFile>
                <overwrite>true</overwrite>
                <verbose>true</verbose>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.5</version>
                </dependency>
                <dependency>
                    <groupId>com.higlowx</groupId>
                    <artifactId>mybatis-generator-maven-plugin-extension</artifactId>
                    <version>0.1.1-1.4.1</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

2. Configure your mybatis generator's configuration file, add `<plugin>` under `<context>` like using official plugin.

```xml

<context id="default" targetRuntime="MyBatis3">
    <plugin type="com.higlowx.mybatis.generator.plugin.SelectSelectivePlugin"/>
    <plugin type="com.higlowx.mybatis.generator.plugin.ModelColumnPlugin"/>
    <plugin type="com.higlowx.mybatis.generator.plugin.SelectOneByExamplePlugin"/>
</
```

3. Run mybatis-generator:generate command.

If you configured all 3 plugins, method `selectByExampleSelective()`, `selectOneByExample()`, `selectOneByExampleSelective()`, `selectByPrimaryKey()` will be generated in java client and sql mapper.

4. Test generated client and sql mapper.


## Change Log

Usually, [Releases Page](https://github.com/higlowx/mybatis-generator-maven-plugin-extension/releases) reports main changes.
You can also find more specific them in [Change Log](docs/CHANGELOG.md).

## Acknowledging

This project derived from [mybatis-generator-plugin](https://github.com/itfsw/mybatis-generator-plugin) supported for official maven plugin before v1.3.7. Unfortunately, for official newer version it's almost stagnant.
I create this project therefore.




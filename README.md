# webjars-maven-plugin

Makes [WebJars](http://webjars.org) super-easy to find and install from the command line.

## Prerequisites

Requires Java 5 and Maven 3.0.4.

## Invocation

To inovke the plugin by simply typing `mvn webjars:<goal>`, you must either add the plugin to your project, or add it to settings.xml.

In your project's POM, add the plugin to the `plugins` element:

````xml
<plugin>
  <groupId>org.webjars</groupId>
  <artifactId>webjars-maven-plugin</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</plugin>
````

In settings.xml, add the following line to the `pluginGroups` element ([more info](http://maven.apache.org/settings.html#Plugin_Groups)):

````xml
<pluginGroup>org.webjars</pluginGroup>
````

If you do neither, you must use `mvn org.webjars:webjars-maven-plugin:<goal>`.

## Goals

### list

Lists all available WebJars. Can be used from any directory.

The optional `webjar` parameter filters the results.

`mvn webjars:list`

`mvn webjars:list -Dwebjar=jquery`

### install

Adds a webjar to your dependencies.

The required `webjar` parameter specifies which WebJar to install. The format is: `<name>[:<version]`. If there is no version, the latest version is used.

`mvn webjars:install -Dwebjar=jquery`

`mvn webjars:install -Dwebjar=jquery:1.10.1`

## License

webjars-maven-plugin is licensed under the Apache v2.0 License.


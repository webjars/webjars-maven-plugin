# webjars-maven-plugin

Makes [WebJars](http://webjars.org) super-easy to find and install from the command line.

## Prerequisites

Requires Java 5 and Maven 3.0.4.

## Installation

### Native

Download the [latest release](https://github.com/webjars/webjars-maven-plugin/releases). Unpack it somewhere and add the bin folder to your path. You can now run `webjars list jquery`.

### Maven

To invoke the plugin with the `mvn webjars:<goal>` shortcut, you must either add the plugin to Maven's settings.xml or to your project's pom.xml. If you do neither, you must use `mvn org.webjars:webjars-maven-plugin:<goal>`.

#### Global

In settings.xml (in $USER_HOME/.m2 by default), add the following line to the `pluginGroups` element ([more info](http://maven.apache.org/settings.html#Plugin_Groups)):

````xml
<pluginGroup>org.webjars</pluginGroup>
````

The shortcut is now available globally.

#### Project-specific

In your project's POM, add the plugin to the `plugins` element:

````xml
<plugin>
  <groupId>org.webjars</groupId>
  <artifactId>webjars-maven-plugin</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</plugin>
````

The shortcut is now available in the project's directory

## Goals

### list

Lists all available WebJars. Can be used from any directory.

The optional `webjar` parameter filters the results.

`webjars list` or `mvn webjars:list`

`webjars list jquery` or `mvn webjars:list -Dwebjar=jquery`

### install

Adds a webjar to your dependencies.

The required `webjar` parameter specifies which WebJar to install. The format is: `<name>[:<version]`. If there is no version, the latest version is used.

`webjars install jquery` or `mvn webjars:install -Dwebjar=jquery`

`webjars install jquery:1.10.1` or `mvn webjars:install -Dwebjar=jquery:1.10.1`

## License

webjars-maven-plugin is licensed under the Apache v2.0 License.


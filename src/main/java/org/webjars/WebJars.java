package org.webjars;

import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class WebJars {

  private final Log log;

  public WebJars(Log log) {
    this.log = log;
  }

  public Multimap<String, ArtifactVersion> list(final String webjar) {
    Multimap<String, ArtifactVersion> artifacts = MavenCentral.getArtifacts(null, null, log);

    artifacts = Multimaps.filterEntries(artifacts, new Predicate<Map.Entry<String, ArtifactVersion>>() {
      public boolean apply(Map.Entry<String, ArtifactVersion> entry) {
        return webjar == null || entry.getKey().contains(webjar);
      }
    });

    if (artifacts.isEmpty()) {
      MavenCentral.reportNoWebJarsFound(webjar, null, log);
      return artifacts;
    }

    StringBuilder sb = new StringBuilder("Found the following artifacts in Maven Central:\n");
    for (String artifact : artifacts.keySet()) {
      if (webjar == null || artifact.contains(webjar)) {
        sb.append(artifact).append(" [");
        for (ArtifactVersion version : artifacts.get(artifact)) {
          sb.append(" ").append(version).append(" ");
        }
        sb.append("]\n");
      }
    }

    log.info(sb);

    return artifacts;
  }

  public void install(String webJar) {
    CommandLine commandLine = CommandLine.parse("mvn org.webjars:webjars-maven-plugin:install -Dwebjar=" + webJar);
    try {
      new DefaultExecutor().execute(commandLine);
    } catch (Exception e) {
      log.error("Could not install " + webJar, e);
    }
  }

  public void help() {
    log.info("Available commands:");
    log.info("list <webJar>\n\tLists all available WebJars.");
    log.info("\tCan be used from any directory.");
    log.info("\tThe optional parameter filters the results.");
    log.info("install <webJar>\n\tAdds the given WebJar to the project's dependencies or updates an existing dependency.");
    log.info("\tMust be used from a Maven project directory.");
    log.info("\tThe required parameter specifies which WebJar to install.");
    log.info("\tThe format is: <name>[<version>]. If there is no version, the latest version is used.");
  }
}

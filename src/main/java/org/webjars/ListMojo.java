package org.webjars;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Map;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Lists all available WebJars.
 */
@Mojo(name="list", requiresProject=false, requiresDirectInvocation=true)
public class ListMojo extends AbstractMojo {

  /**
   * Only list the WebJars that contain this string.
   */
  @Parameter(property="webjar")
  private String webjar;

  public void execute() throws MojoExecutionException, MojoFailureException {
    Multimap<String, ArtifactVersion> artifacts = MavenCentral.getArtifacts(null, null, getLog());

    artifacts = Multimaps.filterEntries(artifacts, new Predicate<Map.Entry<String, ArtifactVersion>>() {
      public boolean apply(Map.Entry<String, ArtifactVersion> entry) {
        return webjar == null || entry.getKey().contains(webjar);
      }
    });

    if (artifacts.isEmpty()) {
      MavenCentral.reportNoWebJarsFound(webjar, null, getLog());
      return;
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

    getLog().info(sb);
  }
}

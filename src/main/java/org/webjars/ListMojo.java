package org.webjars;

import com.google.common.collect.Multimap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
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

  public void execute() throws MojoExecutionException {
    Multimap<String, String> artifacts = MavenCentral.getArtifacts(null, null);

    for (String artifact : artifacts.keySet()) {
      if (webjar == null || artifact.contains(webjar)) {
        StringBuilder sb = new StringBuilder("[");
        for (String version : artifacts.get(artifact)) {
          sb.append(" ").append(version).append(" ");
        }
        sb.append("]");
        getLog().info(artifact + " " + sb);
      }
    }
  }
}

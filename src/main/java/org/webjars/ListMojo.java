package org.webjars;

import com.google.common.collect.Multimap;

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
    WebJars webJars = new WebJars(getLog());
    Multimap<String, ArtifactVersion> artifacts = webJars.list(webjar);

    if (artifacts.isEmpty()) {
      throw new MojoFailureException("No results.");
    }
  }
}

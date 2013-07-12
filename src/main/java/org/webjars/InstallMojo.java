package org.webjars;

import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Adds the specified WebJar as a dependency
 */
@Mojo(name="install", requiresProject=true, requiresDirectInvocation=true)
public class InstallMojo extends AbstractMojo {

  /**
   * The name of the WebJar, optionally suffixed with a version, eg. jquery:2.0.1
   *
   * If the version is omitted, the latest version is taken.
   */
  @Parameter(property="webjar", required=true)
  private String webjar;

  @Component
  private MavenProject project;

  @Component
  private ModelWriter modelWriter;

  public void execute() throws MojoExecutionException, MojoFailureException {
    String[] split = webjar.split(":");
    String artifact = split[0];
    ArtifactVersion requestedVersion = split.length >= 2 ? new DefaultArtifactVersion(split[1]) : null;

    Collection<ArtifactVersion> versions = MavenCentral.getArtifacts(artifact, requestedVersion, getLog()).get(artifact);

    ArtifactVersion resolvedVersion = null;

    if (requestedVersion == null) {
      resolvedVersion = versions.iterator().next();
    } else {
      for (ArtifactVersion artifactVersion : versions) {
        if (artifactVersion.equals(requestedVersion)) {
          resolvedVersion = artifactVersion;
          break;
        }
      }
    }

    Dependency dependency = new Dependency();
    dependency.setGroupId("org.webjars");
    dependency.setArtifactId(artifact);
    dependency.setVersion(resolvedVersion.toString());

    project.getOriginalModel().addDependency(dependency);
    try {
      modelWriter.write(project.getFile(), null, project.getOriginalModel());
    } catch (IOException e) {
      throw new MojoExecutionException("Could not add dependency", e);
    }

    getLog().info("Added dependency: " + artifact + ":" + resolvedVersion);
  }

}

package org.webjars;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Multimap;

public class WebJarsTest {

  private final StringBuilder log = new StringBuilder();

  @Test
  public void should_put_exact_match_first() throws Exception {
    Multimap<String, ArtifactVersion> artifacts = new WebJars(getLogger()).list("bootstrap");

    Iterator<String> logIterator = Splitter.on('\n').split(log).iterator();
    logIterator.next();
    String logL = logIterator.next();
    Iterator<String> artifactsIterator = artifacts.keySet().iterator();
    artifactsIterator.next();
    String secondArtifact = artifactsIterator.next();

    assertEquals("bootstrap", secondArtifact);
    assertThat(logL, startsWith("bootstrap ["));
  }

  @Test
  public void should_sort_alphabetically_if_no_exact_match() throws Exception {
    Multimap<String, ArtifactVersion> artifacts = new WebJars(getLogger()).list("bootstra");

    Iterator<String> logIterator = Splitter.on('\n').split(log).iterator();
    logIterator.next();
    String logL = logIterator.next();
    Iterator<String> artifactsIterator = artifacts.keySet().iterator();
    String firstArtifact = artifactsIterator.next();
    String secondArtifact = artifactsIterator.next();

    assertEquals("angular-ui-bootstrap", firstArtifact);
    assertEquals("bootstrap", secondArtifact);
    assertThat(logL, startsWith("angular-ui-bootstrap ["));
  }

  private SystemStreamLog getLogger() {
    return new SystemStreamLog() {
      @Override
      public void info(CharSequence content) {
        log.append(content);
        super.info(content);
      }
    };
  }
}

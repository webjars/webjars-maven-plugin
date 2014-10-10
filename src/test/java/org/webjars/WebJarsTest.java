package org.webjars;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.Ordering;

/**
 * NOTE: Tests check the logs rather than the object model because the logs are what matter, as they are what the user sees.
 */
public class WebJarsTest {

  private final StringBuilder log = new StringBuilder();

  @Test
  public void should_put_exact_match_first() throws Exception {
    new WebJars(getLogger()).list("bootstrap");

    Iterator<String> logIterator = getLogs();
    String logs = logIterator.next();

    assertThat(logs, startsWith("bootstrap ["));
  }

  @Test
  public void should_sort_alphabetically_if_no_exact_match() throws Exception {
    new WebJars(getLogger()).list("bootstra");

    Iterator<String> logIterator = getLogs();
    logIterator.next();
    
    String previousLine = null;
    while (logIterator.hasNext()) {
      String line = logIterator.next();
      if (previousLine == null) {
        previousLine = line;
        continue;
      }
      
      Assert.assertTrue(Ordering.natural().compare(previousLine, line) < 0);
    }
  }

  @Test
  public void should_not_throw_npe_when_list_is_called_without_a_filter() {
    try {
      new WebJars(getLogger()).list(null);
    } catch (NullPointerException e) {
      fail();
    }
  }

  private Iterator<String> getLogs() {
    Iterator<String> logIterator = Splitter.on('\n').omitEmptyStrings().split(log).iterator();
    logIterator.next();
    return logIterator;
  }

  private SystemStreamLog getLogger() {
    return new SystemStreamLog() {
      @Override
      public void info(CharSequence content) {
        log.append(content);
//        super.info(content);
      }
    };
  }
}

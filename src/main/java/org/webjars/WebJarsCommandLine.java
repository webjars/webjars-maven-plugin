package org.webjars;

import org.apache.maven.plugin.logging.SystemStreamLog;

public class WebJarsCommandLine {

  public static void main(String[] args) {
    SystemStreamLog log = new SystemStreamLog();
    String command = args.length > 0 ? args[0] : "help";
    WebJars webJars = new WebJars(log);

    if ("list".equals(command)) {
      webJars.list(args.length > 1 ? args[1] : null);
    } else if ("install".equals(command)) {
      if (args.length < 2) {
        log.error("Please specify a WebJar to be installed");
        return;
      }
      webJars.install(args[1]);
    } else if ("help".equals(command)) {
      webJars.help();
    } else {
      log.error("Invalid command.");
      webJars.help();
    }
  }
}

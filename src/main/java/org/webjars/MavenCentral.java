package org.webjars;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Comparator;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

public class MavenCentral {

  public static Multimap<String, ArtifactVersion> getArtifacts(String artifact, ArtifactVersion version, Log log) throws MojoFailureException {
    String query = "g:\"org.webjars\"";
    if (artifact != null) {
      query += " AND a:\"" + artifact + "\"";
    }
    if (version != null) {
      query += " AND v:\"" + version + "\"";
    }

    HttpRequest req = HttpRequest.get("http://search.maven.org/solrsearch/select", true, "q", query, "core", "gav", "rows", 500, "wt", "json");
    JsonObject json = new Gson().fromJson(req.body(), JsonObject.class);

    JsonArray docs = json.getAsJsonObject("response").getAsJsonArray("docs");
    Multimap<String, ArtifactVersion> artifacts = TreeMultimap.create(Ordering.natural(), new Comparator<ArtifactVersion>() {
      public int compare(ArtifactVersion version1, ArtifactVersion version2) {
        return version2.compareTo(version1);
      }
    });

    for (JsonElement doc : docs) {
      JsonObject gav = doc.getAsJsonObject();
      String artifactId = gav.get("a").getAsString();

      if (artifactId.startsWith("webjars-")) {
        continue;
      }

      artifacts.put(artifactId, new DefaultArtifactVersion(gav.get("v").getAsString()));
    }

    if (artifacts.isEmpty()) {
      reportNoWebJarsFound(artifact, version, log);
    }

    return artifacts;
  }

  public static void reportNoWebJarsFound(String artifact, ArtifactVersion version, Log log) throws MojoFailureException {
    String errorMessage = "No WebJar found matching " + artifact + (version != null ? ":" + version : "");
    log.error(errorMessage);

    throw new MojoFailureException(errorMessage);
  }
}

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

public class MavenCentral {

  public static Multimap<String, String> getArtifacts(String artifact, String version) {
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
    Multimap<String, String> artifacts = TreeMultimap.create(Ordering.natural(), new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o2.compareTo(o1);
      }
    });

    for (JsonElement doc : docs) {
      JsonObject gav = doc.getAsJsonObject();
      String artifactId = gav.get("a").getAsString();

      if (artifactId.startsWith("webjars-")) {
        continue;
      }

      artifacts.put(artifactId, gav.get("v").getAsString());
    }

    return artifacts;
  }
}

package org.webjars;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    Multimap<String, String> artifacts = TreeMultimap.create();
    for (JsonElement doc : docs) {
      JsonObject gav = doc.getAsJsonObject();
      artifacts.put(gav.get("a").getAsString(), gav.get("v").getAsString());
    }

    return artifacts;
  }
}

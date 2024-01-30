package org.lyrasis.aspaceclient;

import org.json.JSONObject;

public class Result {

  private final JSONObject json;

  public Result(JSONObject json) {
    this.json = json;
  }

  public JSONObject getJson() {
    return json;
  }

  @Override
  public String toString() {
    return json.toString();
  }

}

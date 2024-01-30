package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Response;

public class ResponseIterator implements Iterator<List<Result>> {

  private boolean pending = true;
  private final Config config;
  private int lastPage;
  private int page;
  private int thisPage;
  private final Session session;
  private final String path;

  public ResponseIterator(Config config, String path, Session session) {
    this.config = config;
    this.page = 1;
    this.path = path;
    this.session = session;
  }

  public ResponseIterator(Config config, String path, int page, Session session) {
    this.config = config;
    this.page = page;
    this.path = path;
    this.session = session;
  }

  @Override
  public boolean hasNext() {
    return pending || (thisPage < lastPage);
  }

  @Override
  public List<Result> next() {
    if (!hasNext()) {
      throw new NoSuchElementException("Last page of the API response");
    }
    this.pending = false;
    List<Result> results = new ArrayList<>();

    HashMap<String, String> params = new HashMap<>();
    params.put("page", String.valueOf(page));
    try {
      Response response = new RequestService(config, path, params).execute(session);
      if (!response.isSuccessful() || response.body() == null)
        throw new IOException("Unexpected code " + response);

      JSONObject jo = new JSONObject(response.body().string());
      this.thisPage = Integer.parseInt(jo.get("this_page").toString());
      this.lastPage = Integer.parseInt(jo.get("last_page").toString());

      JSONArray ja = jo.getJSONArray("results");
      for (int i = 0; i < ja.length(); i++) {
        results.add(new Result(ja.getJSONObject(i)));
      }
    } catch (IOException e) {
      throw new NoSuchElementException(e.getMessage());
    }

    this.page++;
    return results;
  }

}

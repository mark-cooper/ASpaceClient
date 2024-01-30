package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import okhttp3.Response;

public class LoginService {
  private final Config config;

  public LoginService(Config config) {
    this.config = config;
  }

  public Response login(Session session) throws IOException {
    String path = String.format("users/%s/login", config.getUsername());
    HashMap<String, String> params = new HashMap<>();
    params.put("password", config.getPassword());

    Response response = new RequestService(config, path, "{}", params).execute(session);

    if (response.isSuccessful() && response.body() != null) {
      JSONObject jo = new JSONObject(response.body().string());
      session.setToken(jo.get("session").toString());
    } else {
      throw new IOException("Login failed: " + response);
    }

    return response;
  }
}

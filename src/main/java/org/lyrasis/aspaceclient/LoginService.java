package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.Response;

public class LoginService {
  private HttpUrl url;
  private String username;
  private String password;

  public LoginService(HttpUrl url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public Response login(Session session) throws IOException {
    String path = "users/" + username + "/login";
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("password", password);

    Response response = new RequestService(url, path, "{}", params).execute(session);

    if (response.isSuccessful()) {
      JSONObject jo = new JSONObject(response.body().string());
      session.setToken(jo.get("session").toString());
    } else {
      throw new IOException("Login failed: " + response);
    }

    return response;
  }
}

package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.HashMap;
import okhttp3.Response;

public class Client {

  private Config config;
  private Session session;

  public Client(Config config) {
    this.config = config;
    this.session = new Session();
  }

  public Response get(String path) throws IOException {
    return new RequestService(config.getUrl(), path).execute(session);
  }

  public Response get(String path, HashMap<String, String> params) throws IOException {
    return new RequestService(config.getUrl(), path, params).execute(session);
  }

  public Response get(String path, HashMap<String, String> params, HashMap<String, String> headers)
      throws IOException {
    return new RequestService(config.getUrl(), path, params, headers).execute(session);
  }

  public String getToken() {
    return session.getToken();
  }

  public Response login() throws IOException {
    return new LoginService(config.getUrl(), config.getUsername(), config.getPassword()).login(session);
  }

  public Response post(String path, String payload) throws IOException {
    return new RequestService(config.getUrl(), path, payload).execute(session);
  }

  public Response post(String path, String payload, HashMap<String, String> params) throws IOException {
    return new RequestService(config.getUrl(), path, payload, params).execute(session);
  }

  public Response post(String path, String payload, HashMap<String, String> params,
      HashMap<String, String> headers) throws IOException {
    return new RequestService(config.getUrl(), path, payload, params, headers).execute(session);
  }

  @Override
  public String toString() {
    return "Client [config=" + config.toString() + "]";
  }

}

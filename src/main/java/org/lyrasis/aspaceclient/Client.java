package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Response;

public class Client {

  private final Config config;
  private final Session session;

  public Client(Config config) {
    this.config = config;
    this.session = new Session();
  }

  public Session getSession() {
    return session;
  }

  public String getToken() {
    return session.getToken();
  }

  public Response login() throws IOException {
    return new LoginService(config).login(session);
  }

  public Response get(String path) throws IOException {
    return new RequestService(config, path).execute(session);
  }

  public Response get(String path, HashMap<String, String> params) throws IOException {
    return new RequestService(config, path, params).execute(session);
  }

  public Response get(String path, HashMap<String, String> params, HashMap<String, String> headers)
      throws IOException {
    return new RequestService(config, path, params, headers).execute(session);
  }

  public Iterator<List<Result>> page(String path) throws IOException {
    return new ResponseIterator(config, path, session);
  }

  public Response post(String path, String payload) throws IOException {
    return new RequestService(config, path, payload).execute(session);
  }

  public Response post(String path, String payload, HashMap<String, String> params)
      throws IOException {
    return new RequestService(config, path, payload, params).execute(session);
  }

  public Response post(String path, String payload, HashMap<String, String> params,
      HashMap<String, String> headers) throws IOException {
    return new RequestService(config, path, payload, params, headers).execute(session);
  }

  @Override
  public String toString() {
    return "Client [config=" + config.toString() + "]";
  }
}

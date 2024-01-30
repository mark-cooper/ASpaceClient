package org.lyrasis.aspaceclient;

import okhttp3.HttpUrl;

public class Config {

  // REST API endpoint: https://test.archivesspace.org/staff/api
  private final HttpUrl url;
  private final String username;
  private final String password;

  public Config(String url, String username, String password) {
    this.url = HttpUrl.parse(url);
    this.username = username;
    this.password = password;
  }

  public HttpUrl getUrl() {
    return url;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return "Configuration [password=\"REDACTED\", url=" + getUrl() + ", username=" + getUsername()
        + "]";
  }
}

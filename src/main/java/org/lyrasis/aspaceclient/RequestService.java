package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class RequestService {
  private final OkHttpClient client = new OkHttpClient();
  private final Config config;
  private String payload;
  private final String path;
  private Session session;
  private HashMap<String, String> headers = new HashMap<>();
  private HashMap<String, String> params = new HashMap<>();

  private static final MediaType MEDIA_TYPE_JSON =
      MediaType.parse("application/json; charset=utf-8");
  private static final MediaType MEDIA_TYPE_DEFAULT = MEDIA_TYPE_JSON;

  public RequestService(Config config, String path) {
    this.config = config;
    this.path = path;
  }

  public RequestService(Config config, String path, HashMap<String, String> params) {
    this.config = config;
    this.path = path;
    this.params = params;
  }

  public RequestService(Config config, String path, HashMap<String, String> params,
      HashMap<String, String> headers) {
    this.config = config;
    this.path = path;
    this.params = params;
    this.headers = headers;
  }

  public RequestService(Config config, String path, String payload) {
    this.config = config;
    this.path = path;
    this.payload = payload;
  }

  public RequestService(Config config, String path, String payload, HashMap<String, String> params) {
    this.config = config;
    this.path = path;
    this.payload = payload;
    this.params = params;
  }

  public RequestService(Config config, String path, String payload, HashMap<String, String> params,
      HashMap<String, String> headers) {
    this.config = config;
    this.path = path;
    this.payload = payload;
    this.params = params;
    this.headers = headers;
  }

  public Response execute(Session session) throws IOException {
    this.session = session;
    Request request;

    HttpUrl url = buildUrl(path, params);
    if (payload != null) {
      request = buildPayloadRequest(url, payload, headers);
    } else {
      request = buildRequest(url, headers);
    }
    return client.newCall(request).execute();
  }

  // PRIVATE METHODS

  private void appendHeaders(Request.Builder builder, HashMap<String, String> headers) {
    for (Map.Entry<String, String> me : headers.entrySet()) {
      builder.addHeader(me.getKey(), me.getValue());
    }

    if (session.getToken() != null) {
      builder.addHeader("X-ArchivesSpace-Session", session.getToken());
    }
    builder.addHeader("Connection", "close");
    builder.addHeader("Content-Type", MEDIA_TYPE_DEFAULT.toString());
  }

  private Request buildRequest(HttpUrl url, HashMap<String, String> headers) {
    Request.Builder builder = new Request.Builder().url(url);
    appendHeaders(builder, headers);
    return builder.build();
  }

  private Request buildPayloadRequest(HttpUrl url, String payload,
      HashMap<String, String> headers) {
    Request.Builder builder =
        new Request.Builder().url(url).post(RequestBody.create(payload, MEDIA_TYPE_DEFAULT));
    appendHeaders(builder, headers);
    return builder.build();
  }

  private HttpUrl buildUrl(String path, HashMap<String, String> params) {
    String scheme = config.getUrl().scheme();
    String host = config.getUrl().host();
    List<String> segments = config.getUrl().pathSegments();

    HttpUrl.Builder builder = new HttpUrl.Builder().scheme(scheme).host(host)
        .addPathSegment(String.join("/", segments)).addPathSegment(path);

    for (Map.Entry<String, String> me : params.entrySet()) {
      builder.addQueryParameter(me.getKey(), me.getValue());
    }

    return builder.build();
  }

}

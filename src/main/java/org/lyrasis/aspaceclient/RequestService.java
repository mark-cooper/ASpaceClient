package org.lyrasis.aspaceclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class RequestService {

  private HashMap<String, String> headers = new HashMap<String, String>();
  private HashMap<String, String> params = new HashMap<String, String>();
  private HttpUrl url;
  private OkHttpClient client = new OkHttpClient();
  private Request request;
  private String payload;
  private String path;
  private Session session;

  private static final MediaType MEDIA_TYPE_JSON =
      MediaType.parse("application/json; charset=utf-8");
  private static MediaType MEDIA_TYPE_DEFAULT = MEDIA_TYPE_JSON;

  public RequestService(HttpUrl url, String path) {
    this.url = url;
    this.path = path;
  }

  public RequestService(HttpUrl url, String path, HashMap<String, String> params) {
    this.url = url;
    this.path = path;
    this.params = params;
  }

  public RequestService(HttpUrl url, String path, HashMap<String, String> params,
      HashMap<String, String> headers) {
    this.url = url;
    this.path = path;
    this.params = params;
    this.headers = headers;
  }

  public RequestService(HttpUrl url, String path, String payload) {
    this.url = url;
    this.path = path;
    this.payload = payload;
  }

  public RequestService(HttpUrl url, String path, String payload, HashMap<String, String> params) {
    this.url = url;
    this.path = path;
    this.payload = payload;
    this.params = params;
  }

  public RequestService(HttpUrl url, String path, String payload, HashMap<String, String> params,
      HashMap<String, String> headers) {
    this.url = url;
    this.path = path;
    this.payload = payload;
    this.params = params;
    this.headers = headers;
  }

  public Response execute(Session session) throws IOException {
    this.session = session;

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
    HttpUrl.Builder builder = new HttpUrl.Builder().scheme(url.scheme()).host(url.host())
        .addPathSegment(String.join("/", url.pathSegments())).addPathSegment(path);

    for (Map.Entry<String, String> me : params.entrySet()) {
      builder.addQueryParameter(me.getKey(), me.getValue());
    }

    return builder.build();
  }

}

package org.lyrasis.aspaceclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;

import okhttp3.Response;

public class ClientTest {
  Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "admin");
  Client client = new Client(config);

  @Test
  public void testGet() {
    try {
      Response response = client.get("repositories");
      assertTrue(response.isSuccessful());
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Test
  public void testLogin() {
    try {
      Response response = client.login();
      assertTrue(response.isSuccessful());
      assertNotNull(client.getToken());
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Test
  public void testPost() {
    // TODO
  }

  @Test
  public void testToString() {
    // TODO
  }
}

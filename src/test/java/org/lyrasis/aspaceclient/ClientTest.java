package org.lyrasis.aspaceclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import okhttp3.Response;

public class ClientTest {
  @Test
  public void testGet() {
    try {
      Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "admin");
      Client client = new Client(config);

      Response response = client.get("repositories");
      assertTrue(response.isSuccessful());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetWithLoginToRestricted() {
    try {
      Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "admin");
      Client client = new Client(config);

      client.login();
      Response response = client.get("repositories/2/resources/2");
      assertTrue(response.isSuccessful());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testGetWithoutLoginToRestricted() {
    try {
      Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "admin");
      Client client = new Client(config);

      Response response = client.get("repositories/2/resources/1");
      assertFalse(response.isSuccessful());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testPage() {
    try {
      Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "admin");
      Client client = new Client(config);

      client.login();
      Iterator<List<Result>> pageIterator = client.page("repositories/2/accessions");
      while (pageIterator.hasNext()) {
        List<Result> results = pageIterator.next();
        assertFalse(results.isEmpty());
        for (Result result : results) {
          assertEquals("accession", result.getJson().get("jsonmodel_type").toString());
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testLogin() {
    try {
      Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "admin");
      Client client = new Client(config);

      Response response = client.login();
      assertTrue(response.isSuccessful());
      assertNotNull(client.getToken());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testLoginInvalidUrl() {
    Config config = new Config("https://test.archivesspace.org/staff", "admin", "admin");
    Client client = new Client(config);
    Exception exception = assertThrows(IOException.class, () -> {
      Response response = client.login();
      assertFalse(response.isSuccessful());
    });

    String expectedMessage = "Login failed";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLoginInvalidUsername() {
    Config config = new Config("https://test.archivesspace.org/staff/api", "abc123xyz", "admin");
    Client client = new Client(config);
    Exception exception = assertThrows(IOException.class, () -> {
      Response response = client.login();
      assertFalse(response.isSuccessful());
    });

    String expectedMessage = "Login failed";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testLoginInvalidPassword() {
    Config config = new Config("https://test.archivesspace.org/staff/api", "admin", "abc123xyz");
    Client client = new Client(config);
    Exception exception = assertThrows(IOException.class, () -> {
      Response response = client.login();
      assertFalse(response.isSuccessful());
    });

    String expectedMessage = "Login failed";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
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

package com.indatasights.demo.crawl.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class ExampleAPIClient {
  InputStream in;
  URL url;

  public ExampleAPIClient(URL url) {
    this.url = url;
  }

  public String request(Map<String, String> data) throws IOException {
    String query = MapToQueryString(data);
    StringBuilder resp = new StringBuilder();
    //System.out.println("url: " + url.toString() + query);
    in = new URL(url + query).openStream();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
      for (String line; (line = reader.readLine()) != null;) {
        resp.append(line);
      }
    }
    return resp.toString();
  }

  public String MapToQueryString(Map<String, String> data) {
    StringBuilder query = new StringBuilder();
    for (Map.Entry<String, String> ma : data.entrySet()) {
      try {
        if (!ma.getValue().isEmpty())
        query.append(ma.getKey() + "=" + URLEncoder.encode(ma.getValue(), "UTF-8") + "&");
      } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
      }
    }
    return query.toString().substring(0, query.length()-1);
  }
}

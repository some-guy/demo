package com.indatasights.demo.crawl.example;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import org.junit.Ignore;
import org.junit.Test;

public class ExampleAPIClientTest {
  
  
  @Ignore
  @Test
  public void testRequest() {
    try {
      URL url = new URL("http://demo.localdomain/get.php?");

      ExampleAPIClient test = new ExampleAPIClient(url);

      Map<String, String> data = new HashMap<>();
      data.put("vkey", "secret");
      data.put("cfg_maxrecs", "1");
      data.put("cfg_mc", "INDIV,HHLD,PINDIV");
      data.put("cfg_namefrq", "1");
      data.put("cfg_ploc", "1");
      data.put("cfg_negmc", "-DIST9");
      data.put("cfg_light", "1");
      data.put("cfg_output", "vstat,stats2,altdata,basic,email,b2bemail");
      data.put("prodids", "email,b2bemail,whois,finapp");
      data.put("d_last", "shannon");
      data.put("d_first", "mike");
      data.put("d_fulladdr", "13053 se 26th st #c204");
      data.put("d_state", "wa");
      data.put("d_zip", "98005");
      data.put("d_zip4", "4241");
      String a = test.request(data);

      System.out.println("JSONResults: " + a);

    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception");
    }
  }
}

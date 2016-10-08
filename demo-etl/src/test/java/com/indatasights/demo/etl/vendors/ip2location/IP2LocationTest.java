package com.indatasights.demo.etl.vendors.ip2location;

import org.junit.Test;

import com.indatasights.demo.etl.Translate;
import com.indatasights.demo.etl.Translator;
import com.indatasights.demo.etl.vendors.ip2location.DB24;


import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

public class IP2LocationTest {

  @Test 
  public void DB24TranslatorTest () {
    String rec = null;
    try {
      String vendor = "ip2location";
      String dataset = "db24";
      Translate xlate = new Translate(vendor, dataset);
      Translator xlater = new DB24();
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      Map<String,List<String>> results = xlater.xlate(xlate.parse(rec));
      //System.out.println("num records in results: "+results.get("records").size());
      //System.out.println("records: "+results.get("records"));
      assertEquals(1, results.get("records").size());
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      e.printStackTrace();
      fail("exception");
    }
  }
 
}

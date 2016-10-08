package com.indatasights.demo.etl.vendors.clgx;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.indatasights.demo.etl.Translator;

public class ExADC extends CLGX implements Translator {


  public ExADC() {

  }
  @Override
  public Map<String, List<String>> xlate(CSVRecord rec) throws IOException, InterruptedException {
    try {

      results.get("records").clear();
      results.get("counters").clear();
      
      results.get("records").add(
          rec.get("last")       + ":"
          + rec.get("first")    + ":" 
          + rec.get("fulladdr") + ":" 
          + rec.get("city")     + ":" 
          + rec.get("state")    + ":" 
          + rec.get("zip")      + ":"
          + rec.get("zip4"));
      

    } catch (Exception e) {
      LOG.info("exadc err: " + e.toString());
    }
    return results;
  }
}

package com.indatasights.demo.etl.vendors.clgx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.indatasights.demo.etl.Translator;

public class ExMLS implements Translator {
  StringBuilder sAddr;
  StringBuilder sAddr2;
  StringBuilder csz;
  StringBuilder mailAddr;
  StringBuilder mailAddr2;
  StringBuilder mailAddrcsz;
  boolean hasMailAddr;
  Map<String, List<String>> results;

  public ExMLS() {
    sAddr = new StringBuilder("");
    sAddr2 = new StringBuilder("");
    csz = new StringBuilder("");
    mailAddr = new StringBuilder("");
    mailAddr2 = new StringBuilder("");
    mailAddrcsz = new StringBuilder("");
    results = new HashMap<>();
    results.put("records", new ArrayList<String>());
    results.put("counters", new ArrayList<String>());
    hasMailAddr=false;
  }

  @Override
  public Map<String, List<String>> xlate (CSVRecord rec) throws IOException, InterruptedException {
    try {
      results.get("records").clear();
      results.get("counters").clear();
      hasMailAddr = false;
      mailAddr.setLength(0);
      if (!rec.get("AddressStreetNumber").isEmpty()) {
        hasMailAddr = true;
        mailAddr.append(( rec.get("AddressStreetNumber").replaceFirst("^0+", "") + " " +rec.get("AddressStreetDirPrefix") + " " + rec.get("AddressStreetDirSuffix")
        + " " + rec.get("AddressStreetName") + " " + rec.get("AddressStreetSuffix") ).replaceAll("^[ ]+", "")
            .replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));
      }
      mailAddr2.setLength(0);
      mailAddr2.append(rec.get("AddressUnitNumber").isEmpty() ? "" : ", #" + rec.get("AddressUnitNumber").replaceFirst("^0+", ""));

      mailAddrcsz.setLength(0);
      mailAddrcsz.append(
          rec.get("AddressCity") + ":" + rec.get("AddressStateOrProvince") + ":" + rec.get("AddressPostalCode") + ":" + rec.get("AddressPostalCodePlus4"));

        if (hasMailAddr) {
          results.get("records").add("" + ":" + "" + ":" + mailAddr
              + mailAddr2 + ":" + mailAddrcsz);
        }
        // LOG.info("own1: " + outKey.toString() + " " + outValue.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }
}

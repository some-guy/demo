package com.indatasights.demo.etl.vendors.clgx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.indatasights.demo.etl.Translator;

public class ExTax implements Translator {
  StringBuilder sAddr;
  StringBuilder sAddr2;
  StringBuilder csz;
  StringBuilder mailAddr;
  StringBuilder mailAddr2;
  StringBuilder mailAddrcsz;
  List<String> ctrs;
  List<String> records;
  Map<String, List<String>> results;

  public ExTax() {
    sAddr = new StringBuilder("");
    sAddr2 = new StringBuilder("");
    csz = new StringBuilder("");
    mailAddr = new StringBuilder("");
    mailAddr2 = new StringBuilder("");
    mailAddrcsz = new StringBuilder("");
    results = new HashMap<>();
    results.put("records", new ArrayList<String>());
    results.put("counters", new ArrayList<String>());
  }

  public Map<String, List<String>> xlate(CSVRecord rec) throws IOException, InterruptedException {
    try {
      results.get("records").clear();
      results.get("counters").clear();
      boolean hasMailAddr = false;
      mailAddr.setLength(0);
      if (!rec.get(56).isEmpty()) {
        hasMailAddr = true;
        mailAddr.append((rec.get(55) + " " + rec.get(56).replaceFirst("^0+", "") + " " + rec.get(57) + " " + rec.get(58)
            + " " + rec.get(59) + " " + rec.get(60) + " " + rec.get(61) + " " + rec.get(62)).replaceAll("^[ ]+", "")
                .replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));
      }
      mailAddr2.setLength(0);
      mailAddr2.append(rec.get(63).isEmpty() ? "" : ", #" + rec.get(63).replaceFirst("^0+", ""));

      mailAddrcsz.setLength(0);
      mailAddrcsz.append(
          rec.get(64) + ":" + rec.get(65) + ":" + rec.get(66).substring(0, 5) + ":" + rec.get(66).substring(5, 9));

      csz.setLength(0);
      csz.append(
          rec.get(41) + ":" + rec.get(42) + ":" + rec.get(43).substring(0, 5) + ":" + rec.get(43).substring(5, 9));

      sAddr.setLength(0);
      sAddr.append(rec.get(32) + " " + rec.get(33).replaceFirst("^0+", "") + " " + rec.get(34) + " " + rec.get(35) + " "
          + rec.get(36) + " " + rec.get(37) + " " + rec.get(38) + " " + rec.get(39));
      String addr1 = sAddr.toString().replaceAll("^[ ]+", "").replaceAll("[ ]+$", "").replaceAll("[  ]+", " ");

      sAddr2.setLength(0);
      sAddr2.append(rec.get(40).isEmpty() ? "" : ", #" + rec.get(40).replaceFirst("^0+", ""));
      String addr2 = sAddr2.toString();

      if (!rec.get(46).isEmpty()) {

        results.get("records").add(
            rec.get(46) + ":" + rec.get(47).replaceFirst("[ ][A-Z]$", "") + ":" + addr1 + addr2 + ":" + csz.toString());
        if (hasMailAddr) {
          results.get("records").add(rec.get(46) + ":" + rec.get(47).replaceFirst("[ ][A-Z]$", "") + ":" + mailAddr
              + mailAddr2 + ":" + mailAddrcsz);
        }
        // LOG.info("own1: " + outKey.toString() + " " + outValue.toString());

      }
      if (!rec.get(48).isEmpty()) {

        results.get("records").add(
            rec.get(48) + ":" + rec.get(49).replaceFirst("[ ][A-Z]$", "") + ":" + addr1 + addr2 + ":" + csz.toString());
        // LOG.info("own2: " + outKey.toString() + " " + outValue.toString());
        if (hasMailAddr) {

          results.get("records").add(rec.get(48) + ":" + rec.get(49).replaceFirst("[ ][A-Z]$", "") + ":" + mailAddr
              + mailAddr2 + ":" + mailAddrcsz.toString());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }
}

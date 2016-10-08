package com.indatasights.demo.etl.vendors.clgx;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.indatasights.demo.etl.Translator;

public class ICP2M extends CLGX implements Translator {
  StringBuilder propertyAddr;
  StringBuilder propertyAddr2;
  StringBuilder propertyAddrcsz;
  StringBuilder mailAddr;
  StringBuilder mailAddr2;
  StringBuilder mailAddrcsz;
  boolean hasMailAddr;
  boolean hasPropertyAddr;

  public ICP2M() {
    propertyAddr = new StringBuilder("");
    propertyAddr2 = new StringBuilder("");
    propertyAddrcsz = new StringBuilder("");

    mailAddr = new StringBuilder("");
    mailAddr2 = new StringBuilder("");
    mailAddrcsz = new StringBuilder("");

    hasMailAddr=false;
    hasPropertyAddr=false;
  }
  @Override
  public Map<String, List<String>> xlate(CSVRecord rec) throws IOException, InterruptedException {
    try {

      results.get("records").clear();
      results.get("counters").clear();
      hasMailAddr = false;
      hasPropertyAddr = false;
      propertyAddr.setLength(0);
      propertyAddr2.setLength(0);
      propertyAddrcsz.setLength(0);
      mailAddr.setLength(0);
      mailAddr2.setLength(0);
      mailAddrcsz.setLength(0);
      //if (rec.get("OWNER CORPORATE INDICATOR FLAG").equals("Y")) return results;
      if ( ! rec.get("Property Address Street").isEmpty()) {
        hasPropertyAddr = true;
        propertyAddr.append((
                rec.get("Property Address Street").replaceFirst("^0+", "")
            ).replaceAll("^[ ]+", "").replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));
      }
      propertyAddr2.append("");

      propertyAddrcsz.append(rec.get("Property Address City") + ":" + rec.get("Property Address State") + ":"
          + rec.get("Property Address Zip").substring(0, 5) + ":" + rec.get("Property Address Zip").substring(5, 9));

      if ( ! rec.get("Mailing Address Street").isEmpty()) {
        hasMailAddr = true;
        mailAddr.append((
             rec.get("Mailing Address Street").replaceFirst("^0+", "")
            ).replaceAll("^[ ]+", "")
                .replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));

        mailAddr2.append("");

        mailAddrcsz.append(rec.get("Mailing Address City") + ":" + rec.get("Mailing Address State") + ":"
            + rec.get("Mailing Address Zip").substring(0, 5) + ":" + rec.get("Mailing Address Zip").substring(5, 9));
      }
      
      if ( ! rec.get("Owner 1 Last Name").isEmpty()) {

        results.get("records").add(
                rec.get("Owner 1 Last Name") + ":"
                + rec.get("Owner 1 First Name").split(" ")[0] + ":" 
                + propertyAddr + propertyAddr2 + ":" + propertyAddrcsz
                );
        if (hasMailAddr) {
          results.get("records").add(
              rec.get("Owner 1 Last Name") + ":"
              + rec.get("Owner 1 First Name").split(" ")[0] + ":" 
              + mailAddr + mailAddr2 + ":" + mailAddrcsz);
        }

      }

      if (!rec.get("Owner 2 Last Name").isEmpty()) {

        results.get("records").add(
            rec.get("Owner 2 Last Name") + ":"
            + rec.get("Owner 2 First Name").split(" ")[0] + ":" 
            + propertyAddr + propertyAddr2 + ":" + propertyAddrcsz.toString());
        if (hasMailAddr) {
          results.get("records").add(
              rec.get("Owner 2 Last Name") + ":" 
              + rec.get("Owner 2 First Name").split(" ")[0] + ":" 
              + mailAddr + mailAddr2 + ":" + mailAddrcsz);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }
}

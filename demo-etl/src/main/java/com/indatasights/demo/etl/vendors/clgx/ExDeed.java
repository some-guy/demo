package com.indatasights.demo.etl.vendors.clgx;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.indatasights.demo.etl.Translator;

public class ExDeed extends CLGX implements Translator {
  StringBuilder propertyAddr;
  StringBuilder propertyAddr2;
  StringBuilder propertyAddrcsz;
  StringBuilder mailAddr;
  StringBuilder mailAddr2;
  StringBuilder mailAddrcsz;
  boolean hasMailAddr;
  boolean hasPropertyAddr;

  public ExDeed() {
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
      if ( ! rec.get("PROPERTY HOUSE NUMBER").isEmpty()) {
        hasPropertyAddr = true;
        propertyAddr.append((
            rec.get("PROPERTY HOUSE NUMBER PREFIX")
            + " " + rec.get("PROPERTY HOUSE NUMBER").replaceFirst("^0+", "")
            + " " + rec.get("PROPERTY HOUSE NUMBER SUFFIX")
            + " " + rec.get("PROPERTY DIRECTION")
            + " " + rec.get("PROPERTY STREET NAME")
            + " " + rec.get("PROPERTY MODE")
            + " " + rec.get("PROPERTY QUADRANT")
            ).replaceAll("^[ ]+", "").replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));
      }
      propertyAddr2.append(rec.get("PROPERTY APARTMENT UNIT NUMBER").isEmpty() ? ""
          : ", #" + rec.get("PROPERTY APARTMENT UNIT NUMBER").replaceFirst("^0+", ""));

      propertyAddrcsz.append(rec.get("PROPERTY CITY") + ":" + rec.get("PROPERTY STATE") + ":"
          + rec.get("PROPERTY ZIPCODE").substring(0, 5) + ":" + rec.get("PROPERTY ZIPCODE").substring(5, 9));

      if ( ! rec.get("MAIL HOUSE NUMBER").isEmpty()) {
        hasMailAddr = true;
        mailAddr.append((
            rec.get("MAIL HOUSE NUMBER PREFIX")
            + " " + rec.get("MAIL HOUSE NUMBER").replaceFirst("^0+", "")
            + " " + rec.get("MAIL HOUSE NUMBER SUFFIX")
            + " " + rec.get("MAIL STREET DIRECTION")
            + " " + rec.get("MAIL STREET NAME")
            + " " + rec.get("MAIL MODE")
            + " " + rec.get("MAIL QUADRANT")
            ).replaceAll("^[ ]+", "")
                .replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));

        mailAddr2.append(rec.get("MAIL APARTMENT UNIT NUMBER").isEmpty() ? "" : ", #" + rec.get("MAIL APARTMENT UNIT NUMBER").replaceFirst("^0+", ""));

        mailAddrcsz.append(rec.get("MAIL CITY") + ":" + rec.get("MAIL STATE") + ":"
            + rec.get("MAIL ZIPCODE").substring(0, 5) + ":" + rec.get("MAIL ZIPCODE").substring(5, 9));
      }
      
      if ( ! rec.get("OWNER BUYER 1 LAST NAME").isEmpty()) {

        results.get("records").add(
                rec.get("OWNER BUYER 1 LAST NAME") + ":"
                + rec.get("OWNER BUYER 1 FIRST NAME & M I").split(" ")[0] + ":" 
                + propertyAddr + propertyAddr2 + ":" + propertyAddrcsz
                );
        if (hasMailAddr) {
          results.get("records").add(
              rec.get("OWNER BUYER 1 LAST NAME") + ":"
              + rec.get("OWNER BUYER 1 FIRST NAME & M I").split(" ")[0] + ":" 
              + mailAddr + mailAddr2 + ":" + mailAddrcsz);
        }

      }

      if (!rec.get("OWNER BUYER 2 LAST NAME").isEmpty()) {

        results.get("records").add(
            rec.get("OWNER BUYER 2 LAST NAME") + ":"
            + rec.get("OWNER BUYER 2 FIRST NAME & M I").split(" ")[0] + ":" 
            + propertyAddr + propertyAddr2 + ":" + propertyAddrcsz.toString());
        if (hasMailAddr) {
          results.get("records").add(
              rec.get("OWNER BUYER 2 LAST NAME") + ":" 
              + rec.get("OWNER BUYER 2 FIRST NAME & M I").split(" ")[0] + ":" 
              + mailAddr + mailAddr2 + ":" + mailAddrcsz);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }
}

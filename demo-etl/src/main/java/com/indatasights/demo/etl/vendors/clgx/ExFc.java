package com.indatasights.demo.etl.vendors.clgx;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.indatasights.demo.etl.Translator;

public class ExFc extends CLGX implements Translator {
  StringBuilder propertyAddr;
  StringBuilder propertyAddr2;
  StringBuilder propertyAddrcsz;
  StringBuilder mailAddr;
  StringBuilder mailAddr2;
  StringBuilder mailAddrcsz;
  boolean hasMailAddr;
  boolean hasPropertyAddr;

  public ExFc() {
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
      
      if ( ! rec.get("SITUS_HOUSE_NUMBER_1").isEmpty()) {
        hasPropertyAddr = true;
        propertyAddr.append((
            rec.get("SITUS_HOUSE_NUMBER_PREFIX_1")
            + " " + rec.get("SITUS_HOUSE_NUMBER_1").replaceFirst("^0+", "")
            + " " + rec.get("SITUS_HOUSE_NUMBER_SUFFIX_1")
            + " " + rec.get("SITUS_DIRECTION_1")
            + " " + rec.get("SITUS_STREET_NAME_1")
            + " " + rec.get("SITUS_MODE_1")
            + " " + rec.get("SITUS_QUADRANT_1")
            ).replaceAll("^[ ]+", "").replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));
      }
      propertyAddr2.append(rec.get("SITUS_APARTMENT_UNIT_1").isEmpty() ? ""
          : ", #" + rec.get("SITUS_APARTMENT_UNIT_1").replaceFirst("^0+", ""));

      propertyAddrcsz.append(rec.get("PROPERTY_CITY_1") + ":" + rec.get("PROPERTY_STATE_1") + ":"
          + rec.get("PROPERTY_ADDRESS_ZIP_CODE_1").substring(0, 5) + ":" + rec.get("PROPERTY_ADDRESS_ZIP_CODE_1").substring(5, 9));

      if ( ! rec.get("MAILING_HOUSE_NUMBER_2").isEmpty()) {
        hasMailAddr = true;
        mailAddr.append((
            rec.get("MAILING_HOUSE_NUMBER_PREFIX_2")
            + " " + rec.get("MAILING_HOUSE_NUMBER_2").replaceFirst("^0+", "")
            + " " + rec.get("MAILING_HOUSE_NUMBER_SUFFIX_2")
            + " " + rec.get("MAILING_DIRECTION_2")
            + " " + rec.get("MAILING_STREET_NAME_2")
            + " " + rec.get("MAILING_MODE_2")
            + " " + rec.get("MAILING_QUADRANT_2")
            ).replaceAll("^[ ]+", "")
                .replaceAll("[ ]+$", "").replaceAll("[  ]+", " "));

        mailAddr2.append(rec.get("MAILING_APARTMENT_UNIT_2").isEmpty() ? "" : ", #" + rec.get("MAILING_APARTMENT_UNIT_2").replaceFirst("^0+", ""));

        mailAddrcsz.append(rec.get("MAILING_PROPERTY_CITY_2") + ":" + rec.get("MAILING_PROPERTY_STATE_2") + ":"
            + rec.get("MAILING_PROPERTY_ADDRESS_ZIP_CODE_2").substring(0, 5) + ":" + rec.get("MAILING_PROPERTY_ADDRESS_ZIP_CODE_2").substring(5, 9));
      }
      
      if ( ! rec.get("1ST_DEFENDANT_BORROWER_OWNER_LAST_NAME").isEmpty()) {

        results.get("records").add(
                rec.get("1ST_DEFENDANT_BORROWER_OWNER_LAST_NAME") + ":"
                + rec.get("1ST_DEFENDANT_BORROWER_OWNER_FIRST_NAME").split(" ")[0] + ":" 
                + propertyAddr + propertyAddr2 + ":" + propertyAddrcsz
                );
        if (hasMailAddr) {
          results.get("records").add(
              rec.get("1ST_DEFENDANT_BORROWER_OWNER_LAST_NAME") + ":"
              + rec.get("1ST_DEFENDANT_BORROWER_OWNER_FIRST_NAME").split(" ")[0] + ":" 
              + mailAddr + mailAddr2 + ":" + mailAddrcsz);
        }

      }

      if (!rec.get("2ND_DEFENDANT_BORROWER_OWNER_LAST_NAME").isEmpty()) {

        results.get("records").add(
            rec.get("2ND_DEFENDANT_BORROWER_OWNER_LAST_NAME") + ":"
            + rec.get("2ND_DEFENDANT_BORROWER_OWNER_FIRST_NAME").split(" ")[0] + ":" 
            + propertyAddr + propertyAddr2 + ":" + propertyAddrcsz.toString());
        if (hasMailAddr) {
          results.get("records").add(
              rec.get("2ND_DEFENDANT_BORROWER_OWNER_LAST_NAME") + ":" 
              + rec.get("2ND_DEFENDANT_BORROWER_OWNER_FIRST_NAME").split(" ")[0] + ":" 
              + mailAddr + mailAddr2 + ":" + mailAddrcsz);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }
}

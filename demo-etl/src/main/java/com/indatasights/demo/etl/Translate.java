package com.indatasights.demo.etl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Translate {
  public static Log LOG = LogFactory.getLog(Translate.class);

  String settingsString;
  TranslateSettings settings;
  CSVFormat csvFormat;
  String vendor;
  String dataset;
  String[] dsHeaders;

  public Translate(String vendor, String dataset) {
    setVendor(vendor);
    setDataset(dataset);
    setSettingsString(Translate.getFile("/translate/vendors/" + getVendor() + "/" + getDataset() + ".json"));
    //LOG.info(getSettingsString());
    setSettings(new Gson().fromJson(getSettingsString(), TranslateSettings.class));
    //LOG.info("SettingsString: " +getSettingsString() +" vendor: "+getVendor() + " " +" dataset: "+getDataset() );
    try {
      setCsvFormat(CSVFormat.valueOf(getSettings().getFormat()));
    } catch (Exception e) {
      setCsvFormat(CSVFormat.TDF);
    }

    //if (null != getSettings().getFields()) {
      dsHeaders = new String[getSettings().getFields().size()];
      for (int i = 0; i < getSettings().getFields().size(); i++) {
        dsHeaders[i] = getSettings().getFields().get(i).get("name");
      }
      setCsvFormat(getCsvFormat().withHeader(dsHeaders));
    //} else {
    //  dsHeaders = null;
    //}
    
    if (null != getSettings().getDelim()) {
      setCsvFormat(getCsvFormat().withDelimiter(getSettings().getDelim()));
    }
  }

  public String toString() {
    return settingsString;
  }

  public TranslateSettings getSettings() {
    return settings;
  }

  private void setSettings(TranslateSettings settings) {
    this.settings = settings;
  }

  public CSVFormat getCsvFormat() {
    return csvFormat;
  }

  private void setCsvFormat(CSVFormat csvFormat) {
    this.csvFormat = csvFormat;
  }

  public CSVRecord parse(String inRec) throws IOException {
    return org.apache.commons.csv.CSVParser.parse(inRec, getCsvFormat()).getRecords().get(0);
  }

  public static void main(String[] args) {
  }

  public String getSettingsString() {
    return settingsString;
  }

  private void setSettingsString(String settingsString) {
    this.settingsString = settingsString;
  }

  public String getVendor() {
    return vendor;
  }

  private void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getDataset() {
    return dataset;
  }

  private void setDataset(String dataset) {
    this.dataset = dataset;
  }



  /**
   * Loads file named from form resources into string
   * 
   * @param fileName
   * @return
   */
  public static String getFile(String fileName) {
    //System.out.println("filename: " + fileName);
    StringBuilder result = new StringBuilder("");
    String input;
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(Translate.class.getResourceAsStream(fileName)));
      while ((input = br.readLine()) != null) {
        result.append(input);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return result.toString();
  }

}

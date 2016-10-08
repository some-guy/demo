package com.indatasights.demo.etl;

import java.util.List;
import java.util.Map;

public class TranslateSettings {

  String comment;
  String vendor;
  Map<String, String> contactInfo;
  Character delim;
  String format;
  String useCases;
  Map<String, List<Map<String,String>>> locations;
  List<Map<String,String>> fields;

  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }
  public String getVendor() {
    return vendor;
  }
  public void setVendor(String vendor) {
    this.vendor = vendor;
  }
  public Map<String, String> getContactInfo() {
    return contactInfo;
  }
  public void setContactInfo(Map<String, String> contactInfo) {
    this.contactInfo = contactInfo;
  }
  public Character getDelim() {
    return delim;
  }
  public void setDelim(Character delim) {
    this.delim = delim;
  }
  public String getUseCases() {
    return useCases;
  }
  public void setUseCases(String useCases) {
    this.useCases = useCases;
  }

  public Map<String, List<Map<String,String>>> getLocations() {
    return locations;
  }
  public void setLocations(Map<String, List<Map<String,String>>> locations) {
    this.locations = locations;
  }
  public List<Map<String, String>> getFields() {
    return fields;
  }
  public void setFields(List<Map<String, String>> fields) {
    this.fields = fields;
  }
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }
  public String toString() {
    return "comment: "+  comment + 
        " delim: " + delim + 
        " vendor: " + vendor + 
        " fields: " + fields.toString() +
        " locations: " + locations.toString() +"\n";
  }
}

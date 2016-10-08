package com.indatasights.demo.etl.vendors.ip2location;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.indatasights.demo.etl.Translator;

public class DB24 extends IP2Location implements Translator {
	private HashMap<String, String> myMap;
	private Gson gson;

  public DB24() {
	  myMap = new HashMap<>();
	  gson = new GsonBuilder().create();
  }
  @Override
  public Map<String, List<String>> xlate(CSVRecord rec) throws IOException, InterruptedException {
    try {
    	myMap.clear();
    	myMap.put("IP_FROM",rec.get("IP_FROM"));
    	myMap.put("IP_TO",rec.get("IP_TO"));
    	myMap.put("COUNTRY_CODE",rec.get("COUNTRY_CODE"));
    	myMap.put("COUNTRY_NAME",rec.get("COUNTRY_NAME"));
    	myMap.put("REGION",rec.get("REGION"));
    	myMap.put("CITY",rec.get("CITY"));
    	myMap.put("LATITUDE",rec.get("LATITUDE"));
    	myMap.put("LONGITUDE",rec.get("LONGITUDE"));
    	myMap.put("ZIPCODE",rec.get("ZIPCODE"));
    	myMap.put("TIME_ZONE",rec.get("TIME_ZONE"));
    	myMap.put("ISP_NAME",rec.get("ISP_NAME"));
    	myMap.put("DOMAIN_NAME",rec.get("DOMAIN_NAME"));
    	myMap.put("NETSPEED",rec.get("NETSPEED"));
    	myMap.put("IDD_CODE",rec.get("IDD_CODE"));
    	myMap.put("AREA_CODE",rec.get("AREA_CODE"));
    	myMap.put("WEATHER_STATION_CODE",rec.get("WEATHER_STATION_CODE"));
    	myMap.put("WEATHER_STATION_NAME",rec.get("WEATHER_STATION_NAME"));
    	myMap.put("MCC",rec.get("MCC"));
    	myMap.put("MNC",rec.get("MNC"));
    	myMap.put("MOBILE_BRAND",rec.get("MOBILE_BRAND"));
    	myMap.put("ELEVATION",rec.get("ELEVATION"));
    	myMap.put("USAGE_TYPE",rec.get("USAGE_TYPE"));

    	String json = gson.toJson(myMap);

    	//System.out.println("json: " +json);
      results.get("records").clear();
      results.get("counters").clear();
      
      results.get("records").add(json);
      

    } catch (Exception e) {
      LOG.info("db24 err: " + e.toString());
    }
    return results;
  }

}

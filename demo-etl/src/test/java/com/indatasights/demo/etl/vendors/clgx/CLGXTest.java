package com.indatasights.demo.etl.vendors.clgx;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import com.indatasights.demo.etl.Translate;
import com.indatasights.demo.etl.Translator;
import com.indatasights.demo.etl.vendors.clgx.ExDeed;
import com.indatasights.demo.etl.vendors.clgx.ExMLS;
import com.indatasights.demo.etl.vendors.clgx.ExTax;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

public class CLGXTest {

  @Test 
  public void ExTaxXlaterTest () {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "extax";
      Translate xlate = new Translate(vendor, dataset);
      Translator xlater = new ExTax();
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      Map<String,List<String>> results = xlater.xlate(xlate.parse(rec));
      //System.out.println("num records in results: "+results.get("records").size());
      //System.out.println("records: "+results.get("records"));
      assertEquals(4, results.get("records").size());
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      e.printStackTrace();
      fail("exception");
    }
  }
  @Test
  public void extaxTest() {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "extax";
      Translate xlate = new Translate(vendor, dataset);
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      CSVRecord csvrec = xlate.parse(rec);
      assertEquals(xlate.getSettings().getFields().size(), csvrec.size());
      assertEquals("06037", csvrec.get(0));
      assertEquals("", csvrec.get(177));
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      fail("exception");
    }
  }
  @Test 
  public void ExDeedXlaterTest () {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "exdeed";
      Translate xlate = new Translate(vendor, dataset);
      Translator xlater = new ExDeed();
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      Map<String,List<String>> results = xlater.xlate(xlate.parse(rec));
      //System.out.println("num records in results: "+results.get("records").size());
      //System.out.println("records: "+results.get("records"));
      assertEquals(2, results.get("records").size());
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      e.printStackTrace();
      fail("exception");
    }
  }
  //@Ignore
  @Test
  public void exdeedTest() {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "exdeed";
      Translate xlate = new Translate(vendor, dataset);
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      CSVRecord csvrec = xlate.parse(rec);
      assertEquals(xlate.getSettings().getFields().size(), csvrec.size());
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      fail("exception");
    }
  }
  //@Ignore
  @Test
  public void exfcTest() {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "exfc";
      Translate xlate = new Translate(vendor, dataset);
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      CSVRecord csvrec = xlate.parse(rec);
      assertEquals(xlate.getSettings().getFields().size(), csvrec.size());
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      fail("exception");
    }
  }
  
  @Test 
  public void ExMLSXlaterTest () {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "exmls";
      Translate xlate = new Translate(vendor, dataset);
      Translator xlater = new ExMLS();
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      CSVRecord csvrec=xlate.parse(rec);
      //System.out.println("pool: "+ csvrec.get("PoolYN"));
      //System.out.println("FA_APN: "+csvrec.get("FA_APN"));
      Map<String,List<String>> results = xlater.xlate(csvrec);
      List<String> records = results.get("records");
      //System.out.println ("header: " + Arrays.toString(csvrec.));
      //System.out.println ("results: " + results.toString());
      //System.out.println("num records in results: "+results.get("records").size());
      //System.out.println("records: "+results.get("records"));
      assertEquals("957382001",csvrec.get("FA_APN"));
      assertEquals("",csvrec.get("PoolYN"));
      assertEquals(1, records.size());
    } catch (Exception e) {
      //System.err.println("exception: " + rec);
      //e.printStackTrace();
      fail("exception");
    }
  }
  //@Ignore
  @Test
  public void exmlsTest() {
    String rec = null;
    try {
      String vendor = "clgx";
      String dataset = "exmls";
      Translate xlate = new Translate(vendor, dataset);
      rec = Translate.getFile("/translate/vendors/" + vendor + "/" + dataset + ".rec");
      CSVRecord csvrec = xlate.parse(rec);
      assertEquals(xlate.getSettings().getFields().size(), csvrec.size());
    } catch (Exception e) {
      System.err.println("exception: " + rec);
      fail("exception");
    }
  }
}

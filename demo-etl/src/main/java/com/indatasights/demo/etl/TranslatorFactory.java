package com.indatasights.demo.etl;

import com.indatasights.demo.etl.vendors.clgx.ExADC;
import com.indatasights.demo.etl.vendors.clgx.ExDeed;
import com.indatasights.demo.etl.vendors.clgx.ExFc;
import com.indatasights.demo.etl.vendors.clgx.ExMLS;
import com.indatasights.demo.etl.vendors.clgx.ExTax;
import com.indatasights.demo.etl.vendors.clgx.ICP2M;
import com.indatasights.demo.etl.vendors.ip2location.DB24;

public class TranslatorFactory {

  public Translator getXlater(String vendor, String dataset) {
    switch (vendor + ":" + dataset) {
    case "clgx:extax":
      return new ExTax();
    case "clgx:exmls":
      return new ExMLS();
    case "clgx:exdeed":
      return new ExDeed();
    case "clgx:exfc":
      return new ExFc();
    case "clgx:exadc":
      return new ExADC();
    case "clgx:icp2m":
      return new ICP2M();
    case "ip2location:db24":
    	return new DB24();
    default:
      throw new IllegalArgumentException("Add vendor:source to XlaterFactory.java");
    }
  }

}

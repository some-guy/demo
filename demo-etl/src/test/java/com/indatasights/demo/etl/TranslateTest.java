package com.indatasights.demo.etl;

import org.junit.*;

import com.indatasights.demo.etl.Translate;

import static org.junit.Assert.*;

public class TranslateTest {

  @Test
  public void XlateTestGetFile() {
    Translate xlate = new Translate("clgx", "extax");
    assertEquals("|", Character.toString(xlate.getSettings().getDelim()));
    //System.out.println(Arrays.toString(xlate.getCsvFormat().getHeader()));
    //System.out.println("dsHeaders: " + Arrays.toString(dsHeaders));
    //System.out.println("headerSB: " + headerSB.toString());

    assertEquals(178, xlate.getSettings().getFields().size());
  }
}

package com.indatasights.demo.unite;


import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import net.sourceforge.jgeocoder.AddressComponent;
import static net.sourceforge.jgeocoder.us.AddressParser.parseAddress;
import static net.sourceforge.jgeocoder.us.AddressStandardizer.normalizeParsedAddress;

/*
 * Assumes all records are in the same format
 */
public class FixRecs {

  private BufferedReader in;
  private String line = "";
  private String[] rec;
  private Map<AddressComponent, String> parsedAddr;
  private Map<AddressComponent, String> normalizedAddr;
  private boolean debug = false;
  private boolean trim = false;
  private boolean parseAddr = false;
  private boolean upperCase = false;
  private StringBuilder outRec = new StringBuilder();

  public void fix() {
    in = new BufferedReader(new InputStreamReader(System.in));

    try {
      while (null != (line = in.readLine())) {
        rec = line.split("\\t", -1);
        trimFields();
        fixAddr();

        emitRecord();
      }
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(line);
    }

  }

  private void trimFields() {
    if (!isTrim())
      return;
    for (int i = 0; i < rec.length; i++) {
      rec[i] = rec[i].trim();
    }
  }

  private void emitRecord() {
    int i = 0;
    outRec.append(rec[i]);
    for (i = 1; i < rec.length; i++) {
      outRec.append("\t").append(rec[i]);
    }
    if (!upperCase)
    System.out.println(outRec.toString().toLowerCase());
    else
      System.out.println(outRec.toString().toUpperCase());

    outRec.setLength(0);
  }

  private void fixAddr() {
    if (!isParseAddr())
      return;
    String addrString = (rec[4] + " " + rec[5] + ", " + rec[6] + ", " + rec[7]
        + ", " + rec[8]);
    parsedAddr = parseAddress(addrString);
    if (debug)
      System.err.println("parsed: " + parsedAddr);

    if (null != parsedAddr) {
      normalizedAddr = normalizeParsedAddress(parsedAddr);
      rec[4] = (normalizedAddr.get(AddressComponent.NUMBER) + " "
          + normalizedAddr.get(AddressComponent.PREDIR) + " "
          + normalizedAddr.get(AddressComponent.STREET) + " "
          + normalizedAddr.get(AddressComponent.POSTDIR) + " " 
          + normalizedAddr.get(AddressComponent.TYPE))
          .replaceAll("null", "")
          .replaceAll("([ ][ ]*)", " ").trim();
      if (null != normalizedAddr.get(AddressComponent.LINE2)) rec[5] = normalizedAddr.get(AddressComponent.LINE2);
      if (null != normalizedAddr.get(AddressComponent.CITY)) rec[6] = normalizedAddr.get(AddressComponent.CITY);
      if (null != normalizedAddr.get(AddressComponent.STATE)) rec[7] = normalizedAddr.get(AddressComponent.STATE);
      if (null != normalizedAddr.get(AddressComponent.ZIP)) rec[8] = normalizedAddr.get(AddressComponent.ZIP);
      
      if (debug) {
        System.err.println("normalized: " + normalizedAddr);
        System.err.println("tmpAddr: " + rec[4]);
      }
    }
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isTrim() {
    return trim;
  }

  public void setTrim(boolean trim) {
    this.trim = trim;
  }

  public boolean isParseAddr() {
    return parseAddr;
  }

  public void setParseAddr(boolean parseAddr) {
    this.parseAddr = parseAddr;
  }

  public boolean isUpperCase() {
    return upperCase;
  }

  public void setUpperCase(boolean upperCase) {
    this.upperCase = upperCase;
  }

  public static void main(String[] args) {
    FixRecs fr = new FixRecs();

    CommandLineParser parser = new GnuParser();

    Options options = new Options();
    options.addOption("i", true, "input file");
    options.addOption("o", true, "output file");
    options.addOption("d", false, "enable debugging");
    options.addOption("t", false, "trim fields");
    options.addOption("a", false, "parse address");
    options.addOption("u", false, "use Upper casing (defaults to lower)");
    try {
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("d")) {
        fr.setDebug(true);
      }
      if (line.hasOption("t")) {
        fr.setTrim(true);
      }
      if (line.hasOption("a")) {
        fr.setParseAddr(true);
      }
      if (line.hasOption("u")) {
        fr.setUpperCase(true);
      }
      fr.fix();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}

package com.indatasights.demo.unite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
@SuppressWarnings("unused")
public class Unite {

  private final Map<String, String> bits = new LinkedHashMap<String, String>();
  private final Map<Integer, Integer> diffs = new LinkedHashMap<Integer, Integer>();
  private final Map<Integer, Integer> tmpDiffs = new LinkedHashMap<Integer, Integer>();
  private String[] previous = null;
  private String[] previousSaved = null;
  private String[] current;
  private final Map<Integer, List<String>> currentIgnored = new LinkedHashMap<Integer, List<String>>();
  private final Map<Integer, List<String>> ignored = new LinkedHashMap<Integer, List<String>>();
  private int[] ignoredFields;
  private int[] immutableFields;
  private final List<Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>();
  private boolean debug = false;
  private boolean clean = true;
  private boolean track = false;
  private String inputFile = null;
  private String outputFile = null;
  private String metaIdFile = null;
  private int metaIdField = -1;
  private BufferedReader in;
  private PrintWriter out;
  private PrintWriter idfh;
  private int totalLines = 0;
  private int metaIdCtr = 0;
  private boolean lastRec = false;
  private String line = "";
  private final StringBuilder outRec = new StringBuilder();
  private final StringBuilder bitsOut = new StringBuilder();

  private void unite() throws IOException {
    try {
      if (metaIdFile != null) {
        idfh = new PrintWriter(new FileWriter(metaIdFile));
      }
      if (outputFile != null) {
        out = new PrintWriter(new FileWriter(outputFile));
      } else
        out = null;
      if (null == inputFile)
        in = new BufferedReader(new InputStreamReader(System.in));
      else
        in = new BufferedReader(new FileReader(inputFile));

      while ((line = in.readLine()) != null) {
        totalLines++;
        if (debug) {
          System.err.println("\nNEW LINE\t" + totalLines + "\n" + line + "\n"
              + " currentIgnored: " + currentIgnored);
        }
        current = line.split("\\t", -1);
        if (current.length == 0)
          continue;

        recordContributingBits();
        stashIgnoredFields();

        if (previous != null) {
          if (debug) {
            System.err.println("has previous!");
          }
          previousSaved = previous.clone();
          if (compareAgainstPrevious()) {
            if (doesNotMatchPrevious()) {
              printRecord(previousSaved);
              previous = current.clone();
            } else {
              mergeArrays(current, previous);
            }
          }
        } else {
          if (debug)
            System.err.println("shiny and new!");
          previous = current.clone();
        }
      }

      lastRec = true;
      printRecord(previous);
      in.close();
      idfh.close();
      if (out != null)
        out.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(line);
    }

  }

  private void printRecord(String[] outGoing) {
    addIgnoredFieldsBackToRecord(outGoing);

    outRec.append(outGoing[0]);
    for (int i = 1; i < outGoing.length; i++) {
      outRec.append("\t").append(outGoing[i]);
    }
    outRec.append("\n");
    if (out != null) {
      out.write(outRec.toString());
    } else {
      System.out.print(outRec.toString());
    }
    if (debug) {
      System.err.println(outRec.toString() + "^^^ Emitted record...");
    }
    outGoing = null;
    outRec.setLength(0);
  }

  private void addToIgnored() {
    if (ignoredFields.length == 0)
      return;
    Iterator<Integer> i = currentIgnored.keySet().iterator();
    while (i.hasNext()) {
      int ci = i.next();
      List<String> tList = currentIgnored.get(ci);
      for (String tS : tList) {
        if (ignored.containsKey(tS)) {
          ignored.get(ci).add(tS);
          if (ignored.get(ci).size() == 1) {

          }
        } else {
          ignored.put(ci, new ArrayList<String>());
          ignored.get(ci).add(tS);
        }

      }
    }
  }

  private boolean compareAgainstPrevious() {
    diffs.clear();
    boolean isDiff = false;
    for (int i = 0; i < previous.length; i++) {
      if (!previous[i].equals(current[i])) {
        diffs.put(i, 1);
        isDiff = true;
      }
    }
    if (debug) {
      if (!diffs.isEmpty()) {
        System.err.println("diffs: " + diffs);
      }
    }
    return isDiff;
  }



  private boolean doesNotMatchPrevious() {
    boolean newRec = false;
    if (debug) {
      System.err.println("doesNotMatchPrevious start");
    }
    for (Map.Entry<Integer, Integer> me : diffs.entrySet()) {
      int oldlength = previous[me.getKey()].length();
      int newlength = current[me.getKey()].length();
      if (Arrays.binarySearch(immutableFields, me.getKey()) > 0
          && (!current[me.getKey()].equals(previous[me.getKey()]))) {
        newRec = true;

      }
      if (current[me.getKey()].startsWith(previous[me.getKey()])
          || previous[me.getKey()].startsWith(current[me.getKey()])) {
        if (debug) {
          System.err.println("strings match: unite.");

        }
      } else {
        if (debug) {
          System.err.println("strings do not match: not united.");

        }
        newRec = true;
      }
    }
    if (debug) {
      System.err.println("doesNotMatchPrevious: newRec = " + newRec);
    }
    return newRec;

  }

  private void mergeArrays(String[] current, String[] previous) {
    for (Map.Entry<Integer, Integer> me : diffs.entrySet()) {
      final int currentLength = current[me.getKey()].length();
      final int previousLength = previous[me.getKey()].length();
      if (currentLength < previousLength) {
        current[me.getKey()] = previous[me.getKey()];
      } else {
        previous[me.getKey()] = current[me.getKey()];
      }
    }
  }


  private void recordContributingBits() {
    if (isTrack() && -1 != metaIdField) {
      for (int i = 0; i < current.length; i++) {
        if (current[i].length() > 0 && i != metaIdField) {
          if (!bits.containsKey(current[metaIdField])) {
            bits.put(current[metaIdField], "");
          }
          bits.put(current[metaIdField], bits.get(current[metaIdField]) + i
              + "-" + current[i].length() + "|");
        }
      }
    }
    if (debug)
      System.err.println("bits in TrackFields: " + bits.toString());
  }

  private void stashIgnoredFields() {
    if (debug) {
      System.err.println("current before RemoveIgnores: "
          + Arrays.asList(current) + "\n" + " RemoveIngores: "
          + Arrays.toString(ignoredFields));
    }
    if (ignoredFields.length == 0)
      return;

    for (int i = 0; i < ignoredFields.length; i++) {
      if (!current[ignoredFields[i]].isEmpty()) {
        if (!currentIgnored.containsKey(ignoredFields[i])) {
          currentIgnored.put(ignoredFields[i], new ArrayList<String>());
        }
        if (!currentIgnored.get(ignoredFields[i]).contains(
            current[ignoredFields[i]])) {
          currentIgnored.get(ignoredFields[i]).add(current[ignoredFields[i]]);

        }
        current[ignoredFields[i]] = "";
      }
    }
    addToIgnored();

    if (debug) {
      System.err.println("currentIgnored:" + currentIgnored + "\n"
          + "current after RemoveIgnores: " + Arrays.asList(current));
    }
  }

  private void addIgnoredFieldsBackToRecord(String[] rec) {
    String key = "";
    String value = "";

    if (currentIgnored.size() > 0) {
      if (!lastRec && !bits.isEmpty()) {
        entryList.clear();
        entryList.addAll(bits.entrySet());

        Entry<String, String> lastEntry = entryList.get(entryList.size() - 1);
        key = lastEntry.getKey();
        value = lastEntry.getValue();
        bits.remove(key);

      }
      for (int i = 0; i < ignoredFields.length; i++) {
        if (i == metaIdField) {
          bitsOut.setLength(0);
          for (Map.Entry<String, String> me : bits.entrySet()) {
            if (!bitsOut.toString().isEmpty())
              bitsOut.append("\t");
            bitsOut.append(me.getKey()).append(":")
                .append(me.getValue().substring(0, me.getValue().length() - 1));
          }
          idfh.write(metaIdCtr + "\t" + bitsOut.toString() + "\n");

          rec[i] = Integer.toString(metaIdCtr++);

        } else {
          if (ignored.containsKey(i) && ignored.get(i).size() == 1) {
            rec[i] = ignored.get(i).iterator().next();
          }
        }
      }
    }
    if (!lastRec && !key.isEmpty()) {
      bits.clear();
      bits.put(key, value);
    }
    currentIgnored.clear();
    ignored.clear();

  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isClean() {
    return clean;
  }

  public void setClean(boolean clean) {
    this.clean = clean;
  }

  public boolean isTrack() {
    return track;
  }

  public void setTrack(boolean track) {
    this.track = track;
  }

  public String getMetaIdFile() {
    return metaIdFile;
  }

  public void setMetaIdFile(String metaIdFile) {
    this.metaIdFile = metaIdFile;
  }

  public int getMetaIdField() {
    return metaIdField;
  }

  public void setMetaIdField(int metaIdField) {
    this.metaIdField = metaIdField;
  }

  public String getInputFile() {
    return inputFile;
  }

  public void setInputFile(String input) {
    this.inputFile = input;
  }

  public String getOutputFile() {
    return outputFile;
  }

  public void setOutputFile(String outputFile) {
    this.outputFile = outputFile;
  }

  public int[] getIgnoredFields() {
    return ignoredFields;
  }

  public void setIgnoredFields(int[] ignoredFields) {
    this.ignoredFields = ignoredFields;
  }

  public String showSettings() {
    return "debug: " + debug + " track: " + track + " metaIdField: "
        + metaIdField + " metaIdFile: " + metaIdFile + " inputFile: "
        + inputFile + " outputFile: " + outputFile + " ignoredFields: "
        + Arrays.toString(ignoredFields) + " immutableFields: "
        + Arrays.toString(immutableFields);
  }

  public static void main(String[] args) {
    Unite unity = new Unite();
    unity.setDebug(false);

    CommandLineParser parser = new GnuParser();

    Options options = new Options();
    options.addOption("t", false, "enable tracking");
    options.addOption("d", false, "enable debugging");
    options.addOption("m", true, "metaId tracking");
    options.addOption("f", true, "input file");
    options.addOption("F", true, "output file");
    options.addOption("i", true, "ignore fields");
    options.addOption("n", true, "immutable fields");
    try {
      CommandLine line = parser.parse(options, args);
      if (line.hasOption("m")) {
        unity.setMetaIdField(Integer.parseInt(line.getOptionValue("m").split(
            ",")[0]));
        unity.setIgnoredFields(new int[] { unity.getMetaIdField() });

        unity.setMetaIdFile(line.getOptionValue("m").split(",")[1]);
        unity.setTrack(true);

      }
      if (line.hasOption("f")) {
        unity.setInputFile(line.getOptionValue("f"));
      }
      if (line.hasOption("d")) {
        unity.setDebug(true);
      }
      if (line.hasOption("F")) {
        unity.setOutputFile(line.getOptionValue("F"));
      }
      if (line.hasOption("n")) {
        String[] imFields = line.getOptionValue("n").split(",");
        int[] tmp = new int[imFields.length];
        for (int i = 0; i < imFields.length; i++) {
          tmp[i] = Integer.parseInt(imFields[i]);
        }
        unity.setImmuableFields(tmp);
      }

      if (line.hasOption("i")) {
        String[] iFields = line.getOptionValue("i").split(",");
        int[] tmp = new int[iFields.length + unity.getIgnoredFields().length];

        int i;
        for (i = 0; i < iFields.length; i++) {
          tmp[i] = Integer.parseInt(iFields[i]);
        }

        for (int j = 0; j + i < (iFields.length + unity.getIgnoredFields().length); j++) {
          tmp[i + j] = unity.getIgnoredFields()[j];
        }

        unity.setIgnoredFields(tmp);

      }
      unity.unite();
      if (unity.isDebug())
        System.err.println(unity.showSettings());
    } catch (Exception exp) {
      exp.printStackTrace();
    }
  }

  private void setImmuableFields(int[] tmp) {
    this.immutableFields = tmp;
  }

}

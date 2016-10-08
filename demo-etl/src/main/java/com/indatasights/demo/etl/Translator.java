package com.indatasights.demo.etl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

public interface Translator {

  public Map<String,List<String>> xlate (CSVRecord record) throws IOException, InterruptedException;
}

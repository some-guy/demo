package com.indatasights.demo.etl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatorBase {
  public Map<String, List<String>> results;

  public TranslatorBase() {
    results = new HashMap<>();
    results.put("records", new ArrayList<String>());
    results.put("counters", new ArrayList<String>());
  }
}

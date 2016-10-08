package com.indatasights.demo.crawl.example;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.map.MultithreadedMapper;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CrawlExample extends Configured implements Tool {
  public static Log LOG = LogFactory.getLog(CrawlExample.class);

  public static class VMapper extends Mapper<Object, Text, Text, Text> {

    URL url;
    Map<String, String> config;
    Map<String, String> query;
    Map<String, String> data;
    Text out;
    Text outKey;
    ExampleAPIClient vClient;
    String crawlType;

    protected void setup(Context context) throws IOException, InterruptedException {

      url = new URL("http://demo.localdomain/get.php?");

      config = new HashMap<>();
      config.put("key", "secret");
      crawlType = context.getConfiguration().get("crawl.type");
      switch (crawlType) {

      case "email":
        config.put("cfg_maxrecs", "1");
        config.put("cfg_mc", "INDIV,HHLD,PINDIV");
        config.put("cfg_namefrq", "1");
        config.put("cfg_ploc", "1");
        config.put("cfg_negmc", "-DIST9");
        config.put("cfg_light", "1");
        config.put("cfg_output", "vstat,stats2,altconfig,basic,email,b2bemail");
        config.put("prodids", "email,b2bemail,whois,finapp");
        break;
      case "phone":
        config.put("cfg_maxrecs", "1");
        config.put("cfg_mc", "INDIV,HHLD,PINDIV");
        config.put("cfg_namefrq", "1");
        config.put("cfg_ploc", "1");
        config.put("cfg_negmc", "-DIST9");
        config.put("cfg_light", "1");
        config.put("cfg_output", "basic");
        config.put("prodids",
            "basic,cell,cell2,whois,finapp,email,b2bemail,busdir6,youngwp,fin2012,telcowp,auto,voter");
        break;
      case "phoneAndEmail":
        config.put("cfg_maxrecs", "1");
        config.put("cfg_mc", "ACSZ");
        config.put("cfg_namefrq", "1");
        config.put("cfg_ploc", "1");
        config.put("cfg_negmc", "-DIST9");
        config.put("cfg_light", "1");
        config.put("cfg_output", "vstat,stats2,basic,email");
        config.put("prodids", "basic,email,cell,cell2,youngwp,voter,finapp,auto");
        break;
      default: throw new IllegalArgumentException("no such crawl type");
      }

      query = new HashMap<>();
      data = new HashMap<>();
      out = new Text();
      outKey = new Text();
      vClient = new ExampleAPIClient(url);

    }

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      try {
        String[] rec = value.toString().split("\\t", -1);


        data.clear();
        query.clear();

        outKey.set(rec[0]);
        query.put("d_last", rec[1]);
        query.put("d_first", rec[2]);
        query.put("d_fulladdr", rec[3]);
        query.put("d_city", rec[4]);
        query.put("d_state", rec[5]);
        query.put("d_zip", rec[6]);
        query.put("d_zip4", rec[7]);
        //  not using a formally defined data structure here, but all inputs records, at a minimum, have the above fields.
        if (rec.length > 8) {
          query.put("d_email", rec[8]);
          if (rec.length > 9) {
            query.put("d_phone", rec[9]);
          }
        }

        data.putAll(config);
        data.putAll(query);
        //if (((rec[1]!="") ||(rec[2]!="")) &&  (rec[3]!="")) {
        //  out.set("{\"noNameAddr\":\"true\"}");
        //  context.getCounter("Attempts", "noNameAddr").increment(1);

        //} else {
            long beginTime = System.currentTimeMillis();
            out.set(vClient.request(data));
            long endTime = System.currentTimeMillis();
            LOG.info("query: " + data.toString() + " runtime: " + Long.toString(((endTime - beginTime)*1000)));
            context.getCounter("Attempts", "success").increment(1);
        //}

        context.write(outKey, out);
      } catch (Exception e) {
        e.printStackTrace();
        context.getCounter("Attempts", "exeptions").increment(1);
      }
    }
  }

  @Override
  public int run(String[] args) throws Exception {
    Job job = Job.getInstance(super.getConf(), "CrawlExample: " + super.getConf().get("crawl.type"));
    job.setJarByClass(CrawlExample.class);
    //job.setMapperClass(VMapper.class);
    job.setMapperClass(MultithreadedMapper.class);
    MultithreadedMapper.setMapperClass(job, VMapper.class);
    MultithreadedMapper.setNumberOfThreads(job, job.getConfiguration().getInt("crawl.threads", 8));
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    job.setNumReduceTasks(0);
    return (job.waitForCompletion(true) ? 0 : 1);
  }

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new CrawlExample(), args);
  }

}

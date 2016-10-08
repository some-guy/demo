package com.indatasights.demo.etl.mapreduce;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.indatasights.demo.etl.Translate;
import com.indatasights.demo.etl.Translator;
import com.indatasights.demo.etl.TranslatorFactory;

public class IndexIP2Location extends Configured implements Tool {
  public static Log LOG = LogFactory.getLog(IndexIP2Location.class);

  public static class EFReducer extends Reducer<Text, Text, Text, Text> {
    Text outValue;
    Text outKey;

    public void setup(Context context) throws IOException, InterruptedException {
      outValue = new Text();
      outKey = new Text();
    }

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      try {
        outKey.set(java.util.UUID.randomUUID().toString());
        outValue.set(key.toString().replaceAll("[:]", "\t"));
        LOG.info(outKey + "\t" + outValue);
        context.write(outKey, outValue);
      } catch (Exception e) {
        LOG.warn("shit: " + key.toString() + " outvalue:" + outValue.toString());
        e.printStackTrace();
      }
    }

    public void cleanup(Context context) throws IOException, InterruptedException {
    }
  }

  public static class EFMapper extends Mapper<Object, Text, Text, Text> {

    Text outValue;
    Text outKey;
    Translate xlate;
    Translator xlater;

    public void setup(Context context) throws IOException, InterruptedException {

      xlate = new Translate(context.getConfiguration().get("xlate.vendor"), context.getConfiguration().get("xlate.dataset"));
      xlater = new TranslatorFactory().getXlater(xlate.getVendor(), xlate.getDataset());

      outKey = new Text();
      outValue = new Text(xlate.getVendor() + ":" + xlate.getDataset());
    }

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      try {
        Map<String, List<String>> results = xlater.xlate(xlate.parse(value.toString()));
        for (String s : results.get("records")) {
          outKey.set(s);
          context.write(outKey, outValue);
        }
      } catch (Exception e) {
        //LOG.error("WTF! " + value.toString());
        e.printStackTrace();
        context.getCounter("Attempts", "exceptions").increment(1);
      }
    }
  }

  @Override
  public int run(String[] args) throws Exception {
    Job job = Job.getInstance(super.getConf(), "ExtractFields");
    job.setJarByClass(IndexIP2Location.class);
    job.setMapperClass(EFMapper.class);
    job.setReducerClass(EFReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    job.setNumReduceTasks(1);

    return (job.waitForCompletion(true) ? 0 : 1);
  }

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new IndexIP2Location(), args);
  }
}

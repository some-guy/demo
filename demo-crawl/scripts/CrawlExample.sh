#!/bin/bash
set -e
set -x

source ${0%%/*}/module.conf

usage () {
  echo  "${0##*/} -i indir -o outdir -t crawlType [phone|email|phoneAndEmai] -c configDir"
  exit 1
}

cType="email"
config=""
while getopts "i:o:t:c:" opt ;
do
   case "$opt" in
      c) config="--config $OPTARG"
         ;;
      i) inDir="$OPTARG"
         ;;
      o) outDir="$OPTARG"
         ;;
      t) cType="$OPTARG"
         ;;
      '?') usage
         ;;
   esac
done

[ -z "$outDir" ] && usage

if hadoop $config fs -ls ${outDir} > /dev/null 2> /dev/null ; then 
  hadoop $config fs -rm -r ${outDir}
fi

hadoop $config jar \
  $binDir/../target/demo-crawl-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
  com.indatasights.demo.crawl.example.CrawlExample \
  -Dcrawl.type="${cType}" \
  -Dmapreduce.output.fileoutputformat.outputdir="${outDir}" \
  -Dmapreduce.input.fileinputformat.inputdir="${inDir}"

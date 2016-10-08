#!/bin/bash
set -e
#set -x

mainClass="com.indatasights.demo.etl.mapreduce.ExtractFields"

[ -f "${0%%/*}/module.conf" ] && . ${0%%/*}/module.conf || exit 1

usage () {
  echo Example:
  echo -e "#$0 -i /data/vendors/clgx/extax/sample -o out/clgx -v clgx -d extax -c"
  exit 1
}
cleanUp=false
while getopts "i:o:d:v:c" opt
do
   case "$opt" in
      c) cleanUp=true
         ;;
      i) inDir="$OPTARG"
         ;;
      o) outDir="$OPTARG"
         ;;
      d) dataset="$OPTARG"
         ;;
      v) vendor="$OPTARG"
         ;;
      '?') usage
         ;;
   esac
done

[ -z "$outDir" -o -z "$dataset" -o -z "$vendor"  -o -z "$inDir" ] && usage

if $cleanUp ; then
  if hadoop fs -ls "${outDir}" > /dev/null 2> /dev/null ; then 
    hadoop fs -rm -r "${outDir}"
  fi
fi

yarn jar $jar $mainClass \
  -Dmapreduce.input.fileinputformat.inputdir="${inDir}" \
  -Dmapreduce.output.fileoutputformat.outputdir="${outDir}" \
  -Dxlate.dataset="${dataset}" \
  -Dxlate.vendor="${vendor}" 

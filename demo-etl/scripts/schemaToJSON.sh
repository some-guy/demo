#!/bin/bash
# this only creates entries for the "fields"  node
for i in `ls exdeed exfc exmls extax`
do
   head -n1 $i | dos2unix | tr '|' '\n' |\
      awk '{print "{\"name\":\""$0"\", \"type\":\"string\", \"default\":\"\", \"description\":\"\"},"}' > $i.json
done

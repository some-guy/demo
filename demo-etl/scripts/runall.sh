#!/bin/bash
mvn assembly:assembly
for i in adc tax mls deed fc
do
bin/ExtractFields.sh -i /data/vendors/clgx/ex${i}/sample -o out/newCrawl/clgx${i}ForCrawl -v clgx -d ex${i} -c
done

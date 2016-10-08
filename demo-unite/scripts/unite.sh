#!/bin/sh
# unite dedpulicates nearest neighbors
# depends heavily on neighbors being sorted correctly
OLD_LC_ALL=$LC_ALL
export LC_ALL="C"
#set -e
#set -x
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
input="${DIR}/../input/src[0-9].tsv"
phase=0

[[ ! -f $DIR/../target/demo-unite-0.0.1-SNAPSHOT-jar-with-dependencies.jar ]] && mvn -f $DIR/../pom.xml assembly:assembly

cat $input |\
  java -cp $DIR/../target/demo-unite-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.indatasights.demo.unite.FixRecs -d -t -a 2> /tmp/FixRecs.debug |\
  sort -t $'\x09' -k4,4 -k2,2 > /tmp/cleaned-sorted-unity-test

# first pass is sorted on name
cat /tmp/cleaned-sorted-unity-test |\
  java -cp $DIR/../target/demo-unite-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.indatasights.demo.unite.Unite -t -m0,/tmp/phase1.ids -i2 -n3 -d 2> /tmp/phase1.debug | tee /tmp/phase1.out | wc >/tmp/phase1.out.wc

# sort on  email
sort -k 10,10  -t $'\x09' /tmp/phase1.out |\
  java -cp $DIR/../target/demo-unite-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.indatasights.demo.unite.Unite -t -m0,/tmp/phase2.ids -i2 -n3 -d 2> /tmp/phase2.debug | tee /tmp/phase2.out | wc >/tmp/phase2.out.wc

echo input:
head $DIR/../input/*.tsv

echo
echo
echo final result:
export LC_ALL=$OLD_LC_ALL

$DIR/columns.sh < /tmp/phase2.out

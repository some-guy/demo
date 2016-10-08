#!/bin/bash
#set -e
#set -x
binDir=$(cd -P -- "$(dirname -- "${BASH_SOURCE-$0}")" && pwd -P)

pid=$$

# ec2 options
region="us-west-2"
zone="${region}b"
ami="ami-1de9f12d"
subnet="subnet-288bfc5f"
key="int"
iType="m1.xlarge"

usage () {
   echo ${0##*/} -n clusterName -c numHosts
   exit 1
}

# process options
clusterName=""
num=""

while getopts "n:c:" opt ;
do
   case "$opt" in 
      n) clusterName="$OPTARG"
         ;;
      c) num="$OPTARG"
         ;;
      '?') usage
         ;;
   esac
done

[ -z "$clusterName"  -o -z "$num" ] && usage

runLog="/tmp/start.log.$pid"
cName="${clusterName}.${pid}"
ansibleDir="${binDir}/../ansible"
ansibleHosts="${ansibleDir}/${clusterName}.hosts.$pid"
ansibleInstances="${ansibleDir}/${clusterName}.instances.$pid"

echo Starting $num instances.
ec2-run-instances \
   --region $region \
   -z $zone\
   $ami \
   -b /dev/sda1=:100 \
   -b /dev/sdb=ephemeral0 \
   -b /dev/sdc=ephemeral1 \
   -b /dev/sdd=ephemeral2 \
   -b /dev/sde=ephemeral3 \
   -s $subnet \
   -k $key \
   -t $iType \
   -n $num \
   > $runLog

instances=$(grep ^INST $runLog | cut -f2 )

echo $instances > $ansibleInstances
grep ^INSTANCE $runLog | cut -f5 > $ansibleHosts

for i in $instances
do
   ec2-create-tags $i -t Name="${cName}" > /dev/null
done

echo -n "Waiting for running systems."
while [ "`ec2-describe-instance-status -A $instances | grep  -w ^INSTANCE | cut -f4 | grep 'pending' | wc -l`" != "0" ]
do
   echo -n "."
   sleep 30
done
echo

echo -n "Waiting for initialized systems."
while [ "`ec2-describe-instance-status -A $instances | grep  ^INSTANCESTATUS | cut -f3 | grep 'initializing' | wc -l`" != "0" ]
do
   echo -n "."
   sleep 30
done
echo Done.

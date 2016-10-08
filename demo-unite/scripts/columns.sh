#!/bin/bash
#set -e
#set -x

sep="•"

[[ -n "$LC_ALL" ]]  && [[ ${LC_ALL##*.} -eq "utf8" ]] && sep="|"

dos2unix |\
   awk '{for (i=1; i<= NF ; i++){ if (length($i)==0) $i="'$sep'"} print}' FS=$'\011' OFS=$'\011' |\
   column -t -s $'\011' -x -c 100

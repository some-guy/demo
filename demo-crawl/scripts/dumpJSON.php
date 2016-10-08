#!/usr/bin/env php
<?php

// small tool to dump json, if you need something stable, copy and modify.
$in=fopen("php://stdin","r");

$field=1;
$pmatches=0;
$ematches=0;
$records=0;
fwrite(STDOUT, sprintf("id\tfname\tlname\taddress\tcity\tstate\tzip\tzip4\tphone\temail\n" , $records ));
while ( ( $line = fgets ($in) ) !== FALSE ) {
  $records++;
  $phone="";
  $email="";
  if ($field === null) {
    //print_r(json_decode($line,true));
  } else {
    $json=explode("\t",$line);
    $results=json_decode($json[$field],true);
    //print_r($results);

    if (!empty($results["Versium"]["results"][0]["Phone"])) {
      $pmatches++;
      $phone=$results["Versium"]["results"][0]["Phone"];
    }
    if (!empty($results["Versium"]["results"][0]["EmailAddr"])) {
      $ematches++;
      $email=$results["Versium"]["results"][0]["EmailAddr"];
    }
    printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
      $json[0],
     (!empty($results["Versium"]["input-query"]["FirstName"]))?$results["Versium"]["input-query"]["FirstName"]:"", 
     (!empty($results["Versium"]["input-query"]["LastName"]))?$results["Versium"]["input-query"]["LastName"]:"", 
     (!empty($results["Versium"]["input-query"]["Address"]))?$results["Versium"]["input-query"]["Address"]:"", 
     (!empty($results["Versium"]["input-query"]["City"]))?$results["Versium"]["input-query"]["City"]:"", 
     (!empty($results["Versium"]["input-query"]["State"]))?$results["Versium"]["input-query"]["State"]:"", 
     (!empty($results["Versium"]["input-query"]["PostalCode"]))?$results["Versium"]["input-query"]["PostalCode"]:"", 
     "", 
     $phone,
     $email
    );
  }
}
fclose($in);
fwrite(STDERR, "records: " . $records . "\n");
fwrite(STDERR, "pmatches: " . $pmatches . "\n");
fwrite(STDERR, "ematches: " . $ematches . "\n");
fwrite(STDERR, "matchrates email:" .round(($ematches/$records),2). " phone: ".round(($pmatches/$records),2)."\n");

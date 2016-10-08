#!/bin/bash

rec='{"WEATHER_STATION_NAME":"Atlanta","IDD_CODE":"1","USAGE_TYPE":"DCH","MNC":"-","IP_FROM":"67260672","LONGITUDE":"-84.387980","ZIPCODE":"30301","ELEVATION":"318","MOBILE_BRAND":"-","AREA_CODE":"404/678/770","MCC":"-","DOMAIN_NAME":"level3.com","REGION":"Georgia","TIME_ZONE":"-05:00","CITY":"Atlanta","WEATHER_STATION_CODE":"USGA0028","IP_TO":"67260927","ISP_NAME":"Level 3 Communications Inc.","COUNTRY_NAME":"United States","LATITUDE":"33.749000","NETSPEED":"T1","COUNTRY_CODE":"US"}'

curl -XDELETE 'http://docker:9200/ip2location-test/'
curl -XPUT 'http://docker:9200/ip2location-test/' -d '{
    "settings" : {
        "index" : {
            "number_of_shards" : 1,
            "number_of_replicas" : 0 
        }
    }}'
    #,
    #"mappings" : {
    #    "record" : {
    #        "properties" : {
    #            "IP_FROM" : { "type" : "string", "index" : "not_analyzed" },
    #            "IP_TO" : { "type" : "string", "index" : "not_analyzed" }
    #        }
    #    }
    #}
#}'
curl -XPOST 'http://docker:9200/ip2location-test/record/' -d "$rec"

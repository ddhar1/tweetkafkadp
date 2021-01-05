#!/bin/bash

# TODO: replace with correct ip addresses
zookeeper="54.186.71.141"
broker0="54.186.232.239"
broker1="54.187.119.73"
broker2="34.213.55.244"

#sed's -e allows you to replace keywords with current IP address
# broker 0
sed -e "s/brokerserverid/${broker0}/g"  -e "s/zookeeperserverid/${zookeeper}/g" ./server_configs/server0/server.properties_raw > ./server_configs/server0/server.properties
# broker 1
sed -e "s/brokerserverid/${broker1}/g" -e "s/zookeeperserverid/${zookeeper}/g" ./server_configs/server1/server.properties_raw > ./server_configs/server1/server.properties
# broker 2
sed -e "s/brokerserverid/${broker2}/g" -e "s/zookeeperserverid/${zookeeper}/g" ./server_configs/server2/server.properties_raw > ./server_configs/server2/server.properties

sed -e "s/brokerserverid0/${broker0}/g" -e "s/brokerserverid1/${broker1}/g" -e "s/brokerserverid2/${broker2}/g" -e "s/zookeeperserverid/${zookeeper}/g" ./server_configs/hosts.yml_raw > ./hosts.yml

# zookeeper
#sed -e "s/brokerserverid/${broker2}/g" ./ansible/server_configs/server2/server.properties_raw > ./ansible/server_configs/server2/server.properties

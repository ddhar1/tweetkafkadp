#!/bin/bash

# replace with correct ip addresses
zookeeper="54.149.164.41"
broker0="34.213.179.50"
broker1="34.220.78.168"
broker2="34.210.71.28"

# broker 0
#> ./ansible/server_configs/server0/server.properties
sed -e "s/brokerserverid/${broker0}/g"  -e "s/zookeeperserverid/${zookeeper}/g" ./ansible/server_configs/server0/server.properties_raw > ./ansible/server_configs/server0/server.properties
# broker 1
sed -e "s/brokerserverid/${broker1}/g" -e "s/zookeeperserverid/${zookeeper}/g" ./ansible/server_configs/server1/server.properties_raw > ./ansible/server_configs/server1/server.properties
# broker 2
sed -e "s/brokerserverid/${broker2}/g" -e "s/zookeeperserverid/${zookeeper}/g" ./ansible/server_configs/server2/server.properties_raw > ./ansible/server_configs/server2/server.properties

# zookeeper
#sed -e "s/brokerserverid/${broker2}/g" ./ansible/server_configs/server2/server.properties_raw > ./ansible/server_configs/server2/server.properties

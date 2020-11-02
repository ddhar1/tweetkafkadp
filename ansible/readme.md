# Install Kafka Zookeeper on hosts

Used to install kafka  - 3 hosts will have a partition, while 1 will have zookeeper

## To use:
1. Replace the IP addresses in the hosts file with address of servers that will be used as brokers and zookeeper
    * in folder server_configs, for each server.properties file for each server, replace the IP address of zookeeper with the server zookeeper will be installedin
2. In terminal run `ansible all -m ping -i ./hosts.yml`, Accept the new fingerprint and verify that a connection can be made
3. Run `ansible-playbook -i hosts.yml kafka.yml` in order to run the playbook 
4. SSH into a server and create your topic. Example below. Make sure to replace after `--zookeeper` with the IP address of your servers with zookeeper, as well as 
```bash
 ~/kafka/bin/kafka-topics.sh --create --zookeeper 34.216.208.38:2181 --replication-factor 3 --partitions 1 --topic twitteronstocks
```

You can list the topics with: `~/kafka/bin/kafka-topics.sh --zookeeper 34.216.208.38:2181 --list`

If you want to consume everything through cmdline use the following. Replace bootstrap-server with the servers being used `~/kafka/bin/kafka-console-consumer.sh --bootstrap-server 54.212.93.115:9092 --topic twitter_on_stocks --from-beginning`


## Description of files


#### Useful Links
* [Ansible: Intro to Playbooks](https://docs.ansible.com/ansible/latest/user_guide/playbooks_intro.html#about-playbooks)
* [Ansible: How to build your inventory](https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html)
* [Apache Kafka: Download](https://kafka.apache.odownloads)
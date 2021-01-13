# Install Kafka Zookeeper on hosts with Ansible

Used to install kafka  - 3 hosts will have a partition, while 1 will have zookeeper

## To use
Make sure you have ansible installed

# Update ymls to be used:
1. We first need to update `./hosts.yml` file and the `server.properties` files that will be uploaded to the brokers used to run Kafka and zookeeper with the ip addresses of the brokers to be used. In the bash script `updateServers.sh`, replace the IP addresses of the variables `zookeeper`, `broker0`, `broker1`, `broker2` with the corresponding ec2instance addresses in the  Run the file using `./updatedServers.sh` in terminal
2. We need to make sure that ansible is using the correct key pair to .pem file to ssh into the EC2 instances being used for the kafka broker. In `./hosts.yml` replace `ansible_ssh_private_key_file`'s value with the location of the .pem key to use to ssh into the servers
3. Depending on what version of kafka you want to use, you may have to replace the URL of the Kafka binary being downloaded using ./kafka.yml. Make sure to replace the 

# Run the ansible scripts
1. In terminal run `ansible all -m ping -i ./hosts.yml`, Accept the new fingerprint and verify that a connection can be made
2. Run `ansible-playbook -i hosts.yml kafka.yml` in order to run the playbook 
3. SSH into a server and create your topic. Example below. Make sure to replace after `--zookeeper` with the IP address of your servers with zookeeper, as well as 
```bash
 ~/kafka/bin/kafka-topics.sh --create --zookeeper 34.216.208.38:2181 --replication-factor 3 --partitions 1 --topic twitter_on_stocks
```

You can list the topics with: `~/kafka/bin/kafka-topics.sh --zookeeper 34.216.208.38:2181 --list`

If you want to consume everything through cmdline use the following. Replace bootstrap-server with the servers being used `~/kafka/bin/kafka-console-consumer.sh --bootstrap-server 54.212.93.115:9092 --topic twitter_on_stocks --from-beginning`


## Description of files


#### Useful Links
* [Ansible: Intro to Playbooks](https://docs.ansible.com/ansible/latest/user_guide/playbooks_intro.html#about-playbooks)
* [Ansible: How to build your inventory](https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html)
* [Apache Kafka: Download](https://kafka.apache.odownloads)
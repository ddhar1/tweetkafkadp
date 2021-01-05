# Creating Servers and relevant security groups that will run Kafka Cluster
For this to run, make sure you have:
1. AWS command line installed
2. With AWS CLI: a profile configured that has permissions to create AWS EC2 Instances, VPCs, Subnets, Internet Gateways.
3. A Key pair made under that profile's account,

* To set the AWS Profile to use to create and destroy resources::  Go into  `./variables.tf`. Replace the `default` of the variable `profile` with the name of the profile configured with AWS CLI 
    * If you're using the AWS CLI for Ubuntu, you can see what profiles you have by looking at the files `~/.aws/config` and `~/.aws/)
* To set the SSH Key pair to use to ssh into the the kakfa brokers: 1. Create a key pair with AWS website -> EC2 -> Create Key Pair. Downlad the .pem file and store it in a safe place. 2. In `./variables.tf`, replace the default of the variable `key_name`, with the name of the key pair just created.
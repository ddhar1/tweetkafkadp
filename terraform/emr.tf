resource "aws_emr_cluster" "cluster" {
    name ="emr_for_sparkstreaming"
    release_label = "emr-6.2.0"
    applications = [ "Spark" ]

    termination_protection = false

    service_role = "EMR_DefaultRole" #TODO: Insert role of choice\

    log_uri = "s3://tweets1/emr-logs/"


    ec2_attributes {
        subnet_id = aws_subnet.prod-subnet-public-1.id
        emr_managed_master_security_group = aws_security_group.securitygroupforemr.id
        emr_managed_slave_security_group = aws_security_group.securitygroupforemr.id
        instance_profile = "EMR_EC2_DefaultRole" #TODO: Insert role of choice
        key_name = var.key_name

    }

    master_instance_group {
      instance_type ="m5.xlarge"
    }

    core_instance_group {
      instance_type ="m5.xlarge"
      instance_count = 2

      ebs_config {
        size ="40"
        type = "gp2"
        volumes_per_instance = 1
      }
    }

    tags = {
        project = "tweetkafkadp"
    }


    step {
        action_on_failure = "TERMINATE_CLUSTER"
        name ="Setup Hadoop Debugging"
        
        hadoop_jar_step {
            jar = "command-runner.jar"
            args = ["state-pusher-script"]
        }

    }


    lifecycle {
        ignore_changes =[step]
    }


}

resource "aws_security_group" "securitygroupforemr" {
    description = "Security group for opening ports used by kafka"
    vpc_id = aws_vpc.prod-vpc.id

    name = "SecurityGroup4EMR"

    tags = {
        project = "tweetkafkadp"
    }

    egress {
        from_port = 0
        to_port = 0
        protocol = -1
        cidr_blocks =["0.0.0.0/0"]
    }

}
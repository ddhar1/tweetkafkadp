resource "aws_emr_cluster" "cluster" {
    name ="emr_for_sparkstreaming"
    release_label = "emr-5.31.0"
    applications = [ "Spark 2.4.6" ]

    termination_protection = false

    service_role = "EMR_EC2_DefaultRole"

    ec2_attributes {
        subnet_id = aws_subnet.prod-subnet-public-1.id
        emr_managed_master_security_group = "emr"
        emr_managed_slave_security_group = "emr"
        instance_profile = "EMR_EC2_DefaultRole"
        key_name = var.key_name

    }

    master_instance_group {
      instance_type ="c4.large"
    }

    core_instance_group {
      instance_type ="c1.xlarge"
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

}

resource "aws_security_group" "emr" {
    description = "Security group for opening ports used by kafka"
    vpc_id = aws_vpc.prod-vpc.id

    name = "Public Access"

        tags = {
        project = "tweetkafkadp"
    }

}
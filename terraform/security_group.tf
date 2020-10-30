resource "aws_security_group" "kafka_ports" {
    description = "Security group for opening ports used by kafka"
    vpc_id = aws_vpc.prod-vpc.id

    name = "Public Access"

    egress {
        from_port = 0
        to_port = 0
        protocol = -1
        cidr_blocks =["0.0.0.0/0"]
    }

    ingress {
        from_port = 3888
        to_port = 3888
        protocol = "tcp"
        cidr_blocks =["0.0.0.0/0"]
        description = "Zookeeper"
    }

    ingress {
        description = "Kafka Brokers"
        from_port = 9092
        to_port = 9092
        protocol = "tcp"
        cidr_blocks =["0.0.0.0/0"]
    }

        ingress {
        description = "HTTP"
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks =["0.0.0.0/0"]
    }

    ingress {
        description = "HTTP"
        from_port = 2049
        to_port = 2049
        protocol="tcp"
        cidr_blocks=["0.0.0.0/0"]
    }
    
    ingress {
        description = "ssh"
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    ingress {
        description="ICMP"
        from_port= 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }


}


resource "aws_security_group" "kafka_ports" {
    description = "Security group for opening ports used by kafka"
    vpc_id = aws_vpc.prod-vpc.id

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
        from_port = 9092
        to_port = 9092
        protocol = "tcp"
        cidr_blocks =["0.0.0.0/0"]
        description = "Kafka Brokers"
    }


}

resource "aws_security_group" "web" {
    description = "Allow web traffic from internet"
    vpc_id = aws_vpc.prod-vpc.id
    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks =["0.0.0.0/0"]
    }

}

resource "aws_security_group" "ssh" {
    description = "Security group to allow SSH"
    vpc_id = aws_vpc.prod-vpc.id
    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }


}
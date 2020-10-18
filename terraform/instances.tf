resource "aws_instance" "broker" {
    count = var.num_instance["broker"]
    instance_type = var.aws_instance_type["broker"]
    ami = var.ami
    key_name = var.key_name

    vpc_security_group_ids = [
        "aws_security_groups.kafka_ports",
        "aws_security_group.ssh"
    ]
    subnet_id = aws_subnet.prod-subnet-public-1.id
    

    associate_public_ip_address = true
    source_dest_check = false



}

resource "aws_instance" "zookeeper" {
    count = var.num_instance["zookeeper"]
    instance_type = var.aws_instance_type["zookeeper"]
    ami = var.ami
    key_name = var.key_name

    vpc_security_group_ids = [
        "aws_security_groups.kafka_ports",
        "aws_security_group.ssh"
    ]
    subnet_id = aws_subnet.prod-subnet-public-1.id
    

    associate_public_ip_address = true
    source_dest_check = false



}
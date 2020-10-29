resource "aws_subnet" "prod-subnet-public-1" {
    vpc_id = aws_vpc.prod-vpc.id // TODO: VPC ID
    cidr_block="10.0.1.0/24"
    map_public_ip_on_launch="true"
    availability_zone = var.availability_zone


}
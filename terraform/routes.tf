resource "aws_route" "public_routes" {
    route_table_id = aws_vpc.prod-vpc.main_route_table_id   
    destination_cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.prod-igw.id

}

resource "aws_route_table" "public_table" {
    vpc_id = aws_vpc.prod-vpc.id

    route {
        cidr_block ="0.0.0.0/0"
        gateway_id = aws_internet_gateway.prod-igw.id
    }

    tags = {
        Name = "Public Route Table"
    }
}

resource "aws_route_table_association" "public" {
    subnet_id = aws_subnet.prod-subnet-public-1.id
    route_table_id = aws_route_table.public_table.id
}
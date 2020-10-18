resource "aws_internet_gateway" "prod-igw" {
    vpc_id = "aws_vpc.prod-vpc.id"


}
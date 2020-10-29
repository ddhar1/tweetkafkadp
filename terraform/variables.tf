variable "profile" {
    default = "cincouser"
}

variable "aws_region" {
    description = "AWS Region"
    default = "us-west-2"
}

variable "key_name" {
    description = "Key pair"
    default = "dags"
}


variable "num_instance" {
    description = "Number of EC2 Instances"
    type = map
    default = {
        "zookeeper" = 1
        "broker" = 3
        
    }
}


variable "aws_instance_type" {
    description = "AWS Instance Type"

    type = map
    default = {
        "zookeeper" = "t2.micro" //"t2.large"
        "broker" = "t2.micro"
        
    }

}

variable "ami" {
    description = "AMI"

    //features: AWS command line tools, Python, Ruby, Perl, and Java.
    default = "ami-01fee56b22f308154" 
}

variable "availability_zone" {
    default = "us-west-2b"
}
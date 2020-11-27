# Company Tweets into Kafka Streaming Project

Goal of this project is to play with AWS Environment, Kafka and Classify tweets of companies which have public stocks

I am working on this project in order to play with AWS, as well as consuming twitter tweets with Kafka.

## Pipeline

1. Create connection with [Twitter's Filter Stream API](https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/introduction). Use a consumer written with Java to send these tweets to a topic in Kafka. This is in folder ./twitterProducer
2. Kafka allows data to available to consumer for a week. 
    * Kafka was set up using Terraform, Ansible which created and interacted with AWS EC2 instances
3. Consumer tweets in Kafka with a pyspark script run with AWS EMR. 
    * Script will classify tweets using
4. Consumer will save tweets in Parquet format in an AWS S3 Bucket for further use

I am considering pushing this project further gathering stock data (daily) with AWS Lambda,  and putting the data in S3 in Redshift/Snowflake or RDS, so charts can be created with it using AWS ElasticBeanStalk and Metabase.
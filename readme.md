# Company Tweets into Kafka Streaming Project
Goal of this project is to play with Spark, AWS EMR, Spark Streaming data, Terraform, Ansible

## Business Use Case

The business use case can be for an investment firm who wants to understand the public's opinion about certain companies in order to see if there's as correlation between this and future stock prices. Unfortunately live stock price API is very costly so this will not be incorporated in this project. I may add an extra pipeline that gets the daily stock prices and puts them in S3

## Architecture
![Pipeline of Data](_img/architecture.png)

1. Create connection with [Twitter's Filter Stream API](https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/introduction). Use a producer written with Java which connects to the API and sends these tweets to a topic in Kafka. This is in folder `./twitterProducer`
3. A pySpark Structured Streaming Kafka Consumer will consume tweets in from Kafka, convert the tweets to JSON format the tweets are in into structured data for databases. See `./twitterConsumer`
4. Consumer will save tweets in Parquet format in an AWS S3 Bucket for future use
    

### Design Choices
* Terraform: Easy to setup and takedown AWS resources
* Ansible: Easy to manage tasks to run on multiple hosts, programmatically
* Kafka 
    * Primarily chosen in order to get experience with setting up my own Kafka cluster!
    * Could make more sense to use a managed Kafka service AWS MSK given the costs
    * Kafka is a good choice if multiple services need to access data same data, there is an order to the data 
    * Can also potentialy help ensure fault tolerance - if one 
* AWS EMR:
    * **Easy** to set up, and doesn't require much maintanence work
    * **Cost**: I am paying for the maintanence of the cluster
* Spark Structured Streaming:
    * **Replace Kafka?**: Theoretically I could send the data from the filtered streaming api straight to Spark without storing it in Kafka
    * One can **Track Offets being read from Kafka** which is useful in case the application goes offline, and it needs to be restarted 
    * The amount of data gathered and saved to S3 in the next step can be adjusted by the `df.writeStream().trigger()` setting. Another spark application would have an easier time with larger parquet files, where the trigger is a large number of minutes. A shorter trigger may be better if the data needed to be immediately used 
* **S3**
    * Has event notifications which can alert Snowflake, or another service that there is new data to ingest
    * I can also easily query the data in s3 with AWS Athena




## Set up:
Pre-set up:
* Twitter API:
    1. [Get approval](https://developer.twitter.com/en/apply-for-access) for setting up a Twitter API account
    2. Create API Keys as [per instructions here](https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/quick-start)
* Install Terraform, Ansible
* If you are running the Producer from your computer, install Java. If not, then you could save the producer as a .jar file and then run it as a daemon process on a server.
* Make sure `terraform/variables.tf` and `ansible/hosts.yml`uses the profile that you have the private key for. This will be the key pair you would use in 

### 1. Set up Kafka Cluster 
1. Set up Terraform: Go to ./terraform. Ensure that 
2. Run `terraform plan` to ensure that there are no issues with the terraform plan
3. Run `terraform apply` to create aws resources
### 2. Run Twitter Producer

### 3. Run Twitter Consumer on AWS EMR

## Future Improvements:
* Adjust NLP script to classify tweets using [John Snow Lab's NLP Library](https://nlp.johnsnowlabs.com/)
* Save Producer as a .jar file, run as a daemon process on an EC2 Instance
* Create unit tests for TwitterProducer - look into Mock Producer or Kafka on docker to avoid creating cluster for testing
* In Spark Structured Streaming script - add offset value to column in order to track offsets in case job breaks (bonus: way to automatically restart job from point of last offset?)
* Send data to dashboard, or to database using S3 event notifications
* What would happen if AWS went down (such as when AWS east went down recently)
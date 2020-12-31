# Twitter Producer Java App

This app connects to the twitter API, and sends the data to a Kafka cluster

* `FilteredStreamDemo`: is the main class that the application runs
* `TwitterAPIConnector`: Connects to Twitter's (Filtered) Streaming API. Can add rules to the twitter API using the `./companies.txt` file, and get streams and send them to `TwitterProducer`
* `TwitterProducer`: Sets the properties of the connection with Kafka, sends a message to Kafka
* `Log4JProperties`: Set the properties of log4J, so log4j properties set by system's path variable is not necessary. Instantiated by `FilteredStreamDemo` 


/*
Main Class to access Twitter Stream API and send tweets using Producer to Kafka
 */
import java.io.*;
import java.net.URISyntaxException;

public class FilteredStreamDemo {
    // Set before running
    private static final String TOPIC_NAME = "twitter_on_stocks";
    private static final String bootstrapServers = "34.213.179.50:9092";

    public static void main(String args[]) throws IOException, URISyntaxException {
        TwitterAPIConnector twitterAPIConnector = new TwitterAPIConnector();
        twitterAPIConnector.deleteAllRules();
        twitterAPIConnector.addRulesfromFile("./companies.txt");
        //twitterAPIConnector.sendTweetStreamToKafka( TOPIC_NAME, bootstrapServers, 2 );
    }


}
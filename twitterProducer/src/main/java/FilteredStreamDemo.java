/*
Main Class to access Twitter Stream API and send tweets using Producer to Kafka
 */
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class FilteredStreamDemo {
    public static String TOPIC_NAME = "twitter_on_stocks";
    public static String bootstrapServers = "34.213.179.50:9092";

    public static void main(String args[]) throws IOException, URISyntaxException {
        // Start logging
        Log4JProperties logProperties = new Log4JProperties();
        Logger log = Logger.getLogger("FilteredStreamDemo");

        log.info("Connecting to twitter");
        TwitterAPIConnector twitterAPIConnector = new TwitterAPIConnector();

        twitterAPIConnector.deleteAllRules();
        twitterAPIConnector.addRulesfromFile("./companies.txt");

        log.info("Sending tweets to twitter");
        try {
            twitterAPIConnector.sendTweetStreamToKafka( TOPIC_NAME, bootstrapServers, 2 );
        }
        catch (Exception e) {
            log.fatal("twitterAPIConnector.sendTweetStreamToKafka failed. Please see logs", e);
            System.exit(-1);
        }

    }


}
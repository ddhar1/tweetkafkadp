

import java.io.*;
import java.net.URISyntaxException;

/*
 * Sample code to demonstrate the use of the Filtered Stream endpoint
 *
 * */

/* Example output


Process finished with exit code -1

 */

public class FilteredStreamDemo {
    // Set before running
    private static final String TOPIC_NAME = "twitter_on_stocks";
    private static final String bootstrapServers = "34.213.179.50:9092";

    public static void main(String args[]) throws IOException, URISyntaxException {
        TwitterAPIConnector twitterAPIConnector = new TwitterAPIConnector();
        //System.out.println(System.getenv("BEARER_TOKEN"));
        twitterAPIConnector.deleteAllRules();
        twitterAPIConnector.addRulesfromFile("./companies.txt");

        System.out.println(twitterAPIConnector.getRules());
        //twitterAPIConnector.sendTweetStreamToKafka( TOPIC_NAME, bootstrapServers, 2 );
    }


}
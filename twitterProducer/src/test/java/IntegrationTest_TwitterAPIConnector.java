/*
Check that Twitter API is working
 */

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

class IntegrationTest_TwitterAPIConnector {
    @Test
    public void testCheckBearerToken() {
        String BEARER_TOKEN = System.getenv("BEARER_TOKEN");
        assert( BEARER_TOKEN != null );
    }

    @Test
    public void testAddRulesfromFile() throws IOException, URISyntaxException {
        TwitterAPIConnector test = new TwitterAPIConnector();
        test.addRulesfromFile("./companies.txt");
        assert( test.getRules().size() == 7 );
    }

    @Test
    public void testDeleteRules() throws IOException, URISyntaxException {
        TwitterAPIConnector test = new TwitterAPIConnector();
        test.deleteAllRules();
        assert( test.getRules().size() == 0 );
    }

}
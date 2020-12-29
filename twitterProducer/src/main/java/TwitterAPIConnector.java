/*
Connects to Twitter Stream and sends tweets to kafa using TwitterProducer
 */
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TwitterAPIConnector {
    private static String bearerToken;
    private static TwitterProducer twitterProducer;
    private  Map<String, String> rules;

    //50 requests per 15-minute window (app auth)
    private static int TWEETSAMPLEVALUE = 50;

    public TwitterAPIConnector() throws IOException, URISyntaxException {
        initBearerToken();

        rules = new HashMap<String, String>();
    }



    private static void initBearerToken()
    {
        bearerToken = System.getenv("BEARER_TOKEN");
        if (null == bearerToken)
        {
            throw new NullPointerException("System doesn't have environmental variable BEARER_TOKEN set");
        }
    }

    private static String getBearerToken()
    {
        return bearerToken;
    }

    public static void sendTweetStreamToKafka(String TOPIC_NAME, String bootstrapServers, int minTilSleep ) throws IOException, URISyntaxException {

        twitterProducer = new TwitterProducer(TOPIC_NAME, bootstrapServers);
        connectStream( getBearerToken(), minTilSleep );
    }


    /*
     * This method calls the filtered stream endpoint and streams Tweets from it
     * */
    private static void connectStream(String bearerToken, int minTilSleep) throws IOException, URISyntaxException {
        // Set up formatter
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("Pacific/Tahiti"));
        Date date = new Date(System.currentTimeMillis());
        String output;
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        // original: "https://api.twitter.com/2/tweets/search/stream"
        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream?tweet.fields=created_at");

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            BufferedReader reader = new BufferedReader(new InputStreamReader((entity.getContent())));
            String line = reader.readLine();
            long stop=System.nanoTime()+ TimeUnit.SECONDS.toNanos(minTilSleep*60 );
            while (line != null &  stop>System.nanoTime() ) {


                // without the tag 'created_at'
                //System.out.println(formatter.format(new Date(System.currentTimeMillis())));
                //output = "{\"time\":\"" + formatter.format(new Date(System.currentTimeMillis())) + "\"," + line + "}";
                //System.out.println(output);
                //twitterProducer.sendMessage(output);

                System.out.println(line);
                twitterProducer.sendMessage(line);

                // get the next line!
                line = reader.readLine();
            }
        }

    }

    /*
     * Helper method to setup rules before streaming data
     * Deletes all rules that currently exist
     * */
    private static void setupRules(String bearerToken, Map<String, String> rules) throws IOException, URISyntaxException {
        List<String> existingRules = getRules(bearerToken);
        if (existingRules.size() > 0) {
            deleteRules(bearerToken, existingRules);
        }
        createRules(bearerToken, rules);
    }

    public static void createRules( Map<String, String> rules) throws IOException, URISyntaxException {
        createRules(getBearerToken(), rules);
    }

    /*
     * Helper method to create rules for filtering
     * */
    private static void createRules(String bearerToken, Map<String, String> rules) throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpPost.setHeader("content-type", "application/json");
        StringEntity body = new StringEntity(getFormattedString("{\"add\": [%s]}", rules));
        httpPost.setEntity(body);
        // responses from
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            System.out.print("Response from API after creating rules");
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        }
    }

    public List<String> getRules() throws IOException, URISyntaxException {
        return getRules(getBearerToken());
    }

    /*
     * Helper method to get existing rules
     * */
    private static List<String> getRules(String bearerToken) throws URISyntaxException, IOException {
        List<String> rules = new ArrayList<String>();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("content-type", "application/json");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            JSONObject json = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
            if (json.length() > 1) {
                JSONArray array = (JSONArray) json.get("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = (JSONObject) array.get(i);
                    rules.add(jsonObject.getString("id"));
                }
            }
        }
        return rules;
    }

    /*
     * Helper method to delete rules
     * */
    private static void deleteRules(String bearerToken, List<String> existingRules) throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpPost.setHeader("content-type", "application/json");
        StringEntity body = new StringEntity(getFormattedString("{ \"delete\": { \"ids\": [%s]}}", existingRules));
        httpPost.setEntity(body);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        }
    }

    public static void deleteAllRules() throws IOException, URISyntaxException {
        List<String> existingRules = getRules(getBearerToken());
        if (existingRules.size() > 0) {
            deleteRules(getBearerToken(), existingRules);
        }
    }


    private static String getFormattedString(String string, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        if (ids.size() == 1) {
            return String.format(string, "\"" + ids.get(0) + "\"");
        } else {
            for (String id : ids) {
                sb.append("\"" + id + "\"" + ",");
            }
            String result = sb.toString();
            return String.format(string, result.substring(0, result.length() - 1));
        }
    }

    private static String getFormattedString(String string, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        if (rules.size() == 1) {
            String key = rules.keySet().iterator().next();
            return String.format(string, "{\"value\": \"" + key + "\", \"tag\": \"" + rules.get(key) + "\"}");
        } else {
            for (Map.Entry<String, String> entry : rules.entrySet()) {
                String value = entry.getKey();
                String tag = entry.getValue();
                sb.append("{\"value\": \"" + value + "\", \"tag\": \"" + tag + "\"}" + ",");
            }
            String result = sb.toString();
            return String.format(string, result.substring(0, result.length() - 1));
        }
    }

    public void addRulesfromFile(String path) throws IOException, URISyntaxException {
        rules = new HashMap<String, String>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            String company = scanner.nextLine();
            rules.put("entity:" + company + " sample:" + Integer.toString(TWEETSAMPLEVALUE) +
                    " lang:en", company);

        }

        createRules(bearerToken, rules);
    }
}

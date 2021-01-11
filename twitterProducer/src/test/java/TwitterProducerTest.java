/*
Tests TwitterProducer class to see if it can successfully send a record to a Kafka container
 */

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.trilead.ssh2.util.TimeoutService;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TwitterProducerTest {
    final public static String KAFKA_DOCKER_IMAGE = "confluentinc/cp-kafka:5.4.3";
    public static KafkaContainer kafka;
    public static TwitterProducer twitterProducer;

    public static String testMessage = "This is a Message, to be turned into a producer record";
    public static String topicName= "test_topic";

    /*
        Create Kafka Container, create topic
        Create Twitter Producer to be tested
     */
    @BeforeClass
    public static void setUpKafka() throws InterruptedException, IOException {
        // set up, start kafka test container
        System.out.println("Set up KafkaContainer using Docker Image: " + KAFKA_DOCKER_IMAGE);
        kafka = new KafkaContainer(DockerImageName.parse(KAFKA_DOCKER_IMAGE));

        kafka.start();
        String createTopic =
                String.format(
                        "/usr/bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic %s",
                        topicName);

        Container.ExecResult execResult = kafka.execInContainer("/bin/sh", "-c", createTopic);
        if (execResult.getExitCode() != 0) {
            throw new IllegalStateException("Docker container failed to be built. Check if docker is installed properly and if docker image " + KAFKA_DOCKER_IMAGE+" Is installed ");
        }

        // set up twitter Producer
        twitterProducer = new TwitterProducer( topicName, kafka.getBootstrapServers() );

    }

    @Test
    /*
        Test outputOfcreateProducerRecord creates a valid ProducerRecord<String, String>
     */
    public void testCreateProducerRecord() throws IOException, InterruptedException, ExecutionException {
        ProducerRecord<String, String> outputOfcreateProducerRecord  = twitterProducer.createProducerRecord(  testMessage );

        assert( outputOfcreateProducerRecord.equals( new ProducerRecord<String, String>( topicName, testMessage)  ) );
    }

    /*
        Test sendMessage() method, to see if it can successfully sends message to a producer (given ACKS are required)
     */
    @Test(timeout = 60*100*5)
    public void testSendValueToProducer() throws Exception {

        // Send data to Kafka, Get recordMetaData to see if successful or not, Timeout because docker can be slow
        RecordMetadata recordMetaData = twitterProducer.sendMessage( testMessage ).get(10, TimeUnit.SECONDS );

        assert( recordMetaData.offset() == 0);
    }

    /*
        Tests sendMessage to see if it will fail
        when trying to sendMessage to offline Kafka Cluster, and get RecordMetadata
     */
    @Test
    public void testSendValueToFailedProducer() throws InterruptedException, ExecutionException {
        kafka.stop();

        Exception recordMetadataFailure = null;
        try {
            RecordMetadata recordMetadata = twitterProducer.sendMessage(testMessage).get();
        }
        catch ( Exception e ) {
            recordMetadataFailure = e;
        }

        assert( recordMetadataFailure != null );
    }


    @AfterClass
    public static void closeKafka() {

        twitterProducer.closeProducer();
        kafka.stop();
    }

}

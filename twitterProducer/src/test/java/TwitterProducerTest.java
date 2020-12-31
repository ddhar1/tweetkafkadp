/*
Tests TwitterProducer class to see if it can successfully send a record to a Kafka container
 */

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

public class TwitterProducerTest {
    final public static String KAFKA_DOCKER_IMAGE = "confluentinc/cp-kafka:5.4.3";
    public static KafkaContainer kafka;
    public static TwitterProducer twitterProducer;

    public static String testMessage = "This is a Message, to be turned into a producer record";
    public static String topicName= "test_topic";

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


    @AfterClass
    public static void closeKafka() {

        twitterProducer.closeProducer();
        kafka.stop();
    }

}

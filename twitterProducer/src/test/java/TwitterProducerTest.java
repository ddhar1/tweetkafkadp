/*
Tests TwitterProducer class to see if it can successfully send a record to a Kafka container
 */

import org.junit.BeforeClass;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

public class TwitterProducerTest {
    final public static String KAFKA_DOCKER_IMAGE = "confluentinc/cp-kafka:5.4.3";
    public static KafkaContainer kafka;

    @BeforeClass
    public static void setUpKafka() throws InterruptedException, IOException {
        // set up, start kafka test container
        System.out.println("Set up KafkaContainer using Docker Image: " + KAFKA_DOCKER_IMAGE);
        kafka = new KafkaContainer(DockerImageName.parse(KAFKA_DOCKER_IMAGE));

        kafka.start();
    }
}

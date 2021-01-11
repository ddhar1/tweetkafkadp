/*
   Connect to kafka and prepare messages to send to Kafka
 */


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.Future;

public class TwitterProducer {

    private KafkaProducer<String, String> producer;
    private String topicName;
    private String bootstrapServers;
    private Logger log;

    public TwitterProducer(String topicName, String bootstrapServers)
    {
        log = Logger.getLogger(this.getClass().getName());
        this.bootstrapServers = bootstrapServers;
        this.producer = createKafkaProducer();
        log.info("Created Kafka Producer");
        this.topicName = topicName;
        log.info("When using Kafka producer, Using topicName: " + topicName);
    }


    public KafkaProducer<String, String> createKafkaProducer(){

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create safe Producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5"); // kafka 2.0 >= 1.1 so we can keep this as 5. Use 1 otherwise.

        // high throughput producer (at the expense of a bit of latency and CPU usage)
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // 32 KB batch size

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
    }

    public ProducerRecord<String, String> createProducerRecord(  String message)
    {
        return new ProducerRecord<String, String>( this.topicName, message );
    }

    public Future<RecordMetadata> sendMessage(String message )
    {
        return producer.send( this.createProducerRecord(message), (recordMetadata, exception) -> {
            if (exception != null) {
                log.error("When sending a record, an error occurred: ");
                log.error( exception.getStackTrace() );
            }
        } );
    }



    public void flushProducer()
    {
        log.info("Flushing producer");
        producer.flush();
    }


    public void closeProducer()
    {
        log.info("Closing producer");
        producer.close();
    }


}


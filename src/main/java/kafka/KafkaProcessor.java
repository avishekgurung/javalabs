package kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class KafkaProcessor {

  private static final Properties config = new Properties();
  private static final boolean IS_ENABLED;
  private static final String TOPIC;
  private static final int KAFKA_POLLING_TIME;
  private static boolean isKafkaUp = false;
  private static KafkaProducer<String, String> producer;
  private static ProducerRecord<String, String> producerRecord;

  static {
    try {
      InputStream inputStream = KafkaProcessor.class.getClassLoader().
        getResourceAsStream(KafkaConstants.PROPERTY_FILE);
      config.load(inputStream);
    }
    catch (IOException e) {
      System.out.println("ERROR in loading config file");
      e.printStackTrace();
    }
    IS_ENABLED = Boolean.parseBoolean(config.getProperty(KafkaConstants.IS_ENABLED_KEY));
    TOPIC = config.getProperty(KafkaConstants.TOPIC_KEY);
    KAFKA_POLLING_TIME = Integer.parseInt(config.getProperty(KafkaConstants.BROKER_POLL_MS));
  }

  public KafkaProcessor() {
    producer = new KafkaProducer(getProducerConfig());
    pollKafka();
  }

  private void pollKafka() {
    final Consumer consumer = getConsumer();
    final Timer timer = new Timer();
    if(!isKafkaUp) {
      timer.schedule(new TimerTask() {
        @Override public void run() {
          System.out.println("Polling kafka");
          try {
            Map<String, String> topics = consumer.listTopics();
            if (topics != null) {
              isKafkaUp = true;
              timer.cancel();
              System.out.println("Kafka is up");
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }, 1000, KAFKA_POLLING_TIME);
    }
  }

  private KafkaConsumer getConsumer() {
    Properties consumerConfig = getConsumerConfig();
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(consumerConfig);
    return consumer;

  }

  private Properties getProducerConfig() {
    Properties properties = new Properties();
    properties.put(KafkaConstants.BOOTSTRAP_SERVERS, config.get(KafkaConstants.BOOTSTRAP_SERVERS));
    properties.put(KafkaConstants.KEY_SERIALIZER, config.get(KafkaConstants.KEY_SERIALIZER));
    properties.put(KafkaConstants.VALUE_SERIALIZER, config.get(KafkaConstants.VALUE_SERIALIZER));
    properties.put(KafkaConstants.PRODUCER_TYPE, config.get(KafkaConstants.PRODUCER_TYPE));
    properties.put(KafkaConstants.REQUEST_REQUIRED_ACKS, config.get(KafkaConstants.REQUEST_REQUIRED_ACKS));
    properties.put(KafkaConstants.BATCH_SIZE, config.get(KafkaConstants.BATCH_SIZE));
    properties.put(KafkaConstants.MAX_BLOCK_MS, config.get(KafkaConstants.MAX_BLOCK_MS));
    return properties;
  }

  private Properties getConsumerConfig() {
    Properties properties = new Properties();
    properties.put(KafkaConstants.BOOTSTRAP_SERVERS, config.get(KafkaConstants.BOOTSTRAP_SERVERS));
    properties.put(KafkaConstants.VALUE_DESERIALIZER, config.get(KafkaConstants.VALUE_DESERIALIZER));
    properties.put(KafkaConstants.KEY_DESERIALIZER, config.get(KafkaConstants.KEY_DESERIALIZER));
    properties.put(KafkaConstants.SESSION_TIMEOUT_MS, config.get(KafkaConstants.SESSION_TIMEOUT_MS));
    properties.put(KafkaConstants.REQUEST_TIMEOUT_MS, config.get(KafkaConstants.REQUEST_TIMEOUT_MS));
    return properties;
  }

  public void send(String data) {

    if(IS_ENABLED) {
      System.out.println("Sent initiated " + new Date());
      producerRecord = new ProducerRecord(TOPIC, data.toString());
      producer.send(producerRecord, new Callback() {
        @Override public void onCompletion(RecordMetadata recordMetadata, Exception e) {
          System.out.println("Recv initiated " + new Date());
          if(e != null && e instanceof TimeoutException) {
            e.printStackTrace();
            pollKafka();
          }
        }
      });
    }
  }
}

package kafka;

public class KafkaConstants {

  private KafkaConstants(){}

  protected static final String PROPERTY_FILE = "kafka.properties";
  protected static final String IS_ENABLED_KEY = "enabled";
  protected static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
  protected static final String KEY_SERIALIZER = "key.serializer";
  protected static final String VALUE_SERIALIZER = "value.serializer";
  protected static final String KEY_DESERIALIZER = "key.deserializer";
  protected static final String VALUE_DESERIALIZER = "value.deserializer";
  protected static final String PRODUCER_TYPE = "producer.type";
  protected static final String REQUEST_REQUIRED_ACKS = "request.required.acks";
  protected static final String BATCH_SIZE = "batch.size";
  protected static final String TOPIC_KEY = "kafka-topic";
  protected static final String SESSION_TIMEOUT_MS= "session.timeout.ms";
  protected static final String REQUEST_TIMEOUT_MS= "request.timeout.ms";
  protected static final String BROKER_POLL_MS = "broker.poll.ms";
  protected static final String MAX_BLOCK_MS = "max.block.ms";

}

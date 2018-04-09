package test;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Date;
import java.util.Properties;

public class Test {

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("producer.type", "async");
    //props.put("request.required.acks", "1");
    //props.put("batch.size", "1000");
    //props.put("request.timeout.ms", "10000");
    //props.put("retries", "1");
    //props.put("metadata.fetch.timeout.ms", "1000");
    //props.put("session.timeout.ms", 5000);
    //props.put("message.timeout.ms", 7000);
    //props.put("")
    //props.put("request.timeout.ms", "10000");
    //props.put("transaction.timeout.ms", 6000);
    props.put("max.block.ms", 8000);

    KafkaProducer<String, String> producer = null;
    try {
      producer = new KafkaProducer<String, String>(props);
      ProducerRecord<String, String> producerRecord =
        new ProducerRecord<String, String>("pixel-server", "William");
      System.out.println("Sent " + new Date());
      producer.send(producerRecord, new Callback() {
        @Override public void onCompletion(RecordMetadata recordMetadata, Exception e) {
          System.out.println("Recv " + new Date());
          System.out.println(e);

        }
      });
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      producer.close();
    }
    /*Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("session.timeout.ms", 5000);
    props.put("request.timeout.ms", "7000");


    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    try {
      System.out.println("Sent " + new Date());
      consumer.listTopics();
    }
    catch (Exception e) {
      System.out.println("Recv " + new Date());
      e.printStackTrace();
    }*/

    //props.put("producer.type", "async");

  }

}

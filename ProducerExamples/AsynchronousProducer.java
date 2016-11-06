import java.util.*;
import org.apache.kafka.clients.producer.*;

public class AsynchronousProducer {

   public static void main(String[] args) throws Exception{
      String topicName = "AsynchronousProducerTopic";
          String key = "Key1";
          String value = "Value-1";

      Properties props = new Properties();
      props.put("bootstrap.servers", "localhost:9092,localhost:9093");
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

      Producer<String, String> producer = new KafkaProducer <>(props);

      ProducerRecord<String, String> record = new ProducerRecord<>(topicName,key,value);

      producer.send(record, new MyProducerCallback());
      System.out.println("AsynchronousProducer call completed");
      producer.close();

   }

}

    class MyProducerCallback implements Callback{

       @Override
       public  void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if (e != null)
            System.out.println("AsynchronousProducer failed with an exception");
                else
                        System.out.println("AsynchronousProducer call Success:");
       }
   }

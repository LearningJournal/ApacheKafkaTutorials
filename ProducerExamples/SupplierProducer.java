import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.kafka.clients.producer.*;
public class SupplierProducer {

   public static void main(String[] args) throws Exception{

      String topicName = "SupplierTopic";

      Properties props = new Properties();
      props.put("bootstrap.servers", "localhost:9092,localhost:9093");
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
      props.put("value.serializer", "SupplierSerializer");

      Producer<String, Supplier> producer = new KafkaProducer <>(props);

          DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          Supplier sp1 = new Supplier(101,"Xyz Pvt Ltd.",df.parse("2016-04-01"));
          Supplier sp2 = new Supplier(102,"Abc Pvt Ltd.",df.parse("2012-01-01"));

         producer.send(new ProducerRecord<String,Supplier>(topicName,"SUP",sp1)).get();
         producer.send(new ProducerRecord<String,Supplier>(topicName,"SUP",sp2)).get();

                 System.out.println("SupplierProducer Completed.");
         producer.close();

   }
}
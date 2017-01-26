import java.util.*;
import org.apache.kafka.clients.consumer.*;


public class AvroConsumer{    
    
    public static void main(String[] args) throws Exception{

        String topicName = "AvroClicks";
            
        String groupName = "RG";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("group.id", groupName);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("specific.avro.reader", "true");
        
        KafkaConsumer<String, ClickRecord> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topicName));
        try{
            while (true){
                ConsumerRecords<String, ClickRecord> records = consumer.poll(100);
                for (ConsumerRecord<String, ClickRecord> record : records){
                        System.out.println("Session id="+ record.value().getSessionId()
                                         + " Channel=" + record.value().getChannel() 
                                         + " Referrer=" + record.value().getReferrer());
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            finally{
                consumer.close();
            }
    }
    
}

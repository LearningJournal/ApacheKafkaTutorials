import java.util.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.*;
import java.sql.*;

public class SensorConsumer{


    public static void main(String[] args) throws Exception{

            String topicName = "SensorTopic";
            KafkaConsumer<String, String> consumer = null;
            int rCount;

            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092,localhost:9093");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("enable.auto.commit", "false");

            consumer = new KafkaConsumer<>(props);
            TopicPartition p0 = new TopicPartition(topicName, 0);
            TopicPartition p1 = new TopicPartition(topicName, 1);
            TopicPartition p2 = new TopicPartition(topicName, 2);

            consumer.assign(Arrays.asList(p0,p1,p2));
            System.out.println("Current position p0=" + consumer.position(p0)
                             + " p1=" + consumer.position(p1)
                             + " p2=" + consumer.position(p2));

            consumer.seek(p0, getOffsetFromDB(p0));
            consumer.seek(p1, getOffsetFromDB(p1));
            consumer.seek(p2, getOffsetFromDB(p2));
            System.out.println("New positions po=" + consumer.position(p0)
                             + " p1=" + consumer.position(p1)
                             + " p2=" + consumer.position(p2));

            System.out.println("Start Fetching Now");
            try{
                do{
                    ConsumerRecords<String, String> records = consumer.poll(1000);
                    System.out.println("Record polled " + records.count());
                    rCount = records.count();
                    for (ConsumerRecord<String, String> record : records){
                        saveAndCommit(consumer,record);
                    }
                }while (rCount>0);
            }catch(Exception ex){
                System.out.println("Exception in main.");
            }
            finally{
                    consumer.close();
            }
    }

    private static long getOffsetFromDB(TopicPartition p){
        long offset = 0;
        try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","pandey");

                String sql = "select offset from tss_offsets where topic_name='" + p.topic() + "' and partition=" + p.partition();
                Statement stmt=con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next())
                    offset = rs.getInt("offset");
                stmt.close();
                con.close();
            }catch(Exception e){
                System.out.println("Exception in getOffsetFromDB");
            }
        return offset;
    }

    private static void saveAndCommit(KafkaConsumer<String, String> c, ConsumerRecord<String, String> r){
        System.out.println("Topic=" + r.topic() + " Partition=" + r.partition() + " Offset=" + r.offset() + " Key=" + r.key() + " Value=" + r.value());
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","pandey");
            con.setAutoCommit(false);

            String insertSQL = "insert into tss_data values(?,?)";
            PreparedStatement psInsert = con.prepareStatement(insertSQL);
            psInsert.setString(1,r.key());
            psInsert.setString(2,r.value());

            String updateSQL = "update tss_offsets set offset=? where topic_name=? and partition=?";
            PreparedStatement psUpdate = con.prepareStatement(updateSQL);
            psUpdate.setLong(1,r.offset()+1);
            psUpdate.setString(2,r.topic());
            psUpdate.setInt(3,r.partition());

            psInsert.executeUpdate();
            psUpdate.executeUpdate();
            con.commit();
            con.close();
        }catch(Exception e){
            System.out.println("Exception in saveAndCommit");
        }

    }
}
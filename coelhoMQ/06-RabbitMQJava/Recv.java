import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Recv {

  private final static String UPPER_QUEUE = "random_queue_1";
  private final static String LOWER_QUEUE = "random_queue_2";

  private static boolean msg_queue = false;
  ArrayList<String> mensagens;
  public static void receber_msg(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
    /*while(msg_queue){
      try {Thread.sleep(200);} catch (InterruptedException e) {}
    }
    msg_queue = true;*/

    System.out.println((consumerTag==null?"y":"n")+" "+(envelope==null?"y":"n")+" "+(properties==null?"y":"n")+" "+(body==null?"y":"n"));
    System.out.println(properties);
    if(1==1) return;
    Date date = new Date();
    long age = date.getTime() -
            properties.getTimestamp().getTime();
    int priority = properties.getPriority();
    System.out.println("age: "+age+" | priority: "+priority);

    String message = new String(body, "UTF-8");
    System.out.println(" [x] Received '" + message + "'");
    try {Thread.sleep(1000);} catch (InterruptedException e) {}
  }

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(UPPER_QUEUE, false, false, false, null);
    channel.queueDeclare(LOWER_QUEUE, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer1 = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
          receber_msg(consumerTag,envelope,properties,body);
      }
    };

    Consumer consumer2 = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
          receber_msg(consumerTag,envelope,properties,body);
      }
    };



    channel.basicConsume(UPPER_QUEUE, true, consumer1);
    channel.basicConsume(LOWER_QUEUE, true, consumer2);
  }
}
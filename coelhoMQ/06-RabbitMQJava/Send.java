import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Send {

  private final static String UPPER_QUEUE = "random_queue_1";
  private final static String LOWER_QUEUE = "random_queue_2";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    Map<String, Object> args = new HashMap<String, Object>();
    args.put("x-priority", 10);
    channel.queueDeclare(UPPER_QUEUE, false, false, false, args);
    channel.queueDeclare(LOWER_QUEUE, false, false, false, args);

    AMQP.BasicProperties p = new AMQP.BasicProperties.Builder().priority(2).build();

    String message = "Hello World!";
    String messagep = "Special Hello World!";

    channel.basicPublish("", LOWER_QUEUE, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.basicPublish("", UPPER_QUEUE, p, messagep.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + messagep + "'");

    channel.basicPublish("", LOWER_QUEUE, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.basicPublish("", LOWER_QUEUE, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.basicPublish("", UPPER_QUEUE, p, messagep.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + messagep + "'");

    channel.basicPublish("", UPPER_QUEUE, p, messagep.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + messagep + "'");

    channel.basicPublish("", UPPER_QUEUE, p, messagep.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + messagep + "'");

    channel.basicPublish("", LOWER_QUEUE, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }
}
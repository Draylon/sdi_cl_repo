import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Recv {

  private final static String QUEUE_NAME = "random_queue_1";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    Map<String, Object> args = new HashMap<String, Object>();
    args.put("x-priority", 10);

    channel.queueDeclare(QUEUE_NAME, false, false, false, args);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }

    };

    channel.basicConsume(QUEUE_NAME, true,args, consumer);
  }
}
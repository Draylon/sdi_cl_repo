import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Recv {

  private final static String UPPER_QUEUE = "random_queue_1";
  private final static String LOWER_QUEUE = "random_queue_2";

  private static boolean msg_queue = false;
  private static Thread t = null;

  private static class QueuedMessage{
      String msg;
      long age;
      int priority;
      QueuedMessage(String msg,long age,int priority){this.msg=msg;this.age=age;this.priority=priority;}
  }

  private static Channel channel;
  private static int qt_msg = 0;
  private static Map<QueuedMessage,Integer> lower_queue = new HashMap<>();
  private static Map<QueuedMessage,Integer> upper_queue = new HashMap<>();

  private static void consume_thread(){
      t = new Thread(() -> {
        while(channel.getConnection().isOpen()){
          System.out.println(" - ");
          if(qt_msg<=0) {
            try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            continue;
          }
          //Desempilha
          // age = age - 5 * priority
          lower_queue.forEach((queuedMessage, integer) -> {
            lower_queue.put(queuedMessage,integer - 5*queuedMessage.priority);
          });

          upper_queue.forEach((queuedMessage, integer) -> {
            upper_queue.put(queuedMessage,integer - 5*queuedMessage.priority);
          });

          //msg_queue = false;

          final QueuedMessage[] lower_q = {null};
          final Integer[] lower_q_i = {null};
          lower_queue.forEach ((queuedMessage, integer) -> {
            if(lower_q[0] ==null || lower_q_i[0] > integer){
              lower_q[0] = queuedMessage;
              lower_q_i[0] = integer;
            }
          });

          final QueuedMessage[] upper_q = {null};
          final Integer[] upper_q_i = {null};
          upper_queue.forEach ((queuedMessage, integer) -> {
            if(upper_q[0] ==null || upper_q_i[0] > integer){
              upper_q[0] = queuedMessage;
              upper_q_i[0] = integer;
            }
          });

          System.out.println("Escolha:");
          System.out.println(lower_q_i[0] +" : "+upper_q_i[0]);


          if(upper_q[0]==null){
            System.out.println("Print s/ prioridade");
            System.out.println(" [x] Received '" + lower_q[0].msg + "'"+" | age: "+lower_q[0].age+" | priority: "+lower_q[0].priority +" priority na fila: "+lower_q_i[0]);
            lower_queue.remove(lower_q[0]);
          }else if(lower_q[0] == null){
            System.out.println("Print da prioridade");
            System.out.println(" [x] Received '" + upper_q[0].msg + "'"+" | age: "+upper_q[0].age+" | priority: "+upper_q[0].priority +" priority na fila: "+upper_q_i[0]);
            upper_queue.remove(upper_q[0]);
          }else{
            if(upper_q_i[0] < lower_q_i[0]){
              System.out.println("Print da prioridade");
              System.out.println(" [x] Received '" + upper_q[0].msg + "'"+" | age: "+upper_q[0].age+" | priority: "+upper_q[0].priority +" priority na fila: "+upper_q_i[0]);
              upper_queue.remove(upper_q[0]);
            }else{
              System.out.println("Print s/ prioridade");
              System.out.println(" [x] Received '" + lower_q[0].msg + "'"+" | age: "+lower_q[0].age+" | priority: "+lower_q[0].priority +" priority na fila: "+lower_q_i[0]);
              lower_queue.remove(lower_q[0]);
            }
          }
          qt_msg--;
          try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
        }

      });
  }

  public static void receber_msg(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
    String message = new String(body, "UTF-8");

    int age = (int) new Date().getTime() -
            (int) properties.getTimestamp().getTime();
    int priority = properties.getPriority();

    QueuedMessage msg = new QueuedMessage(message,age,priority);

    qt_msg++;
    if(priority==1) lower_queue.put(msg, age);
    else upper_queue.put(msg,age);

    //while(lista_semaforo){ try {Thread.sleep(200);} catch (InterruptedException e) {} }
    //msg_queue = true;

    //try {Thread.sleep(500);} catch (InterruptedException e) {}
  }

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    channel = connection.createChannel();

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

    consume_thread();
  }
}
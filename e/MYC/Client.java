package MYC;

import java.net.URL;
import java.net.InetAddress;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.Endpoint;
import java.util.*;
import java.io.*;


public class Client {

  public static void main(String[] args) {

    try {

      String host = (args.length < 1) ? "localhost" : args[0];
      if (args.length < 2) {
        System.out.println("ERROR: java MYC.Client <host_server> <name>");
        System.exit(1);
      }

      // ##### WS Recepcao  #####
      URL url1 = new URL("http://"+host+":9875/WSRecepcao?wsdl");
      QName qname1 = new QName("http://MYC/",
      "WSRecepcaoServerImplService");

      Service recepcao = Service.create(url1, qname1);
      WSRecepcaoServer srecepcao = recepcao.getPort(WSRecepcaoServer.class);
      String name1 = args[1];
      InetAddress addr = InetAddress.getLocalHost();
      String hostname = addr.getHostName();
      srecepcao.endClient();

      System.out.println("##  Cliente ("+hostname+") "+name1+"  ##");
      System.out.println("Status: Pronto");
      System.out.println("WS-Pandelo: 2");
      System.out.println("WS-Cobertura: 1");
      System.out.println("WS-Recheio: 4");
      System.out.println("WS-Cortes:2");
      System.out.println("###########");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}

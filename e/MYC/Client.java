package MYC;

import java.net.URL;
import java.net.InetAddress;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.Endpoint;
import java.util.*;
import java.io.*;


public class Client {

  static String cl_id;
  static HashMap<String,Integer> requests = new HashMap<>();

  static void readSetup (String host, WSRecepcaoServer srecepcao) {
      try {
          Scanner sc = new Scanner(System.in);

          boolean client_start = false;

          while(true) {
              if (!sc.hasNextLine()) {
                  break;
              }
              String newline = sc.nextLine();
              BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
              String[] word = newline.split(" ");
              if (Objects.equals(word[1], "Cliente")) {
                  if(Objects.equals(word[2], cl_id))
                      client_start = true;
              }else if(Objects.equals(word[1], "****") || Objects.equals(word[1], "")) {
                client_start = false;
              }else if(client_start){
                  // word[0] servico?
                  // word[2] tipo?
                switch (word[0]) {
                    case "WS-Cobertura":
                      srecepcao.solicitaCobertura(cl_id,word[2]);
                      requests.put("WS-Cobertura",requests.getOrDefault("WS-Cobertura",0)+1);
                        break;
                    case "WS-Pandelo" :
                      srecepcao.solicitaPandelo(cl_id,word[2]);
                      requests.put("WS-Pandelo",requests.getOrDefault("WS-Pandelo",0)+1);
                        break;
                    case "WS-Recheio":
                      srecepcao.solicitaRecheio(cl_id,word[2]);
                      requests.put("WS-Recheio",requests.getOrDefault("WS-Recheio",0)+1);
                        break;
                    case "WS-Corte":
                      srecepcao.solicitaCorte(cl_id,word[2]);
                      requests.put("WS-Corte",requests.getOrDefault("WS-Corte",0)+1);
                        break;
                }
              }
          }
          sc.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  public static void main(String[] args) {
    try {

      String host = (args.length < 1) ? "localhost" : args[0];
      if (args.length < 2) {
        System.out.println("ERROR: java MYC.Client <host_server> <name>");
        System.exit(1);
      }
      cl_id = args[1];

      // ##### WS Recepcao  #####
      URL url1 = new URL("http://"+host+":9875/WSRecepcao?wsdl");
      QName qname1 = new QName("http://MYC/",
      "WSRecepcaoServerImplService");

      Service recepcao = Service.create(url1, qname1);
      WSRecepcaoServer srecepcao = recepcao.getPort(WSRecepcaoServer.class);
      InetAddress addr = InetAddress.getLocalHost();
      String hostname = addr.getHostName();
      srecepcao.setHost(cl_id);
      readSetup(host,srecepcao);
      srecepcao.endClient();

      System.out.println("##  Cliente ("+hostname+") "+cl_id+"  ##");
      System.out.println("Status: Pronto");
      System.out.println("WS-Pandelo: "+requests.getOrDefault("WSPandelo",0));
      System.out.println("WS-Cobertura: "+requests.getOrDefault("WSCobertura",0));
      System.out.println("WS-Recheio: "+requests.getOrDefault("WSRecheio",0));
      System.out.println("WS-Cortes: "+requests.getOrDefault("WSCortes",0));
      System.out.println("###########");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}

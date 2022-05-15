package MYC;

import java.net.URL;
import java.net.InetAddress;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.Endpoint;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;


public class Client {

  static String cl_id;
  static HashMap<String,Integer> requests = new HashMap<>();

  static String[] remove_vazios(String[] word){
    return Arrays.stream(word).filter(x->!x.isEmpty()).collect(Collectors.toList()).toArray(new String[0]);
  }

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
              String[] word = remove_vazios(newline.split(" "));
              System.out.println(Arrays.toString(word));
              if(word.length < 2)
                  continue;
              if (Objects.equals(word[1], "Cliente")) {
                  System.out.println(word[2]+" == "+cl_id+" : "+Objects.equals(word[2], cl_id));
                  if(Objects.equals(word[2], cl_id)) {
                      client_start = true;
                  }
              }else if( Objects.equals(word[1], "****") ) {
                  System.out.println(word[1]+" == **** : "+Objects.equals(word[1], cl_id));
                client_start = false;
              }else if(client_start){
                  // word[0] servico?
                  // word[2] tipo?
                switch (word[0]) {
                    case "WS-Cobertura":
                      srecepcao.solicitaCobertura(cl_id,word[2]);
                      requests.put("WSCobertura",requests.getOrDefault("WSCobertura",0)+1);
                        break;
                    case "WS-Pandelo":
                      srecepcao.solicitaPandelo(cl_id,word[2]);
                      requests.put("WSPandelo",requests.getOrDefault("WSPandelo",0)+1);
                        break;
                    case "WS-Recheio":
                      srecepcao.solicitaRecheio(cl_id,word[2]);
                      requests.put("WSRecheio",requests.getOrDefault("WSRecheio",0)+1);
                        break;
                    case "WS-Cortes":
                      srecepcao.solicitaCorte(cl_id,word[2]);
                      requests.put("WSCorte",requests.getOrDefault("WSCorte",0)+1);
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

        InetAddress addr = InetAddress.getLocalHost();
        String hostname = addr.getHostName(); // pc do client

      // ##### WS Recepcao  #####
      URL url1 = new URL("http://"+host+":9715/WSRecepcao?wsdl");
      QName qname1 = new QName("http://MYC/",
      "WSRecepcaoServerImplService");

      Service recepcao = Service.create(url1, qname1);
      WSRecepcaoServer srecepcao = recepcao.getPort(WSRecepcaoServer.class);

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

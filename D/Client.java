/** HelloClient.java **/
import java.io.*;
import java.util.*;
import java.rmi.registry.*;
import java.net.InetAddress;

public class Client {

  public static void main(String[] args) {
    String host = (args.length < 1) ? null : args[0];
    try {
      // Obtém uma referência para o registro do RMI
      Registry registry = LocateRegistry.getRegistry(host,6600);

      // Obtém a stub do servidor
      BolsaGeometrica stub= (BolsaGeometrica) registry.lookup("myRMIBG");

      InetAddress addr = InetAddress.getLocalHost();
      String hostname = addr.getHostName();
      System.out.println("##  Cliente ("+hostname+") "+args[1]+"  ##");
      System.out.println("Status: atendido por "+args[0]);
      System.out.println("pCli: J");
      System.out.println("###########");

      // Chama o método do servidor e imprime a mensagem
      stub.endClient();
    } catch (Exception ex) {
    }
  }
}

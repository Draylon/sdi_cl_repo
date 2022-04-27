/** HelloServer.java **/

import java.io.*;
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class BGServer implements BolsaGeometrica {
   public BGServer() {}

   private static int NClientes;
   private static String[] pecas;

   public static void setNroClient(int nclients) throws Exception {
       NClientes = nclients;
   }

   public static int getNroClient() throws Exception {
       return(NClientes);
   }

   public static void setPecas(String[] lpecas) throws Exception {
       pecas = lpecas;
   }

   public static String getPeca(int posicao) throws Exception {
       return(pecas[posicao+1]);
   }

   public static int getNroPecas() throws Exception {
       return(pecas.length-2);
   }

   public static void setServer () throws Exception {
       Scanner sc = new Scanner(System.in);
       while(true){
         if (!sc.hasNextLine()) {
           break;
         }
         String sCurrentLine = sc.nextLine();
         BufferedReader inFromUser
         = new BufferedReader(new InputStreamReader(System.in));
         String[] word = sCurrentLine.split(" ");
         switch (word[0]) {
              case "NClientes":
                   setNroClient(Integer.parseInt(word[2]));
                   break;

              // continuar código da leitura das configurações

              case "pecas":
                   setPecas(word);
                   break;

              default:
                //System.out.println("Ignorado: ("+word[0]+")");
         }
       }

       sc.close();
   }

   public void stopServer() {
          System.out.println("##  Servidor  ##");
          System.out.println("Status: finalizado");
          System.out.println("estoque: A A B B E H H H H J");
          System.out.println("entregas: B B J");
           try {
               Registry rmiRegistry = LocateRegistry.getRegistry(6600);
               BolsaGeometrica myService = (BolsaGeometrica) rmiRegistry.lookup("myRMIBG");
               rmiRegistry.unbind("myRMIBG");
           } catch (NoSuchObjectException e) {
               e.printStackTrace();
           } catch (NotBoundException e) {
               e.printStackTrace();
           } catch (RemoteException ex) {
           }
           System.exit(0);
    }


   public static void main(String[] args) {
      try {

        //System.out.println("Configurando servidor ...");
        setServer();
         // Instancia o objeto servidor e a sua stub
         BGServer server = new BGServer();
         BolsaGeometrica stub = (BolsaGeometrica) UnicastRemoteObject.exportObject(server, 0);
         // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
         Registry registry = LocateRegistry.createRegistry(6600);
         //Registry registry = LocateRegistry.getRegistry(9999);
         registry.bind("myRMIBG", stub);
         //System.out.println("Servidor pronto:\n\t\tNroClientes:"+getNroClient()+"\n\t\tPecas:"+getNroPecas()+"\n");
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

// Métodos disponíveis via RMI

 public void endClient() throws RemoteException {
   try {
       setNroClient(getNroClient()-1);
       //System.out.println("Clientes ativos = "+this.getNroClient());
       if (this.getNroClient()==0) {
           this.stopServer();
       }
   } catch (Exception e) {
      e.printStackTrace();
   }
 }

}

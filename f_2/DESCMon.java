/** HelloServer.java **/

import java.io.*;
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.concurrent.TimeUnit;

public class DESCMon {
  public DESCMon() {}

    private static int NClientes;
    private static String[] pecas;
    private static Registry registry;

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
        String[] word = sCurrentLine.split("=");
        switch (word[0]) {
          case "NofClient":
          setNroClient(Integer.parseInt(word[1]));
          break;

          // continuar código da leitura das configurações

          default:
          //System.out.println("Ignorado: ("+word[0]+")");
        }
      }

      sc.close();
    }

    static void printReport() {
      System.out.println("*** Log DESCMon File ***");
      System.out.println("T001;LIDO;arqbla1.map;input;");
      System.out.println("T002;DIVIDO;arqbla1.p01;tempfiles;");
      System.out.println("T003;DIVIDO;arqbla1.p02;tempfiles;");
      System.out.println("T004;UPLOAD;arqbla1.p02;grupo_g;user1TOKEN;");
      System.out.println("T005;UPLOAD;arqbla1.p01;grupo_g;user2TOKEN;");
      System.out.println("T006;DOWNLOAD;arqbla1.p01;grupo_g;user1TOKEN;");
      System.out.println("T007;DOWNLOAD;arqbla1.p02;grupo_g;user2TOKEN;");
      System.out.println("T008;CONCAT;arqbla1.map;output;");
      System.out.println("****** End DESCMon ******");
    }

    public static void main(String[] args) {
      try {

        //System.out.println("Configurando servidor ...");
        setServer();
        // Instancia o objeto servidor e a sua stub
        String clientSentence = "1";
        Integer cont=0;
        ServerSocket welcomeSocket = new ServerSocket(2121);

        // System.out.println("Servidor pronto ...");

        while (getNroClient() != 0) {
            Socket connectionSocket = welcomeSocket.accept();
            setNroClient(getNroClient()-1);
            BufferedReader inFromClient
                    = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient
                    = new DataOutputStream(connectionSocket.getOutputStream());

            clientSentence = inFromClient.readLine();
            // System.out.println("Servidor linha("+cont+"): "+clientSentence+" ("+getNroClient()+")");
            clientSentence
                    = "RServidor: " + clientSentence + '\n';
            outToClient.writeBytes(clientSentence);
        }
        printReport();
        // System.out.println("Servidor finalizado com sucesso!");


      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

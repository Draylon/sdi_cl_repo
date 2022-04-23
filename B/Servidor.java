  import java.io.*;
  import java.net.*;

  class Servidor {

      public static void main(String argv[]) throws Exception {
          String clientSentence = "1";
          Integer cont=0;
          ServerSocket welcomeSocket = new ServerSocket(2121);

          System.out.println("Servidor pronto ...");

          while (true) {
              Socket connectionSocket = welcomeSocket.accept();
              BufferedReader inFromClient
                      = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
              DataOutputStream outToClient
                      = new DataOutputStream(connectionSocket.getOutputStream());

              clientSentence = inFromClient.readLine();
              System.out.println("Servidor linha("+cont+"): "+clientSentence);
              clientSentence
                      = "RServidor: " + clientSentence + '\n';
  	          outToClient.writeBytes(clientSentence);

              cont++;
  	           if (cont==2) {
                 System.out.println("Servidor finalizado com sucesso!");
                 System.exit(0);
               }
          }
      }
  }

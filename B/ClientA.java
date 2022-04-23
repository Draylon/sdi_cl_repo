import java.io.*;
import java.net.*;
import java.util.*;

class ClientA {

  public static void main(String argv[]) throws Exception {
      String sentence;
      String modifiedSentence;
      BufferedReader br = null;
      FileReader fr = null;

      Scanner sc = new Scanner(System.in);
      while(true){
          if (!sc.hasNextLine()) {
              break;
          }
          String sCurrentLine = sc.nextLine();
          BufferedReader inFromUser
                = new BufferedReader(
                      new InputStreamReader(System.in));
          Socket clientSocket = new Socket("ens1", 2121);

          DataOutputStream outToServer
                = new DataOutputStream(
                      clientSocket.getOutputStream());
          BufferedReader inFromServer
                = new BufferedReader(
                      new InputStreamReader(clientSocket.getInputStream()));

          outToServer.writeBytes("ClientA:" + sCurrentLine + '\n');
          modifiedSentence = inFromServer.readLine();
          System.out.println(modifiedSentence);
          clientSocket.close();
      }
  }
}

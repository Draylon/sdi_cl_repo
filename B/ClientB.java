import java.io.*;
import java.net.*;

class ClientB {

  private static final String FILENAME = "file.txt";


  public static void main(String argv[]) throws Exception {
      String sentence;
      String modifiedSentence;
      BufferedReader br = null;
      FileReader fr = null;

      try {
          fr = new FileReader(FILENAME);
          br = new BufferedReader(fr);
          String sCurrentLine;
          br = new BufferedReader(new FileReader(FILENAME));
          while ((sCurrentLine = br.readLine()) != null) {
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

              outToServer.writeBytes("ClientB:" + sCurrentLine + '\n');
              modifiedSentence = inFromServer.readLine();
              System.out.println(modifiedSentence);
              clientSocket.close();
          }
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          try {
            if (br != null) {
              br.close();
            }
            if (fr != null) {
              fr.close();
            }
          } catch (IOException ex) {
            ex.printStackTrace();
          }
      }

  }
}

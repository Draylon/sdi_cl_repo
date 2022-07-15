//https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/

package com.wsudesc.app;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Properties;


public class DESCCli {

  public static Socket getJob() throws Exception{
    String sCurrentLine = "Teste";
    Socket clientSocket = new Socket("ens1", 2121);

    DataOutputStream outToServer
          = new DataOutputStream(
                clientSocket.getOutputStream());
    BufferedReader inFromServer
          = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

    outToServer.writeBytes("ClientA:" + sCurrentLine + '\n');
    String modifiedSentence = inFromServer.readLine();
    return clientSocket;
  }

  public static void main(String args[]) throws DbxException, IOException {

    try {

      Socket clSocket = getJob();

    // Create Dropbox client
    String filepath =  args[0];
    DropboxConfig dbconf = new DropboxConfig(filepath+"/dbU1Token.properties");
    DbxClientV2 client = dbconf.DropboxClient();

    // Get current account info
    FullAccount account = client.users().getCurrentAccount();
    //System.out.println(account.getName().getDisplayName());

    // Get files and folder metadata from Dropbox root directory
    ListFolderResult result = client.files().listFolder("");
    while (true) {
      for (Metadata metadata : result.getEntries()) {
        //System.out.println(metadata.getPathLower());
      }

      if (!result.getHasMore()) {
        break;
      }

      result = client.files().listFolderContinue(result.getCursor());
    }


    // Upload "test.txt" to Dropbox
    int myname = (int)(Math.random() * 50000 + 1);
    try (InputStream in = new FileInputStream("../input/file10k.map")) {
      FileMetadata metadata = client.files().uploadBuilder("/grupo_g/part-"+myname+".map").uploadAndFinish(in);
    }

    String localPath = "../tempfiles/part-"+myname+".map";
    OutputStream outputStream = new FileOutputStream(localPath);
    FileMetadata metadata = client.files()
    .downloadBuilder("/grupo_g/part-"+myname+".map")
    .download(outputStream);

      // Chama o método do servidor e imprime a mensagem
      //System.out.println(modifiedSentence);
      clSocket.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
  }
}

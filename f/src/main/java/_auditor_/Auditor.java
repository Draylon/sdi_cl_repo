//https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/
//https://dropbox.tech/developers/migrating-app-permissions-and-access-tokens
//https://www.dropboxforum.com/t5/Dropbox-API-Support-Feedback/How-to-automate-getting-a-new-token-using-Java/td-p/584555

package com.wsudesc.app;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import java.security.MessageDigest;
import java.util.*;
import java.io.*;
import java.util.Properties;

public class Auditor {
  private static DbxClientV2 u1TOKEN;
  private static DbxClientV2 u2TOKEN;
  private static DbxClientV2 u3TOKEN;

  public static byte[] createChecksum(String filename) throws Exception {
    InputStream fis =  new FileInputStream(filename);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
      numRead = fis.read(buffer);
      if (numRead > 0) {
        complete.update(buffer, 0, numRead);
      }
    } while (numRead != -1);

    fis.close();
    return complete.digest();
  }

  // see this How-to for a faster way to convert
  // a byte array to a HEX string
  public static String getMD5Checksum(String filename) throws Exception {
    byte[] b = createChecksum(filename);
    String result = "";

    for (int i=0; i < b.length; i++) {
      result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    }
    return result;
  }

  public static void fileconcat (String nfile1, String nfile2, String nfileout) throws Exception {
    // open file input stream to the first file file2.txt
    InputStream in = new FileInputStream(nfile1);
    byte[] buffer = new byte[1 << 20];  // loads 1 MB of the file

    // open file output stream to which files will be concatenated.
    // Arquivo reconstruído de saída
    File file3 = new File(nfileout);
    file3.createNewFile();

    if (!file3.exists()) {
      System.out.println("File concat (ERROR)!");
      System.exit(1);
    }

    OutputStream os = new FileOutputStream(file3, true);
    int count;

    // read entire file1.txt and write it to file3.txt
    while ((count = in.read(buffer)) != -1) {
      os.write(buffer, 0, count);
      os.flush();
    }
    in.close();

    // open file input stream to the second file, file2.txt
    // Parte UM do arquivo p02
    File file2 = new File(nfile2);
    in = new FileInputStream(file2);
    if (!file2.exists()) {
      System.out.println("File concat (ERROR)!");
      System.exit(1);
    }

    // read entire file2.txt and write it to file3.txt
    while ((count = in.read(buffer)) != -1) {
      os.write(buffer, 0, count);
      os.flush();
    }
    in.close();
    os.close();
  }

  public static void transLIDO (String[] transacao) {
    String filepath = "../"+transacao[3]+"/"+transacao[2];
    File f = new File(filepath);
    if (!f.exists()) {
      System.out.println("LIDO(ERROR): "+filepath);
      System.exit(1);
    }
  }

  public static void transDIVIDO (String[] transacao) throws Exception{
    String filepath = "../"+transacao[3]+"/"+transacao[2];
    File f = new File(filepath);

    if (!f.exists()) {
      System.out.println("DIVIDO (ERROR)");
      System.exit(1);
    }

    int cod = (int)(Math.random() * 50000 + 1);
    String nfile = transacao[2].substring(0,(transacao[2].length()-4));
    fileconcat ("../"+transacao[3]+"/"+nfile+".p01",
    "../"+transacao[3]+"/"+nfile+".p02",
    "/tmp/"+nfile+'-'+cod+".aud");

    String file_rescMD5 = getMD5Checksum("/tmp/"+nfile+'-'+cod+".aud");
    String file_orgMD5 = getMD5Checksum("../input/"+nfile+".map");
    if (!file_orgMD5.equals(file_rescMD5)) {
      System.out.println("DIVIDO(ERROR)");
      System.exit(1);
    }
  }


  public static DbxClientV2 mytoken (String usertoken) throws Exception{
    if (usertoken.equals("user1TOKEN")) {
      return (u1TOKEN);
    }
    if (usertoken.equals("user2TOKEN")) {
      return (u2TOKEN);
    }
    return (u3TOKEN);
  }


  public static void transDOWN (String[] transacao) throws Exception{
    String nfile = transacao[2].substring(0,(transacao[2].length()-4));
    String fileo_MD5 = getMD5Checksum("../tempfiles/"+transacao[2]);

    DbxClientV2 client = mytoken(transacao[4]);

    String localPath = "/tmp/"+transacao[2];
    OutputStream outputStream = new FileOutputStream(localPath);
    FileMetadata metadata = client.files()
    .downloadBuilder("/"+transacao[3]+"/"+transacao[2])
    .download(outputStream);

    String filea_MD5 = getMD5Checksum(localPath);

    if (!filea_MD5.equals(fileo_MD5)) {
      System.out.println("TransDOWN (ERROR)");
      System.exit(1);
    }
  }

  public static void transUP (String[] transacao) throws Exception{
    DbxClientV2 client = mytoken(transacao[4]);

    InputStream in = new FileInputStream("../tempfiles/"+transacao[2]);
    FileMetadata metadata = client.files().uploadBuilder("/"+transacao[3]+"/"+transacao[2]).uploadAndFinish(in);

    String localPath = "/tmp/"+transacao[2];
    OutputStream outputStream = new FileOutputStream(localPath);
    FileMetadata metadatad = client.files()
    .downloadBuilder("/"+transacao[3]+"/"+transacao[2])
    .download(outputStream);

    String MD5filelocal = getMD5Checksum("../tempfiles/"+transacao[2]);
    String MD5fileup = getMD5Checksum("/tmp/"+transacao[2]);
    if (!MD5fileup.equals(MD5filelocal)) {
      System.out.println("TransUP (ERROR)");
      System.exit(1);
    }
  }

  public static void transCAT (String[] transacao) throws Exception{
    String nfile = transacao[2].substring(0,(transacao[2].length()-4));

    int cod = (int)(Math.random() * 50000 + 1);
    fileconcat ("../tempfiles/"+nfile+".p01",
    "../tempfiles/"+nfile+".p02",
    "/tmp/"+nfile+'-'+cod+".out");

    String file_rescMD5 = getMD5Checksum("/tmp/"+nfile+'-'+cod+".out");
    String file_orgMD5 = getMD5Checksum("../output/"+nfile+".map");
    if (!file_orgMD5.equals(file_rescMD5)) {
      System.out.println("CONCAT(ERROR)");
      System.exit(1);
    }
  }


  public static void readLog () throws Exception {
    Scanner sc = new Scanner(System.in);

    while(true){
      if (!sc.hasNextLine()) {
        break;
      }

      String sCurrentLine = sc.nextLine();
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      String[] campo = sCurrentLine.split(";");

      if (campo[0].equals("*** Log DESCMon File ***")) {
        sCurrentLine = sc.nextLine();
        campo = sCurrentLine.split(";");
      }

      if (campo[0].equals("****** End DESCMon ******")) {
        sc.close();
        System.exit(0);
      }

      switch (campo[1]) {
        case "LIDO":
        transLIDO(campo);
        System.out.println(campo[0]+": OK");
        break;

        case "DIVIDO":
        transDIVIDO(campo);
        System.out.println(campo[0]+": OK");
        break;

        case "UPLOAD":
        transUP(campo);
        System.out.println(campo[0]+": OK");
        break;

        case "DOWNLOAD":
        transDOWN(campo);
        System.out.println(campo[0]+": OK");
        break;

        case "CONCAT":
        transCAT(campo);
        System.out.println(campo[0]+": OK");
        break;

        default:
        System.out.println("Ignorado: ("+campo[1]+")");
      }
    }

    sc.close();
  }

  public static void main(String args[]) throws DbxException, IOException {

    // Create Dropbox client
    String filepath =  args[0];
    DropboxConfig dbconf = new DropboxConfig(filepath+"/dbU1Token.properties");
    u1TOKEN = dbconf.DropboxClient();
    dbconf = new DropboxConfig(filepath+"/dbU2Token.properties");
    u2TOKEN = dbconf.DropboxClient();
    dbconf = new DropboxConfig(filepath+"/dbU3Token.properties");
    u3TOKEN = dbconf.DropboxClient();

    try {
      readLog();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

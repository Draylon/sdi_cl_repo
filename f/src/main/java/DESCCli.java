//https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.nio.file.Files;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Properties;


public class DESCCli{
    static class Pair<T,K>{
        public T fst;
        public K snd;

        public Pair(T part1, K part2) {
            fst=part1;
            snd=part2;
        }
    }

    public static Socket getJob(int port) throws Exception{
        //String sCurrentLine = "Teste";
        Socket clientSocket = new Socket("ens1", port);

        //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //outToServer.writeBytes("ClientA:" + sCurrentLine + '\n');
        //outToServer.flush();
        //String modifiedSentence = inFromServer.readLine();
        return clientSocket;
    }
    public static BufferedReader getInFromServer(Socket s) throws IOException {
        return new BufferedReader(new InputStreamReader(s.getInputStream()));
    }


    public static Pair<File, File> split_file(File file) throws IOException {
        File part1 = new File("../tmpfiles/"+file.getName()+".p01");
        File part2 = new File("../tmpfiles/"+file.getName()+".p02");
        FileInputStream fis;
        FileOutputStream fos1;
        FileOutputStream fos2;
        byte[] fileBytes;
        int bytesRead;
        fis = new FileInputStream(file);
        fos1 = new FileOutputStream(part1);
        fos2 = new FileOutputStream(part2);

        fileBytes = new byte[(int) (file.length()/2)];
        bytesRead = fis.read(fileBytes, 0, (int) (file.length() / 2));
        fos1.write(fileBytes);
        bytesRead = fis.read(fileBytes, (int) (file.length() / 2) , (int) file.length());
        fos2.write(fileBytes);
        fos1.flush();
        fos2.flush();
        fileBytes = null;
        fis.close();
        fis = null;
        fos1.close();
        fos2.close();
        fos1 = null;
        fos2 = null;
        return new Pair<File, File>(part1, part2);
    }


    public static File merge_file(Pair<File,File> pair) throws IOException {
        File juntar = new File("../output/"+pair.fst.getName().split(".p01")[0]);
        FileOutputStream fos;
        FileInputStream fis1,fis2;
        byte[] fileBytes;
        int bytesRead;

        fos = new FileOutputStream(juntar, true);

        fis1 = new FileInputStream(pair.fst);
        fis2 = new FileInputStream(pair.snd);

        fileBytes = new byte[(int) pair.fst.length()];
        bytesRead = fis1.read(fileBytes, 0, (int) pair.fst.length());
        fos.write(fileBytes);

        fileBytes = new byte[(int) pair.snd.length()];
        bytesRead = fis2.read(fileBytes, 0, (int) pair.snd.length());
        fos.write(fileBytes);

        fos.flush();
        fileBytes = null;
        fis1.close(); fis1 = null;
        fis2.close(); fis2 = null;

        fos.close(); fos = null;
        return juntar;
    }


    public static String last_added_database = "";
    public static boolean done = false;

    public static void upload_file(File f,String filename,String filepath) throws IOException, DbxException {

        String key_name;
        switch (last_added_database) {
            case "dbU1Token":
                key_name = "dbU2Token";
                break;
            case "dbU2Token":
                key_name = "dbU3Token";
                break;
            case "dbU3Token":
                key_name = "dbU1Token";
                break;
            default:
                key_name = "dbU1Token";
        }
        last_added_database = key_name;

        DropboxConfig dbconf = new DropboxConfig(filepath + "/" + key_name + ".properties");
        DbxClientV2 client = dbconf.DropboxClient();

        try (InputStream in = Files.newInputStream(f.toPath())) {
            FileMetadata metadata = client.files().uploadBuilder("/grupo_g_db/part-" + filename).uploadAndFinish(in);
        }
    }


    public static void main(final String[] args) {

        ArrayList<Thread> thread_array = new ArrayList<>();
        for(int i=0;i < 4;i++){
            thread_array.add( new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!done) {
                        try {
                            Socket clSocket = getJob(7471);//dbU2Token

                            DataOutputStream outToServer = new DataOutputStream(clSocket.getOutputStream());

                            BufferedReader inFromServer = getInFromServer(clSocket);
                            String filename = inFromServer.readLine();
                            if(filename == "_HALT_") {
                                done = true;
                                throw new Exception();
                            }
                            File f = new File(filename);


                            Pair<File, File> split_pair = split_file(f);

                            upload_file(split_pair.fst,f.getName()+".p01",args[0]);
                            outToServer.writeBytes(last_added_database);
                            upload_file(split_pair.snd,f.getName()+".p02",args[0]);
                            outToServer.writeBytes(last_added_database);

                            File merged = merge_file(split_pair);
                            outToServer.writeBytes("output");



                            // Chama o método do servidor e imprime a mensagem
                            //System.out.println(modifiedSentence);
                            clSocket.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }));
        }
        for(Thread tt : thread_array){
            tt.start();
        }

        //Thread client1 = new Thread(build_runnable(args,7471));
        //Thread client2 = new Thread(build_runnable(args,7471));
        //Thread client3 = new Thread(build_runnable(args,7471));
        //client1.start();
        //client2.start();
        //client3.start();
    }

    //função não usada, só pra guardar o original
    public static void asdasd() throws IOException, DbxException {
        // Create Dropbox client
        String[] args = {"stonks"};
        String key_name = "stonks";
        String filepath = args[0];
        DropboxConfig dbconf = new DropboxConfig(filepath + "/" + key_name + ".properties");
        DbxClientV2 client = dbconf.DropboxClient();

        // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        //System.out.println(account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        ArrayList<File> files_in_account = new ArrayList<>();
        while (true){
            for (Metadata metadata : result.getEntries()) {
                files_in_account.add(new File(metadata.getPathLower()));
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }


        ///======================
        // lista todos os arquivos aqui (na gambiarra)
        ArrayList<File> files_list = null;
        File files_directory = new File("../input");
        for (String file_dir : Objects.requireNonNull(files_directory.list())) {
            files_list.add(new File(file_dir));
        }



        // Upload "test.txt" to Dropbox
        int myname = (int) (Math.random() * 50000 + 1);
        try (InputStream in = new FileInputStream("../input/file10k.map")) {
            FileMetadata metadata = client.files().uploadBuilder("/grupo_g_db/part-" + myname + ".map").uploadAndFinish(in);
        }

        String localPath = "../tempfiles/part-" + myname + ".map";
        OutputStream outputStream = new FileOutputStream(localPath);
        FileMetadata metadata = client.files()
                .downloadBuilder("/grupo_g_db/part-" + myname + ".map")
                .download(outputStream);
    }
}

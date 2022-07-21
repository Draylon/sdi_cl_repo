/** HelloServer.java **/


import java.io.*;
import java.net.*;
import java.util.*;


public class DESCMon {
    static class Pair<T,K>{
        public T fst;
        public K snd;

        public Pair(T part1, K part2) {
            fst=part1;
            snd=part2;
        }
    }
  public DESCMon() {}

    static int nofClient;
    static String dbDIR;
    static String user1TOKEN;
    static String user2TOKEN;
    static String user3TOKEN;

    public static void setServer () throws Exception {
      Scanner sc = new Scanner(System.in);
      while(true){
        if (!sc.hasNextLine()) {
          break;
        }
        String sCurrentLine = sc.nextLine();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String[] word = sCurrentLine.split("=");
        switch (word[0]) {
            case "NofClient":
                nofClient =  Integer.parseInt(word[1]);
                break;
            case "dbDIR":
                dbDIR = word[1];
                break;
            case "user1TOKEN":
                user1TOKEN = word[1];
                break;
            case "user2TOKEN":
                user2TOKEN = word[1];
                break;
            case "user3TOKEN":
                user3TOKEN = word[1];
                break;
          default:
          //System.out.println("Ignorado: ("+word[0]+")");
        }
      }

      sc.close();
    }

    static void printReport(){
      System.out.println("*** Log DESCMon File ***");
      System.out.println("T001;LIDO;arqbla1.map;input;");
      System.out.println("T002;DIVIDO;arqbla1.p01;tempfiles;");
      System.out.println("T003;DIVIDO;arqbla1.p02;tempfiles;");
      System.out.println("T004;UPLOAD;arqbla1.p02;grupo_g_db;user1TOKEN;");
      System.out.println("T005;UPLOAD;arqbla1.p01;grupo_g_db;user2TOKEN;");
      System.out.println("T006;DOWNLOAD;arqbla1.p01;grupo_g_db;user1TOKEN;");
      System.out.println("T007;DOWNLOAD;arqbla1.p02;grupo_g_db;user2TOKEN;");
      System.out.println("T008;CONCAT;arqbla1.map;output;");
      System.out.println("****** End DESCMon ******");
    }

    static ArrayList<File> files;
    static ArrayList<Pair<File,File>> parts;

    public static Pair<File, File> split_file(File file) throws IOException {
        File part1 = new File(file.getName()+".p01");
        File part2 = new File(file.getName()+".p02");
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
        File juntar = new File(pair.fst.getName().split(".p01")[0]);
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

    static ArrayList<File> parts_linear = new ArrayList<>();
    static boolean parts_lock=false;
    static int parts_iter=0;

    public static void main(String[] args) {
        try {

        setServer();

        System.out.println("*** Log DESCMon File ***");


        // https://stackoverflow.com/questions/4431945/split-and-join-back-a-binary-file-in-java

        ArrayList<File> files_list = null;
        File files_directory = new File("../input");
        for(String file_dir : Objects.requireNonNull(files_directory.list())){
            files_list.add(new File(file_dir));
        }

        ServerSocket filename_socket = new ServerSocket(7471);
        for(File f: files_list){

            Socket uploaded_file = filename_socket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(uploaded_file.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(uploaded_file.getOutputStream());

            System.out.println("T000;LIDO;"+f.getName()+";input;");
            System.out.println("T000;DIVIDO;"+f.getName()+".p01;tempfiles;");
            System.out.println("T000;DIVIDO;"+f.getName()+".p02;tempfiles;");

            outToClient.writeBytes(f.getName());
            outToClient.flush();
            
            String part1_token = inFromClient.readLine();
            String part2_token = inFromClient.readLine();

            System.out.println("T000;UPLOAD;arqbla1.p02;grupo_g_db;"+part1_token+";");
            System.out.println("T000;UPLOAD;arqbla1.p01;grupo_g_db;"+part2_token+";");
            System.out.println("T000;DOWNLOAD;arqbla1.p01;grupo_g_db;"+part1_token+";");
            System.out.println("T000;DOWNLOAD;arqbla1.p02;grupo_g_db;"+part2_token+";");
            System.out.println("T000;CONCAT;"+f.getName()+";output;");
        }

        Socket cancel = filename_socket.accept();
        DataOutputStream cl = new DataOutputStream(cancel.getOutputStream());
        cl.writeBytes("_HALT_");
        cl.flush();
        /*
        //listar todos os arquivos em ../input
        int transacao=0;

        for(File f : files_list){
            parts.add(split_file(f));
        }


        for(Pair<File,File> pp : parts){
            parts_linear.add(pp.fst);
            parts_linear.add(pp.snd);
        }

        int iter = 1;
        for(File part_file : parts_linear){
            //send part

        }

        ServerSocket cl1_sock = new ServerSocket(7471);
        ServerSocket cl2_sock = new ServerSocket(7472);
        ServerSocket cl3_sock = new ServerSocket(7473);

        Thread client1 = new Thread(()->{
            try {
                Socket connectionSocket = cl1_sock.accept();
                File upload_part = parts_linear.get(parts_iter);
                parts_iter++;

            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        //System.out.println("Configurando servidor ...");

        // Instancia o objeto servidor e a sua stub
        String clientSentence = "1";
        Integer cont=0;
        ServerSocket welcomeSocket = new ServerSocket(2121);

        // System.out.println("Servidor pronto ...");

        while (nofClient != 0) {
        Socket connectionSocket = welcomeSocket.accept();
        nofClient-=1;
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        clientSentence = inFromClient.readLine();
        // System.out.println("Servidor linha("+cont+"): "+clientSentence+" ("+getNroClient()+")");
        clientSentence = "RServidor: " + clientSentence + '\n';
        outToClient.writeBytes(clientSentence);
        }




        printReport();
        // System.out.println("Servidor finalizado com sucesso!");
        */

        } catch (Exception ex) {
        ex.printStackTrace();
        }
    }
}
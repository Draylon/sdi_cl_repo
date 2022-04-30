/** HelloClient.java **/
import java.io.*;
import java.util.*;
import java.rmi.registry.*;
import java.net.InetAddress;

public class Client {

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        String cl_id = args[1];
        try {
            // Obtém uma referência para o registro do RMI
            Registry registry = LocateRegistry.getRegistry(host,6622);

            // Obtém a stub do servidor
            BolsaGeometrica stub= (BolsaGeometrica) registry.lookup("rmiGrupoG");

            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();

            // Começar implementacao

            Scanner sc = new Scanner(System.in);
            Boolean clFlag = false;
            String peca_id = null;
            int peca_qt = 0;
            Boolean request_complete=false;
            while(true){
                if (!sc.hasNextLine() || request_complete) {
                    break;
                }
                
                String sCurrentLine = sc.nextLine();
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                String[] word = sCurrentLine.split(" ");

                if(word[0].equals("## Clientes ##")){
                    clFlag=true;
                }else if(word[0].equals("pCli"+cl_id)){
                    peca_id=word[2];
                }else if(word[0].equals("QtdCli"+cl_id)){
                    peca_qt=Integer.parseInt(word[2]);
                    request_complete=true;
                }else{
                    //System.out.println("Ignorado: ("+word[0]+")");
                }
            }
            sc.close();
            if(request_complete){
                //stub.requestPeca1(new PecaReq().setPeca_id(peca_id).setPeca_qt(peca_qt));
                stub.requestPeca2(peca_id,peca_qt);
                System.out.println("##  Cliente ("+hostname+") "+args[1]+"  ##");
                System.out.println("Status: atendido por "+args[0]);
                System.out.println("pCli: "+peca_id);
                System.out.println("###########");
            }
            /*
            //Template do print
            System.out.println("##  Cliente ("+hostname+") "+args[1]+"  ##");
            System.out.println("Status: atendido por "+args[0]);
            if (args[1].equals("1")) {
            System.out.println("pCli: B");
            } else {
            System.out.println("pCli: J");
            }
            System.out.println("###########");
            */


            // Chama o método do servidor e imprime a mensagem
            stub.endClient();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

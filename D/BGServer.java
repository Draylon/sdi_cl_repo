/** HelloServer.java **/

import java.io.*;
import java.rmi.*;
import java.net.*;
import java.util.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.concurrent.TimeUnit;

public class BGServer implements BolsaGeometrica {
  public BGServer() {}

    private static int NClientes;
    private static String[] pecas;
    private static ArrayList<String> entregas;
    private static Registry registry;
    private static BolsaGeometrica stub;

    public static void setNroClient(int nclients) throws Exception {
      NClientes = nclients;
    }

    public static int getNroClient() throws Exception {
      return(NClientes);
    }

    public static void setPecas(String[] lpecas) throws Exception {
        pecas = new String[lpecas.length-2];
        System.arraycopy(lpecas,2,pecas,0,lpecas.length-2);
    }

    public static String getPeca(int posicao) throws Exception {
      return(pecas[posicao]);
    }

    public static int getNroPecas() throws Exception {
      return(pecas.length);
    }

    @Override
    public void requestPeca(PecaReq q) throws RemoteException {
        if(q.peca_qt==0) System.out.println("EMPTY REQUEST");
        for(int j=0;j<q.peca_qt;j++){
            for (int i=0;i<pecas.length;i++) {
                if(Objects.equals(pecas[i], q.peca_id)){
                    pecas[i]="_";
                    entregas.add(q.peca_id);
                    break;
                }
            }
        }
    }

    public static void setServer () throws Exception {
        Scanner sc = new Scanner(System.in);
        while(true){
            if (!sc.hasNextLine()) {
                break;
        }
        String sCurrentLine = sc.nextLine();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
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

    public static void stopServer() {
        try {
            registry.unbind("myRMIBG");
            UnicastRemoteObject.unexportObject(registry,true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException ex) {
        }

    }

    static void printSorted(String[] v){
        Arrays.stream(v).sorted().forEach((String peca)->{
            if(!Objects.equals(peca, "_")){
                System.out.print(peca+" ");
            }
        });
        System.out.println();
    }

    static void printReport() {
        System.out.println("##  Servidor  ##");
        System.out.println("Status: finalizado");
        System.out.println("estoque: ");
        printSorted(pecas);
        System.out.println("entregas: ");
        printSorted(entregas.stream().toArray(String[]::new));
    }

    public static void main(String[] args) {
        try {
            //System.out.println("Configurando servidor ...");
            setServer();
            // Instancia o objeto servidor e a sua stub
            BGServer server = new BGServer();
            //BolsaGeometrica stub = (BolsaGeometrica) UnicastRemoteObject.exportObject(server, 0);
            stub = (BolsaGeometrica) UnicastRemoteObject.exportObject(server, 0);
            // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
            registry = LocateRegistry.createRegistry(6600);
            //Registry registry = LocateRegistry.createRegistry(6600);
            //Registry registry = LocateRegistry.getRegistry(9999);
            registry.bind("rmiGrupoG", stub);

            //System.out.println("Servidor pronto:\n\t\tNroClientes:"+getNroClient()+"\n\t\tPecas:"+getNroPecas()+"\n");
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Boolean flag = true;
        while (flag) { // Pooling aguardando clientes
            try {
                TimeUnit.SECONDS.sleep(1);
                if (getNroClient() <= 0) {
                    flag = false;
                    printReport();
                    stopServer();
                    // ...
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.exit(0);

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

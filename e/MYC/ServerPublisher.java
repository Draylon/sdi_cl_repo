package MYC;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetAddress;
import javax.xml.namespace.QName;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.xml.ws.Service;
import javax.xml.ws.Endpoint;
import java.util.*;
import java.io.*;

public class ServerPublisher {

    static <T> T makeService(String port,String Servico,String host,Class<T> cltype) throws MalformedURLException {
        URL url1 = new URL("http://"+host+":"+port+"/"+Servico+"?wsdl");
        QName qname1 = new QName("http://MYC/",
                Servico+"ServerImplService");
        return Service.create(url1, qname1).getPort(cltype);
    }

    static void printReport(String host) throws Exception {

        WSRecepcaoServer wsRecepcaoServer = makeService("9715","WSRecepcao",host,WSRecepcaoServer.class);
        WSRecheioServer wsRecheioServer = makeService("9718","WSRecheio",host,WSRecheioServer.class);
        WSCortesServer wsCortesServer = makeService("9717","WSCortes",host,WSCortesServer.class);
        WSCoberturaServer wsCoberturaServer = makeService("9719","WSCobertura",host,WSCoberturaServer.class);
        WSPandeloServer wsPandeloServer = makeService("9716","WSPandelo",host,WSPandeloServer.class);

        System.out.println("##  Servidor  ##");
        System.out.println("Status: finalizado");
        System.out.println("Nro_clientes_atendidos: "+wsRecepcaoServer.getNroClient());
        System.out.println("WS-Pandelo (ens1): "+wsPandeloServer.getFeitos());
        System.out.println("WS-Cobertura (ens1): "+wsCoberturaServer.getFeitos());
        System.out.println("WS-Recheio (ens1): "+wsRecheioServer.getFeitos());
        System.out.println("WS-Cortes (ens1): "+wsCortesServer.getFeitos());
        System.out.println("WS-Recepcao (ens1): "+wsRecepcaoServer.getRequests());
        System.out.println("###########");
    }

    static void readSetup (String host, WSRecepcaoServer srecepcao){

        try {
            Scanner sc = new Scanner(System.in);
            while(true){
                if (!sc.hasNextLine()) {
                    break;
                }
                String newline = sc.nextLine();
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                String[] word = newline.split(" ");
                switch (word[0]) {
                    case "NClientes":
                        srecepcao.setServer(Integer.parseInt(word[2]));
                        break;
                    // continuar código da leitura das configurações
                    default:
                        // System.out.println("Ignorado: ("+word[0]+")");
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        String host = args[0];

        System.out.println("* SERVER: Beginning to publish WS Servers ("+host+") *");

        // WS Recepcao
        Endpoint ep = Endpoint.create(new WSRecepcaoServerImpl());
        ep.publish("http://"+host+":9715/WSRecepcao");

        // WS Pandelo
        Endpoint ep_pan = Endpoint.create(new WSPandeloServerImpl());
        ep_pan.publish("http://"+host+":9716/WSPandelo");

        // WS Cortes
        Endpoint ep_cor = Endpoint.create(new WSCortesServerImpl());
        ep_cor.publish("http://"+host+":9717/WSCortes");

        // WS Recheio
        Endpoint ep_rec = Endpoint.create(new WSCortesServerImpl());
        ep_rec.publish("http://"+host+":9718/WSRecheio");

        // WS Cobertura
        Endpoint ep_cob = Endpoint.create(new WSCortesServerImpl());
        ep_cob.publish("http://"+host+":9719/WSCobertura");


        System.out.println("* All done publishing. *");

        try {
            // ##### WS Recepcao  #####
            URL url1 = new URL("http://"+host+":9715/WSRecepcao?wsdl");
            QName qname1 = new QName("http://MYC/",
                    "WSRecepcaoServerImplService");
            Service recepcao = Service.create(url1, qname1);
            WSRecepcaoServer srecepcao = recepcao.getPort(WSRecepcaoServer.class);
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            srecepcao.setHost(host);
            readSetup(host, srecepcao);
            Boolean flag = true;
            while (flag) { // Pooling aguardando clientes
                TimeUnit.SECONDS.sleep(1);
                if (srecepcao.getNroClient() <= 0) {
                    System.out.println("* Server End *");
                    flag = false;
                    printReport(host);
                    ep.stop();
                    ep_pan.stop();
                    ep_cor.stop();
                    ep_rec.stop();
                    ep_cob.stop();

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

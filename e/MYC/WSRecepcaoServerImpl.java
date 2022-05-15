package MYC;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.io.*;

@WebService(endpointInterface = "MYC.WSRecepcaoServer")
public class WSRecepcaoServerImpl implements WSRecepcaoServer {

    private static int NClientes;
    private static String host;

    public void endClient() throws Exception {
        NClientes--;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    public int getNroClient() throws Exception {
        return(NClientes);
    }

    public static void setNroClient(int nclients) throws Exception {
        NClientes = nclients;
    }

    public void setServer(Integer nclients) {
        //System.out.println("["+nclients+"]");
        try {
            setNroClient(nclients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int requests=0;
    @Override
    public int getRequests() {
        return requests;
    }

    static <T> T makeService(String port,String Servico, String host, Class<T> cltype) throws MalformedURLException {
        requests++;
        URL url1 = new URL("http://"+host+":"+port+"/"+Servico+"?wsdl");
        QName qname1 = new QName("http://MYC/",
                Servico+"ServerImplService");
        return Service.create(url1, qname1).getPort(cltype);
    }

    @Override
    public void solicitaCobertura(String cl,String tipo) throws Exception {
        requests++;
        WSCoberturaServer svc = makeService("9719","WSCobertura",host,WSCoberturaServer.class);
        System.out.println(svc.getCobertura(cl));
    }

    @Override
    public void solicitaCorte(String cl,String tipo) throws Exception {
        requests++;
        WSCortesServer svc = makeService("9717","WSCortes",host,WSCortesServer.class);
        System.out.println(svc.getCortes(cl));
    }

    @Override
    public void solicitaPandelo(String cl,String tipo) throws Exception {
        requests++;
        WSPandeloServer svc = makeService("9716","WSPandelo",host,WSPandeloServer.class);
        System.out.println(svc.getPandelo(cl));
    }

    @Override
    public void solicitaRecheio(String cl,String tipo) throws Exception {
        requests++;
        WSRecheioServer svc = makeService("9718","WSRecheio",host,WSRecheioServer.class);
        System.out.println(svc.getRecheio(cl));
    }
}

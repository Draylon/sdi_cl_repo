package MYC;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.*;
import java.io.*;

@WebService(endpointInterface = "MYC.WSRecepcaoServer")
public class WSRecepcaoServerImpl implements WSRecepcaoServer {

  private static int NClientes;

  public void endClient() throws Exception {
    NClientes--;
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

}

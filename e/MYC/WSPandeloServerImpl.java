package MYC;

import javax.jws.WebService;

@WebService(endpointInterface = "MYC.WSPandeloServer")
public class WSPandeloServerImpl implements WSPandeloServer {

	public String getPandelo(String host, String name) {
    System.out.println("##  Cliente ("+host+") "+name+"  ##");
    System.out.println("Status: Pronto");
    System.out.println("WS-Pandelo: 2");
    System.out.println("WS-Cobertura: 1");
    System.out.println("WS-Recheio: 4");
    System.out.println("WS-Cortes:2");
    System.out.println("###########");
		return "Pandelo de " + name + " entregue!";
	}

}

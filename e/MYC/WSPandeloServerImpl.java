package MYC;

import javax.jws.WebService;

@WebService(endpointInterface = "MYC.WSPandeloServer")
public class WSPandeloServerImpl implements WSPandeloServer {

    private static int feitos=0;

    public int getFeitos() {
        return feitos;
    }

    public String getPandelo(String name) {
        feitos++;
		return "Pandelo de " + name + " entregue!";
	}

}
/*System.out.println("##  Cliente ("+host+") "+name+"  ##");
        System.out.println("Status: Pronto");
        System.out.println("WS-Pandelo: 2");
        System.out.println("WS-Cobertura: 1");
        System.out.println("WS-Recheio: 4");
        System.out.println("WS-Cortes:2");
        System.out.println("###########");*/

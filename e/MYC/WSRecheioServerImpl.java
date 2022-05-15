package MYC;

import javax.jws.WebService;

@WebService(endpointInterface = "MYC.WSRecheioServer")
public class WSRecheioServerImpl implements WSRecheioServer {

    private static int feitos;

    public int getFeitos() {
        return feitos;
    }

    public String getRecheio(String name) {
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

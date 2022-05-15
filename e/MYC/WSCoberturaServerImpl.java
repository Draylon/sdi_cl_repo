package MYC;

import javax.jws.WebService;

@WebService(endpointInterface = "MYC.WSCoberturaServer")
public class WSCoberturaServerImpl implements WSCoberturaServer {

    private static int feitos;

	public String getCobertura(String name) {

		return "Pandelo de " + name + " entregue!";
	}

    public int getFeitos() {
        return feitos;
    }

}
/*System.out.println("##  Cliente ("+host+") "+name+"  ##");
        System.out.println("Status: Pronto");
        System.out.println("WS-Pandelo: 2");
        System.out.println("WS-Cobertura: 1");
        System.out.println("WS-Recheio: 4");
        System.out.println("WS-Cortes:2");
        System.out.println("###########");*/

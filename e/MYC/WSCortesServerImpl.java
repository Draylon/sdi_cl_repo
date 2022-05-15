package MYC;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

@WebService(endpointInterface = "MYC.WSCortesServer")
public class WSCortesServerImpl implements WSCortesServer {

	public String getCortes(String name) {
		return "Corte " + name + " efetuado!";
	}

	public int getFeitos() {
		return feitos;
	}

	private static int feitos;


}

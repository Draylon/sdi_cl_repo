package MYC;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.net.MalformedURLException;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WSRecepcaoServer {

	@WebMethod
	public void setServer(Integer nclients);
	//public static void setEndpoint(Endpoint myep);
	public int getNroClient() throws Exception;
	public void endClient() throws Exception;

	public void setHost(String host);

	public int getRequests();

	public void solicitaCobertura(String cl,String tipo) throws Exception;
	public void solicitaCorte(String cl,String tipo)throws Exception;
	public void solicitaPandelo(String cl,String tipo)throws Exception;
	public void solicitaRecheio(String cl,String tipo)throws Exception;

}

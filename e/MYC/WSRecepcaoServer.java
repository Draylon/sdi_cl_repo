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
	@WebMethod
	public int getNroClient() throws Exception;
	public int getNroClient2() throws Exception;
	@WebMethod
	public void endClient() throws Exception;
	@WebMethod
	public void setHost(String host);
	@WebMethod
	public int getRequests();
	@WebMethod
	public void solicitaCobertura(String tipo) throws Exception;
	@WebMethod
	public void solicitaCorte(String tipo)throws Exception;
	@WebMethod
	public void solicitaPandelo(String tipo)throws Exception;
	@WebMethod
	public void solicitaRecheio(String tipo)throws Exception;

}

package MYC;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WSRecheioServer {

	@WebMethod
	String getRecheio(String name);

	@WebMethod
	int getFeitos();
}

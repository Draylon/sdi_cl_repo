package MYC;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WSCoberturaServer {

	@WebMethod
	String getCobertura(String name);

	@WebMethod
	int getFeitos();
}

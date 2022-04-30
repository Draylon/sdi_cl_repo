/** HelloWorld.java **/
import java.rmi.*;

public interface BolsaGeometrica extends Remote {
   public void endClient() throws RemoteException;
   public void requestPeca1(PecaReq q) throws RemoteException;
   public void requestPeca2(String id,int qt) throws RemoteException;
}

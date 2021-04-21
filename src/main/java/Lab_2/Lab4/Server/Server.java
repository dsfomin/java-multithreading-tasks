package Lab_2.Lab4.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Server {
    public static void main(String[] args) throws RemoteException {
        ServerRMIImpl serverRMIImpl = new ServerRMIImpl();
        Registry registry = LocateRegistry.createRegistry(123);
        registry.rebind("serverRMI", serverRMIImpl);
        System.out.println("Server started!");
    }
}

package Module2.Part3;

import Module2.Part1.DAOTask4;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRmiTask4 {
    public static void main(String[] args) throws RemoteException {
        DAOTask4 serverRMIImpl = new DAOTask4();
        Registry registry = LocateRegistry.createRegistry(123);
        registry.rebind("serverRMI", serverRMIImpl);
        System.out.println("Server started!");
    }
}

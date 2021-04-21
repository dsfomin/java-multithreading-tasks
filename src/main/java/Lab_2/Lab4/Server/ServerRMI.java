package Lab_2.Lab4.Server;

import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface ServerRMI extends Remote {
    int addManufacturer(Manufacturer manufacturer) throws RemoteException, SQLException, IOException;
    int addCarBrandToManufacturer(Brand carBrand, String manufacturerId) throws RemoteException, IOException;
    int deleteManufacturer(String manufacturerId) throws RemoteException, SQLException, IOException;
    List<Manufacturer> getManufacturers() throws RemoteException, SQLException, IOException, ClassNotFoundException;
}

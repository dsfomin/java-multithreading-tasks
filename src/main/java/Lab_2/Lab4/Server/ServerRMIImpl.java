package Lab_2.Lab4.Server;

import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class ServerRMIImpl extends UnicastRemoteObject implements ServerRMI {

    private final AutoShowDao autoShowDao = new AutoShowDao();

    protected ServerRMIImpl() throws RemoteException {
        super();
    }

    @Override
    public int addManufacturer(Manufacturer manufacturer) {
        return autoShowDao.addManufacturer(manufacturer);
    }

    @Override
    public int addCarBrandToManufacturer(Brand carBrand, String manufacturerId) {
        return autoShowDao.addCarBrandToManufacturer(carBrand, manufacturerId);
    }

    @Override
    public int deleteManufacturer(String manufacturerId) throws SQLException {
        return autoShowDao.deleteManufacturer(manufacturerId);
    }

    @Override
    public List<Manufacturer> getManufacturers() throws SQLException {
        return autoShowDao.getManufacturers();
    }
}

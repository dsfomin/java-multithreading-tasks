package Lab_2.Lab3.Server;

import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;
import Lab_2.Lab3.Client.Request;
import Module2.Letter.ExtendedUser;
import Module2.Letter.User;
import Module2.Part1.DAOTask4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket client = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final AutoShowDao autoShowDao = new AutoShowDao();

    public ClientHandler(Socket clientSocket) throws RemoteException {
        try {
            client = clientSocket;
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (processQuery());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean processQuery() {
        try
        {
            Request request = (Request) in.readObject();
            int response = serverComputations(request);
            out.writeInt(response);
            out.flush();
            return true;
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    private int serverComputations(Request request) {
        int response = 1;
        try {
            switch (request) {
                case ADD_MANUFACTURER: {
                    autoShowDao.addManufacturer((Manufacturer) in.readObject());
                    break;
                }
                case ADD_CAR_BRAND: {
                    Brand brand = (Brand) in.readObject();
                    String manufacturerId = (String) in.readObject();
                    autoShowDao.addCarBrandToManufacturer(brand, manufacturerId);
                    break;
                }
                case DELETE_MANUFACTURER: {
                    String manufacturerId = (String) in.readObject();
                    autoShowDao.deleteManufacturer(manufacturerId);
                    break;
                }
                case GET_MANUFACTURERS: {
                    List<Manufacturer> manufacturers = autoShowDao.getManufacturers();
                    out.writeObject(manufacturers);
                    break;
                }
                default: {throw new IllegalStateException("Unknown client request");}
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = 0;
        }
        return response;
    }
}

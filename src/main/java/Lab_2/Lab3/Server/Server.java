package Lab_2.Lab3.Server;

import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;
import Lab_2.Lab3.Client.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private final AutoShowDao autoShowDao = new AutoShowDao();

    public void start(int port) throws IOException
    {
        ServerSocket server = new ServerSocket(port);
        while (true)
        {
            Socket sock = server.accept();
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            while (processQuery());
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

    public static void main(String[] args)
    {
        try
        {
            Server server = new Server();
            server.start(12345);
        }
        catch (IOException e)
        {
            System.out.println("Возникла ошибка");
        }
    }
}

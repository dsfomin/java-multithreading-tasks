package Lab_2.Lab3.Client;

import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {
    private Socket sock = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public Client(String ip, int port) {
        try {
            sock = new Socket(ip,port);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void disconnect() throws IOException { sock.close(); }

    public int addManufacturer(Manufacturer manufacturer) throws IOException {
        out.writeObject(Request.ADD_MANUFACTURER);
        out.writeObject(manufacturer);
        out.flush();

        return in.readInt();
    }

    public int addCarBrandToManufacturer(Brand carBrand, String manufacturerId) throws IOException {
        out.writeObject(Request.ADD_CAR_BRAND);
        out.writeObject(carBrand);
        out.writeObject(manufacturerId);
        out.flush();

        return in.readInt();
    }

    public int deleteManufacturer(String manufacturerId) throws IOException {
        out.writeObject(Request.DELETE_MANUFACTURER);
        out.writeObject(manufacturerId);
        out.flush();

        return in.readInt();
    }

    public void deleteBrandManufacturer(Brand carBrand, Manufacturer manufacturer) {

    }

    public List<Manufacturer> getManufacturers() throws IOException, ClassNotFoundException {
        out.writeObject(Request.GET_MANUFACTURERS);
        out.flush();

        List<Manufacturer> manufacturers = (List<Manufacturer>) in.readObject();
        in.readInt();
        return manufacturers;
    }

    public void save() {

    }
}

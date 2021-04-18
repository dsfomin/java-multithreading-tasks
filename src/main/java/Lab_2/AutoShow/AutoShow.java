package Lab_2.AutoShow;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface AutoShow extends Serializable {
    void addManufacturer(Manufacturer manufacturer) throws SQLException, IOException;
    void addCarBrandToManufacturer(Brand carBrand, String manufacturerId) throws IOException;
    void deleteManufacturer(String manufacturerId) throws SQLException;
    void deleteBrandManufacturer(Brand carBrand, Manufacturer manufacturer);
    List<Manufacturer> getManufacturers() throws SQLException;
    void save() throws JAXBException, IOException;
}

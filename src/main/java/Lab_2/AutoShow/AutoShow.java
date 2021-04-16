package Lab_2.AutoShow;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface AutoShow {
    void addManufacturer(Manufacturer manufacturer) throws SQLException;
    void addCarBrandToManufacturer(Brand carBrand, String manufacturerId);
    void deleteManufacturer(String manufacturerId) throws SQLException;
    void deleteBrandManufacturer(Brand carBrand, Manufacturer manufacturer);
    List<Manufacturer> getManufacturers() throws SQLException;
    void save() throws JAXBException, IOException;
}

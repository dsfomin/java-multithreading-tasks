package Lab_2.Lab1.AutoShow;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "autoshow")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoShow {
    @XmlElement(name = "manufacturer")
    private List<Manufacturer> manufacturers;

    public AutoShow() {
        manufacturers = new ArrayList<>();
    }

    public void saveDataToXML(String filename) throws JAXBException, IOException {
        DOMParser dom = new DOMParser(filename);
        dom.saveToXML(this);
    }

    public void addManufacturer(Manufacturer manufacturer) {
        if (!containsManufacturerId(manufacturers, manufacturer.getId())) {
            manufacturers.add(manufacturer);
        } else {
            throw new KeyAlreadyExistsException("manufacturer with such id: " + manufacturer.getId() + " already exists!");
        }
    }

    public void addCarBrandToManufacturer(Brand carBrand, String manufacturerId) {
        Manufacturer manufacturerFromDb = manufacturers.stream()
                .filter(o -> o.getId().equals(manufacturerId))
                .findFirst()
                .orElseThrow(()
                        -> new IllegalArgumentException("manufacturer with such id: " + manufacturerId + " doesn't exists!"));

        if (!containsBrandId(manufacturers, carBrand.getId())) {
            manufacturerFromDb.getBrands().add(carBrand);
        } else {
            throw new IllegalArgumentException("brand with id: " + carBrand.getId() + " already exists!");
        }
    }

    public void deleteManufacturer(String manufacturerId) {
        if (!manufacturers.removeIf(s -> s.getId().equals(manufacturerId))) {
            throw new IllegalArgumentException("manufacturer with such id: " + manufacturerId + " doesn't exists!");
        }
    }

    public void deleteBrandManufacturer(Brand carBrand, Manufacturer manufacturer) {
        manufacturer.getBrands().remove(carBrand);
    }

    public List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public void loadDataFromXML(String filename) {
        DOMParser dom = new DOMParser(filename);
        dom.parse();
        manufacturers = dom.getAutoShow().getManufacturers();
    }

    private boolean containsManufacturerId(final List<Manufacturer> list, final String id){
        return list.stream().anyMatch(o -> o.getId().equals(id));
    }

    private boolean containsBrandId(final List<Manufacturer> list, final String id){
        return list.stream().anyMatch(manufacturer -> manufacturer.getBrands().stream().anyMatch(o -> o.getId().equals(id)));
    }
}

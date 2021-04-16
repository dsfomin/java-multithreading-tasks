package Lab_2.Lab1;

import Lab_2.AutoShow.AutoShow;
import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;

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
public class AutoShowXML implements AutoShow {
    private final static String XML = "src/main/resources/Lab_2_1/Lab2_1.xml";

    @XmlElement(name = "manufacturer")
    private final List<Manufacturer> manufacturers;

    public AutoShowXML() {
        manufacturers = new ArrayList<>();
    }

    @Override
    public void addManufacturer(Manufacturer manufacturer) {
        if (!containsManufacturerId(manufacturers, manufacturer.getId())) {
            manufacturers.add(manufacturer);
        } else {
            throw new KeyAlreadyExistsException("manufacturer with such id: " + manufacturer.getId() + " already exists!");
        }
    }

    @Override
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

    @Override
    public void deleteManufacturer(String manufacturerId) {
        if (!manufacturers.removeIf(s -> s.getId().equals(manufacturerId))) {
            throw new IllegalArgumentException("manufacturer with such id: " + manufacturerId + " doesn't exists!");
        }
    }

    @Override
    public void deleteBrandManufacturer(Brand carBrand, Manufacturer manufacturer) {
        manufacturer.getBrands().remove(carBrand);
    }

    @Override
    public List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public void save() throws JAXBException, IOException {
        DOMParser dom = new DOMParser(XML);
        dom.saveToXML(this);
    }

    private boolean containsManufacturerId(final List<Manufacturer> list, final String id){
        return list.stream().anyMatch(o -> o.getId().equals(id));
    }

    private boolean containsBrandId(final List<Manufacturer> list, final String id){
        return list.stream().anyMatch(manufacturer -> manufacturer.getBrands().stream().anyMatch(o -> o.getId().equals(id)));
    }
}

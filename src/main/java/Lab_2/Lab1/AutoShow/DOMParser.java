package Lab_2.Lab1.AutoShow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DOMParser {

    private DocumentBuilderFactory documentBuilderFactory = null;
    private DocumentBuilder documentBuilder = null;
    private Document document = null;
    private String fileName;

    private AutoShow autoShow = null;

    public DOMParser(String fileName) {
        this.fileName = fileName;
    }

    public void parse() {
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(fileName);
            autoShow = new AutoShow();
            analysis();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void analysis() {

        Element root = document.getDocumentElement();
        if (root.getTagName().equals("autoshow")) {
            NodeList manufacturerList = root.getElementsByTagName("manufacturer");
            for (int i = 0; i < manufacturerList.getLength(); i++) {
                Node manufacturerNode = manufacturerList.item(i);
                if (manufacturerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element curManufacturer = (Element) manufacturerNode;

                    Manufacturer manufacturer = new Manufacturer();
                    manufacturer.setId(curManufacturer.getAttribute("id"));
                    manufacturer.setName(curManufacturer.getAttribute("name"));

                    NodeList brandList = curManufacturer.getElementsByTagName("brand");
                    for (int j = 0; j < brandList.getLength(); j++) {
                        Node brandNode = brandList.item(j);
                        if (brandNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element curBrand = (Element) brandList.item(j);

                            Brand brand = new Brand();
                            brand.setId(curBrand.getAttribute("id"));

                            NodeList featureList = curBrand.getChildNodes();

                            for (int k = 0; k < featureList.getLength(); k++) {
                                Node f = featureList.item(k);
                                if (f.getNodeType() == Node.ELEMENT_NODE) {
                                    Element feature = (Element) f;
                                    switch (feature.getTagName()) {
                                        case "name":
                                            brand.setName(feature.getTextContent());
                                            break;
                                        case "fuel_consumption":
                                            brand.setFuelConsumption(Double.valueOf(feature.getTextContent()));
                                            break;
                                        case "weight":
                                            brand.setWeight(Integer.parseInt(feature.getTextContent()));
                                            break;
                                        case "acceleration":
                                            brand.setAcceleration(Double.valueOf(feature.getTextContent()));
                                            break;
                                        case "horsepower":
                                            brand.setHorsepower(Integer.parseInt(feature.getTextContent()));
                                            break;
                                        default:
                                            throw new IllegalArgumentException("Unknown tag: " + feature.getTagName());
                                    }
                                }
                            }
                            manufacturer.getBrands().add(brand);
                        }
                    }
                    autoShow.getManufacturers().add(manufacturer);
                }
            }
        }
    }

    public void saveToXML(AutoShow autoShow) throws JAXBException, IOException {
        JAXBContext contextObj = JAXBContext.newInstance(AutoShow.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshallerObj.marshal(autoShow, new FileOutputStream("src/main/resources/Lab2_1.xml"));
    }

    public AutoShow getAutoShow() {
        return autoShow;
    }
}



package Lab_2.AutoShow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Brand implements Serializable {
    @XmlAttribute
    private String id;

    @XmlElement
    private String name;

    @XmlElement(name = "fuel_consumption")
    private Double fuelConsumption;

    @XmlElement
    private Integer weight;

    @XmlElement
    private Double acceleration;

    @XmlElement
    private Integer horsepower;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fuelConsumption=" + fuelConsumption +
                ", weight=" + weight +
                ", acceleration=" + acceleration +
                ", horsepower=" + horsepower +
                '}';
    }
}

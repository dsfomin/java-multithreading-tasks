package Lab_2.Lab3.Server;


import Lab_2.AutoShow.AutoShow;
import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoShowDao implements AutoShow {

    private Connection con;
    private Statement stmt;

    public AutoShowDao() {
        String url = "jdbc:mysql://localhost:3306/autoshow";
        String userName = "root";
        String password = "asdfg567";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, userName, password);
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() throws SQLException {
        con.close();
    }

    @Override
    public void addManufacturer(Manufacturer manufacturer) {
        String sql = String.format("INSERT INTO manufacturer (id, name) VALUES (%s, '%s')", manufacturer.getId(), manufacturer.getName());

        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("manufacturer with such id: " + manufacturer.getId() + " already exists!");
        }
    }

    @Override
    public void addCarBrandToManufacturer(Brand carBrand, String manufacturerId) {
        String sql = String.format("INSERT INTO brand (id, name, fuel_consumption, weight, acceleration, horsepower, manufacturer_id) VALUES (%s, '%s', %s, %s, %s, %s, %s)",
                carBrand.getId(),
                carBrand.getName(),
                carBrand.getFuelConsumption(),
                carBrand.getWeight(),
                carBrand.getAcceleration(),
                carBrand.getHorsepower(),
                manufacturerId);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(
                    "manufacturer with id: " + manufacturerId + " doesn't exists\n" +
                            "or brand with id: " + carBrand.getId() + " already exists!");
        }
    }

    @Override
    public void deleteManufacturer(String manufacturerId) throws SQLException {
        String sql = "DELETE FROM manufacturer WHERE id = " + manufacturerId;

        int c = stmt.executeUpdate(sql);
        if (!(c > 0)) {
            throw new IllegalArgumentException("manufacturer with such id: " + manufacturerId + " doesn't exists!");
        }
    }

    @Override
    public void deleteBrandManufacturer(Brand carBrand, Manufacturer manufacturer) {
    }

    @Override
    public List<Manufacturer> getManufacturers() throws SQLException {
        List<Manufacturer> result = new ArrayList<>();
        String sql1 = "SELECT * FROM manufacturer";
        String sql2 = "SELECT * FROM brand";
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);
            rs = stmt.executeQuery(sql1);

            System.out.println("СПИСОК Производителей:");

            while (rs.next()) {
                Manufacturer manufacturer = new Manufacturer();

                int id = rs.getInt("id");
                String name = rs.getString("name");

                manufacturer.setId(String.valueOf(id));
                manufacturer.setName(name);
                result.add(manufacturer);

                System.out.println(" >> " + id + " - " + name);
            }

            rs = stmt.executeQuery(sql2);

            System.out.println("СПИСОК Марок:");

            while (rs.next()) {
                Brand brand = new Brand();

                int id = rs.getInt("id");
                String name = rs.getString("name");
                Double fuelConsumption = rs.getDouble("fuel_consumption");
                int horsepower = rs.getInt("horsepower");
                Double acceleration = rs.getDouble("acceleration");
                int weight = rs.getInt("weight");
                int manufacturerId = rs.getInt("manufacturer_id");

                brand.setId(String.valueOf(id));
                brand.setName(name);
                brand.setAcceleration(acceleration);
                brand.setFuelConsumption(fuelConsumption);
                brand.setHorsepower(horsepower);
                brand.setWeight(weight);

                Manufacturer manufacturer = result.stream()
                        .filter(m -> m.getId().equals(String.valueOf(manufacturerId)))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "brand with id: " + brand.getId() + "has manufacturer with id: " + manufacturerId + " that doesn't exists!"));

                manufacturer.getBrands().add(brand);

                System.out.println(" >> " + id + " - " + name);
            }
            con.commit();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            con.rollback();
        } finally {
            if (rs != null) {
                rs.close();
            }
            con.setAutoCommit(true);
        }
        return result;
    }

    @Override
    public void save() {
    }
}

package Lab_2.Lab4.Server;


import Lab_2.AutoShow.Brand;
import Lab_2.AutoShow.Manufacturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoShowDao {

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

    public int addManufacturer(Manufacturer manufacturer) {
        int response = 1;
        String sql = String.format("INSERT INTO manufacturer (id, name) VALUES (%s, '%s')", manufacturer.getId(), manufacturer.getName());

        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            response = 0;
        }
        return response;
    }

    public int addCarBrandToManufacturer(Brand carBrand, String manufacturerId) {
        int response = 1;
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
            response = 0;
        }
        return response;
    }

    public int deleteManufacturer(String manufacturerId) throws SQLException {
        String sql = "DELETE FROM manufacturer WHERE id = " + manufacturerId;

        return stmt.executeUpdate(sql);
    }

    public List<Manufacturer> getManufacturers() throws SQLException {
        List<Manufacturer> result = new ArrayList<>();
        String sql1 = "SELECT * FROM manufacturer";
        String sql2 = "SELECT * FROM brand";
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);
            rs = stmt.executeQuery(sql1);

            while (rs.next()) {
                Manufacturer manufacturer = new Manufacturer();

                int id = rs.getInt("id");
                String name = rs.getString("name");

                manufacturer.setId(String.valueOf(id));
                manufacturer.setName(name);
                result.add(manufacturer);
            }

            rs = stmt.executeQuery(sql2);

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
}

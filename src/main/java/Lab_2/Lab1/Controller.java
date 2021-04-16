package Lab_2.Lab1;

import Lab_2.Lab1.AutoShow.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Controller {
    private final static String XML = "src/main/resources/Lab2_1.xml";
    private final static String XSD = "src/main/resources/Lab2_1.xsd";
    private final static MyValidator validate = new MyValidator(XML, XSD);
    private final static String LINE_SEPARATOR = System.lineSeparator();

    private AutoShow autoShow;
    private final DOMParser dom = new DOMParser(XML);

    public Controller() {
        if (validate.isValid()) {
            System.out.println("XML is valid with this XSD");
        } else {
            throw new IllegalStateException("XML is invalid with XSD");
        }

        dom.parse();
        autoShow = dom.getAutoShow();
    }

    @FXML
    public Button addManufacturerButton;
    @FXML
    public Button saveDataButton;
    @FXML
    public Button addCarBrandButton;
    @FXML
    public Button deleteManufacturerButton;
    @FXML
    public Button getManufacturersButton;
    @FXML
    public Label saveResult;

    public void addManufacturer(ActionEvent actionEvent) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        //Defining the Name text field
        final TextField name = new TextField();
        name.setPromptText("Enter manufacturer name.");
        name.setPrefColumnCount(10);
        name.getText();
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);
        //Defining the Last Name text field
        final TextField id = new TextField();
        id.setPromptText("Enter manufacturer id.");
        GridPane.setConstraints(id, 0, 1);
        grid.getChildren().add(id);
        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);
        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 3);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        submit.setOnAction(e -> {
            if ((name.getText() != null && !name.getText().isEmpty()) && (id.getText() != null && !id.getText().isEmpty())) {

                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setId(id.getText());
                manufacturer.setName(name.getText());

                try {
                    autoShow.addManufacturer(manufacturer);
                    label.setText("Manufacturer was successfully added!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            } else {
                label.setText("Fill all fields!");
            }
        });

        clear.setOnAction(e -> {
            name.clear();
            id.clear();
            label.setText(null);
        });

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(grid));
        stage.show();
    }

    public void save(ActionEvent actionEvent) {
        try {
            dom.saveToXML(autoShow);
            saveResult.setTextFill(Paint.valueOf("green"));
            saveResult.setText("Saved successfully!");
        } catch (Exception ex) {
            saveResult.setText(ex.getMessage());
        }
    }

    public void addCarBrand(ActionEvent actionEvent) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        //Defining the manufacturer id text field
        final TextField manufacturerId = new TextField();
        manufacturerId.setPromptText("Enter manufacturer id.");
        GridPane.setConstraints(manufacturerId, 0, 0);
        grid.getChildren().add(manufacturerId);
        //Defining the id text field
        final TextField id = new TextField();
        id.setPromptText("Enter brand id.");
        GridPane.setConstraints(id, 0, 1);
        grid.getChildren().add(id);
        //Defining the name text field
        final TextField name = new TextField();
        name.setPromptText("Enter brand name.");
        name.setPrefColumnCount(10);
        GridPane.setConstraints(name, 0, 2);
        grid.getChildren().add(name);
        //Defining the fuel consumption text field
        final TextField fuelConsumption = new TextField();
        fuelConsumption.setPromptText("Enter brand fuel consumption.");
        fuelConsumption.setPrefColumnCount(10);
        GridPane.setConstraints(fuelConsumption, 0, 3);
        grid.getChildren().add(fuelConsumption);
        //Defining the weight text field
        final TextField weight = new TextField();
        weight.setPromptText("Enter brand weight.");
        GridPane.setConstraints(weight, 0, 4);
        grid.getChildren().add(weight);
        //Defining the acceleration text field
        final TextField acceleration = new TextField();
        acceleration.setPromptText("Enter brand acceleration.");
        acceleration.setPrefColumnCount(10);
        GridPane.setConstraints(acceleration, 0, 5);
        grid.getChildren().add(acceleration);
        //Defining the horsepower field
        final TextField horsepower = new TextField();
        horsepower.setPromptText("Enter brand horsepower.");
        GridPane.setConstraints(horsepower, 0, 6);
        grid.getChildren().add(horsepower);
        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);
        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 7);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        submit.setOnAction(e -> {
            if ((name.getText() != null && !name.getText().isEmpty()) &&
                    (manufacturerId.getText() != null && !manufacturerId.getText().isEmpty()) &&
                    (id.getText() != null && !id.getText().isEmpty()) &&
                    (acceleration.getText() != null && !acceleration.getText().isEmpty()) &&
                    (horsepower.getText() != null && !horsepower.getText().isEmpty()) &&
                    (weight.getText() != null && !weight.getText().isEmpty()) &&
                    (fuelConsumption.getText() != null && !fuelConsumption.getText().isEmpty())) {

                try {
                    Brand brand = new Brand();
                    brand.setId(id.getText());
                    brand.setName(name.getText());
                    brand.setHorsepower(Integer.valueOf(horsepower.getText()));
                    brand.setWeight(Integer.valueOf(weight.getText()));
                    brand.setAcceleration(Double.valueOf(acceleration.getText()));
                    brand.setFuelConsumption(Double.valueOf(fuelConsumption.getText()));

                    autoShow.addCarBrandToManufacturer(brand, manufacturerId.getText());
                    label.setText("Brand was successfully added!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            } else {
                label.setText("Fill all fields!");
            }
        });

        clear.setOnAction(e -> {
            manufacturerId.clear();
            id.clear();
            name.clear();
            weight.clear();
            fuelConsumption.clear();
            horsepower.clear();
            acceleration.clear();
            label.setText(null);
        });

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(grid));
        stage.show();

    }

    public void deleteManufacturer(ActionEvent actionEvent) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        //Defining the id text field
        final TextField id = new TextField();
        id.setPromptText("Enter manufacturer id.");
        GridPane.setConstraints(id, 0, 0);
        grid.getChildren().add(id);
        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 2, 0);
        grid.getChildren().add(clear);
        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 1);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        submit.setOnAction(e -> {
            if (id.getText() != null && !id.getText().isEmpty()) {
                try {
                    autoShow.deleteManufacturer(id.getText());
                    label.setText("Manufacturer was successfully deleted!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            } else {
                label.setText("Fill all fields!");
            }
        });

        clear.setOnAction(e -> {
            id.clear();
            label.setText(null);
        });

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(grid));
        stage.show();
    }

    public void getManufacturers(ActionEvent actionEvent) {
        Stage stage = new Stage();

        ScrollPane root = new ScrollPane();
        Scene scene = new Scene(root);
        Text text = new Text(printAllManufacturers(autoShow));
        text.wrappingWidthProperty().bind(scene.widthProperty());
        root.setFitToWidth(true);
        root.setFitToHeight(true);
        root.setContent(text);

        stage.setTitle("List of manufacturers");
        stage.setScene(scene);
        stage.show();

        System.out.println(autoShow.getManufacturers().toString());
    }

    private String printAllManufacturers(AutoShow autoShow) {
        StringBuilder sb = new StringBuilder();
        for (Manufacturer m : autoShow.getManufacturers()) {
            sb.append('\t').append("Id: ").append(m.getId()).append(LINE_SEPARATOR);
            sb.append('\t').append("Name: ").append(m.getName()).append(LINE_SEPARATOR);
            for (Brand b : m.getBrands()) {
                sb.append('\t').append('\t').append("Id: ").append(b.getId()).append(LINE_SEPARATOR);
                sb.append('\t').append('\t').append("Name: ").append(b.getName()).append(LINE_SEPARATOR);
                sb.append('\t').append('\t').append("Acceleration: ").append(b.getAcceleration()).append(LINE_SEPARATOR);
                sb.append('\t').append('\t').append("Fuel Consumption: ").append(b.getFuelConsumption()).append(LINE_SEPARATOR);
                sb.append('\t').append('\t').append("Horsepower: ").append(b.getHorsepower()).append(LINE_SEPARATOR);
                sb.append('\t').append('\t').append("Weight: ").append(b.getWeight()).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }
}

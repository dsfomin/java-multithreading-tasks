package Lab_1.Lab1.a;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private Slider slider;
    @FXML
    private Spinner<Integer> priorityThread1, priorityThread2;
    @FXML
    private Label sliderValue;
    @FXML
    private Button button;

    private Thread thread1, thread2;

    @FXML
    private void initialize() {
        priorityThread1.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5));
        priorityThread2.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5));

        slider.setValue(50.0d);
        slider.setBlockIncrement(1.0d);
        slider.setMax(100.0d);
        slider.setMin(0.0d);

    }

    @FXML
    public void onClick() {
        startThreads();
    }

    private Thread initializeThread(double targetPosition) {
        return new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (slider) {
                    Platform.runLater(() -> {
                        if (slider.getValue() < targetPosition)
                            slider.setValue(slider.getValue() + 1);
                        else
                            slider.setValue(slider.getValue() - 1);
                        sliderValue.setText(Double.toString(slider.getValue()));
                    });
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    private void initializeThreads() {
        thread1 = initializeThread(10.0);
        thread2 = initializeThread(90.0);

        thread1.setDaemon(true);
        thread2.setDaemon(true);

        thread1.setPriority(priorityThread1.getValue());
        thread2.setPriority(priorityThread2.getValue());

        priorityThread1.valueProperty().addListener(
                (observableValue, integer, newValue) -> thread1.setPriority(newValue));

        priorityThread2.valueProperty().addListener(
                (observableValue, integer, newValue) -> thread1.setPriority(newValue));
    }

    private void startThreads() {
        if (thread1 == null || thread2 == null || !thread1.isAlive() || !thread2.isAlive()) {
            initializeThreads();
            button.setText("Stop");
            slider.setDisable(true);
            thread1.start();
            thread2.start();
        } else {
            button.setText("Start");
            slider.setDisable(false);
            try {
                thread1.interrupt();
                thread2.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

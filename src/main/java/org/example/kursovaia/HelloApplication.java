package org.example.kursovaia;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import org.example.kursovaia.data.DbWorker;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));//открывается самым первым
        stage.setTitle("Федерация дзюдо России");
        stage.setScene(new Scene(root,700,400));
        stage.show();
        DbWorker.initDB();
    }

    public static void main(String[] args) {
        launch();
    }
}
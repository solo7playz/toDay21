package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("one.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),800,400);
        stage.setScene(scene);
        stage.setTitle("javafx-maven");
        stage.show();
    }

    public static void main (String[] args){ launch();}
}

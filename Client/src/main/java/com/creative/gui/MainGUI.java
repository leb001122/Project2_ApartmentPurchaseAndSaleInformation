package com.creative.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Socket socket = new Socket("192.168.0.134", 7777);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 843, 505);

        MainController mainController = fxmlLoader.<MainController>getController();
        mainController.setStream(in, out);

        stage.setTitle("프로그램 이름");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
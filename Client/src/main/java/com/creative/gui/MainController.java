package com.creative.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainController {
    @FXML
    private Button btnEnrollment;

    @FXML
    private Button btnLogin;
    private InputStream in;
    private OutputStream out;


    @FXML
    void enroll_Clicked(MouseEvent event) throws IOException {

        FXMLLoader enrollLoader = new FXMLLoader(getClass().getResource("enrollmentView.fxml"));
        Parent enrollRoot = enrollLoader.load();

        EnrollmentController enrollmentController = enrollLoader.<EnrollmentController>getController();
        enrollmentController.setStream(in, out);

        Stage primaryStage = (Stage) btnEnrollment.getScene().getWindow();
        Scene scene = new Scene(enrollRoot);
        primaryStage.setScene(scene);
    }

    @FXML
    void login_Clicked(MouseEvent event) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginView.fxml"));
        Parent loginRoot = loginLoader.load();

        LogInController logInController = loginLoader.<LogInController>getController();
        logInController.setStream(in, out);


        Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(loginRoot);
        primaryStage.setScene(scene);
    }

    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
}

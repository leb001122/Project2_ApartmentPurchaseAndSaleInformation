package com.creative.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ApartPreViewController {
    @FXML
    private Button btnBack;

    @FXML
    private Button btnPreView;

    private InputStream in;
    private OutputStream out;


    @FXML
    void back_Clicked(ActionEvent event) throws IOException {
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("userView.fxml"));
        Parent backRoot = backLoader.load();


        MainController controller = backLoader.<MainController>getController();
        controller.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }

    @FXML
    void preView_Clicked(ActionEvent event) {

    }

    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

}

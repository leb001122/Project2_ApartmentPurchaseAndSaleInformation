package com.creative.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UserController {

    private InputStream in;
    private OutputStream out;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFavorite;

    @FXML
    private Button btnGraph;


    @FXML
    private Button btnRealTrans;


    @FXML
    void favorite_Clicked(ActionEvent event) throws IOException {
        FXMLLoader favorLoader = new FXMLLoader(getClass().getResource("favoriteView.fxml"));
        Parent favorRoot = favorLoader.load();

        FavoriteController favoriteController = favorLoader.<FavoriteController>getController();
        favoriteController.setStream(in, out);

        Stage primaryStage = (Stage) btnFavorite.getScene().getWindow();
        Scene scene = new Scene(favorRoot);
        primaryStage.setScene(scene);

    }

    @FXML
    void graph_Clicked(ActionEvent event) throws IOException {
        System.out.println(out);
        System.out.println(in);

        FXMLLoader graphLoader = new FXMLLoader(getClass().getResource("graphChoiceOptionView.fxml"));
        Parent graphRoot = graphLoader.load();

        GraphChoiceOptionController graphChoiceOptionController = graphLoader.<GraphChoiceOptionController>getController();
        graphChoiceOptionController.setStream(in, out);

        Stage primaryStage = (Stage) btnGraph.getScene().getWindow();
        Scene scene = new Scene(graphRoot);
        primaryStage.setScene(scene);
    }


    // 아파트 거래정보 조회
    @FXML
    void realTrans_Clicked(ActionEvent event) throws IOException {
        System.out.println(in);
        System.out.println(out);

        FXMLLoader realTransLoader = new FXMLLoader(getClass().getResource("RealTrans_Choice_SelectView.fxml"));
        Parent realTransRoot = realTransLoader.load();

        RealTrans_Choice_Select_Controller controller = realTransLoader.<RealTrans_Choice_Select_Controller>getController();
        controller.setStream(in, out);

        Stage primaryStage = (Stage) btnRealTrans.getScene().getWindow();
        Scene scene = new Scene(realTransRoot);
        primaryStage.setScene(scene);
    }

    @FXML
    void back_Clicked(ActionEvent event) throws IOException {
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent backRoot = backLoader.load();


        MainController controller = backLoader.<MainController>getController();
        controller.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }

    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
}

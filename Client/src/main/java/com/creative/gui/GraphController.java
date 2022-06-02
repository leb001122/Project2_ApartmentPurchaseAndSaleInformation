package com.creative.gui;

import dto.ApartPriceIndicesDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    private InputStream in;
    private OutputStream out;

    @FXML
    private Button btnBack;
    @FXML
    private LineChart<String, Float> apart_graph;
    private String region;

//    ArrayList<ApartPriceIndicesDTO> arrayList ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void setGraph(ArrayList<ApartPriceIndicesDTO> arrayList) {

        XYChart.Series<String, Float> data = new XYChart.Series<String, Float>();
        apart_graph.getData().clear();

        for (ApartPriceIndicesDTO dto : arrayList) {
            if (dto.getIndex() != 0) {
                data.getData().add(new XYChart.Data<String, Float>(dto.getDate(), dto.getIndex()));
            }
        }

        data.setName(region);
        apart_graph.getData().add(data);
    }

    @FXML
    void back_Clicked(ActionEvent event) throws IOException {
        System.out.println(out);
        System.out.println(in);

        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("graphChoiceOptionView.fxml"));
        Parent backRoot = backLoader.load();

        GraphChoiceOptionController controller = backLoader.<GraphChoiceOptionController>getController();
        controller.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }


    public void setRegionName(String region) {
        this.region = region;
    }

//    public void setList(ArrayList<ApartPriceIndicesDTO> arrayList) {
//        this.arrayList = arrayList;
//    }

    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
}

package com.creative.gui;

import dto.ApartPriceIndicesDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import network.Packet;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GraphChoiceOptionController implements Initializable {

    @FXML
    private ComboBox<String> areaChoice;
    @FXML
    private ComboBox<String> preYearChoice;
    @FXML
    private Button btnShow;

    private InputStream in;
    private OutputStream out;
    @FXML
    private Button btnBack;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] strArrList1 = {"전국", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북",
                "충남", "전북", "전남", "경북", "경남", "제주"};
        ObservableList<String> fxComboNameList1 = FXCollections.observableArrayList(strArrList1);
        areaChoice.setItems(fxComboNameList1);

        String[] strArrList2 = {"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013",
                "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021"};
        ObservableList<String> fxComboNameList2 = FXCollections.observableArrayList(strArrList2);
        preYearChoice.setItems(fxComboNameList2);

        // 첫번쨰를 디폴트 선택으로 설정
        areaChoice.getSelectionModel().selectFirst();
        preYearChoice.getSelectionModel().selectFirst();
    }

    @FXML
    void show_Clicked(ActionEvent event) throws IOException {

        ApartPriceIndicesDTO reqDto = ApartPriceIndicesDTO.builder()
                .region(areaChoice.getValue())
                .date(preYearChoice.getValue())
                .build();

        ArrayList<ApartPriceIndicesDTO> arrayList = reqPriceIndices(reqDto);

        if (arrayList == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("해당하는 결과가 없습니다.");
            alert.showAndWait();
        }
        else {
            FXMLLoader graphLoader = new FXMLLoader(getClass().getResource("graphView.fxml"));
            Parent graphRoot = graphLoader.load();

            GraphController graphController = graphLoader.<GraphController>getController();
            graphController.setRegionName(reqDto.getRegion());
            System.out.println(reqDto.getRegion());
            graphController.setStream(in, out);
            graphController.setGraph(arrayList);

            Stage primaryStage = (Stage) btnShow.getScene().getWindow();
            Scene scene = new Scene(graphRoot);
            primaryStage.setScene(scene);
        }
    }


    public ArrayList<ApartPriceIndicesDTO> reqPriceIndices(ApartPriceIndicesDTO apartPriceIndicesDTO) throws IOException {
        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.INDICES_GRAPH);
        sendPt.writeObject(out, apartPriceIndicesDTO);

        Packet recvPt = new Packet();
        recvPt.read(in);

        if (recvPt.getCode() == Protocol.SUCCESS) {
            ArrayList<ApartPriceIndicesDTO> dtos = new ArrayList<>();;
            ArrayList<Object> recvPtArrayList = recvPt.getArrayList();

            if (recvPtArrayList == null) {
                System.out.println("결과 없음");
            } else {
                for (Object dto : recvPtArrayList) {
                    dtos.add((ApartPriceIndicesDTO) dto);
                }
                return dtos;
            }
        }
        return null;
    }

    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }


    public void back_Clicked(ActionEvent event) throws IOException {
        System.out.println(out);
        System.out.println(in);

        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("userView.fxml"));
        Parent backRoot = backLoader.load();

        UserController userController = backLoader.<UserController>getController();
        userController.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }
}


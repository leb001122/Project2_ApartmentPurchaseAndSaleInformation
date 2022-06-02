package com.creative.gui;

import dto.ApartTransactionDTO;
import dto.FavoritesDTO;
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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import network.Packet;
import network.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FavoriteController implements Initializable {
    @FXML
    private Button btnBack;

    @FXML
    private ListView<String> favoriteList;

    private InputStream in;
    private OutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    // 즐겨찾기 조회
    public ArrayList<ApartTransactionDTO> retrieveFavorites() throws IOException {
        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.FAVORITES_READ);
        sendPt.write(out);

        Packet recvPt = new Packet();
        recvPt.read(in);

        ArrayList<ApartTransactionDTO> favoritesDTOS = new ArrayList<>();;
        ArrayList<Object> recvPtArrayList = recvPt.getArrayList();

        for (Object dto : recvPtArrayList) {
            System.out.println(dto);
        }

        if (recvPtArrayList != null) {
            for (Object dto : recvPtArrayList) {
                favoritesDTOS.add((ApartTransactionDTO) dto);
            }
            return favoritesDTOS;
        }
        return null;
    }


    // 즐겨찾기 삭제
    public boolean deleteFavorites(FavoritesDTO favoritesDTO) throws IOException {
        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.FAVORITES_DELETE);
        sendPt.writeObject(out, favoritesDTO);

        Packet recvPt = new Packet();
        recvPt.read(in);

        if (recvPt.getCode() == Protocol.SUCCESS) {
            return true;
        } else if (recvPt.getCode() == Protocol.FAIL){
            return false;
        }
        return false;
    }

    @FXML
    void back_Clicked(ActionEvent event) throws IOException {
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("userView.fxml"));
        Parent backRoot = backLoader.load();

        UserController userController = backLoader.<UserController>getController();
        userController.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }

    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        showFavorites();
    }

    void showFavorites() {
        try {

            ArrayList<ApartTransactionDTO> favoritesDTOS = retrieveFavorites();
            if (favoritesDTOS == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("등록된 즐겨찾기 정보가 존재하지 않습니다.");
                alert.showAndWait();
                return;
            }

            List<String > strList = new ArrayList<>();
            String str = null;
            for(int i = 0; i<favoritesDTOS.size(); i++ ){
                str = favoritesDTOS.get(i).getId()+" / ";
                str += favoritesDTOS.get(i).getSiGunGu()+" / ";
                str += favoritesDTOS.get(i).getRoadName() +" / ";
                str += favoritesDTOS.get(i).getDanjiName() +"/";
                str += favoritesDTOS.get(i).getArea() +"/";
                str += favoritesDTOS.get(i).getContract_y_m() +" / ";
                str += favoritesDTOS.get(i).getTradeAmount() +" / ";
                str += favoritesDTOS.get(i).getFloor() +" / ";
                str += favoritesDTOS.get(i).getBuildYear();
                strList.add(str);
                str = null;
            }

            ObservableList<String> list = FXCollections.observableArrayList(strList);
            favoriteList.setItems(list);
            favoriteList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void cancel_Clicked(ActionEvent event)
    {
        // TODO 즐겨찾기 삭제할 목록 선택해서 아이디 반환

        String obj = (String)favoriteList.getSelectionModel().getSelectedItem();
        String [] objArr = obj.split("/");
        int id = Integer.parseInt(StringUtils.removeEnd(objArr[0], " "));

        FavoritesDTO favoritesDTO = FavoritesDTO.builder()
                .apartTransactionId(id) // 선택된 아이디 집어넣기
                .build();
        try {
            if (deleteFavorites(favoritesDTO)) {
                System.out.println("즐겨찾기 삭제 성공");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("즐겨찾기 취소 실패");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        showFavorites();
    }

}

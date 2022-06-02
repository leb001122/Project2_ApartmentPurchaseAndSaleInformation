package com.creative.gui;

import dto.ApartTransactionDTO;
import dto.FavoritesDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

public class RealTransController implements Initializable {
    private InputStream in;
    private OutputStream out;

    @FXML
    private Button btnPreView;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnFavorite;

    @FXML
    private ListView<String> realTransList;

    private ArrayList<ApartTransactionDTO> apartTransactionDTOS;

    public void initialize(URL location, ResourceBundle resources) {



    }

    // 즐겨찾기
    public boolean registerFavorites(FavoritesDTO favoritesDTO) throws IOException {

        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.FAVORITES_CREATE);
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
    void favorite_Clicked(ActionEvent event) {
        String obj = realTransList.getSelectionModel().getSelectedItem();
        String [] objArr = obj.split("/");
        int id = Integer.parseInt(StringUtils.removeEnd(objArr[0], " "));

        FavoritesDTO dto = FavoritesDTO.builder()
                .apartTransactionId(id)
                .build();
        try {
            if (registerFavorites(dto)) {
                System.out.println("즐겨찾기 성공");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("즐겨찾기 추가 실패");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void preView_Clicked(ActionEvent event) throws IOException {
        Pane root = new Pane();
        Line line = new Line();

        line.setStartX(-1000);
        line.setEndX(5000);
        line.setLayoutX(100);
        line.setLayoutY(84);

        // 내용 추가(Label)                V
        Label greeting = new Label(); // 내용 define
        greeting.setText("아파트 면적 프리뷰");
        greeting.setAlignment(Pos.CENTER);
        greeting.setLayoutX(1500);
        //greeting.setLayoutY(60);
        greeting.setTextFill(Color.BLACK);    // 칼라변경 make sure you import color class
        greeting.setFont(Font.font(null, FontWeight.BOLD, 48));

        String str = realTransList.getSelectionModel().getSelectedItem();
        String strArr[] = str.split("/");
        double pwang = Double.parseDouble(strArr[4]);


        //글자스타일, 글자진하게, 글자크기 변경.

        // 내용추가.(이거써야 글자 띄어짐.)


        Rectangle box1 = new Rectangle(8,17);
        Rectangle box2 = new Rectangle(pwang*18,pwang*18);

        //사각형 색상 변경
        box1.setFill(Color.DARKGOLDENROD);
        box2.setFill(Color.WHITE);
        //사각형 테두리 생성
        box1.setStrokeWidth(1);
        box1.setStroke(Color.BLACK);

        box2.setStrokeWidth(1);
        box2.setStroke(Color.BLACK);


        box2.setX(280);
        box2.setY(150);
        //사각형 좌표 설정
        box1.setX(box2.getX()+box2.getWidth());
        box1.setY(box2.getY()+box2.getHeight()-box1.getHeight());

        //BorderPane 객체에 사각형 객체 추가
        root.getChildren().addAll(greeting,box1, box2, line);



        Scene scene = new Scene(root,400,400);

        Stage stage = new Stage();


        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void back_Clicked(ActionEvent event) throws IOException {
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("userView.fxml"));
        Parent backRoot = backLoader.load();

        UserController controller = backLoader.<UserController>getController();
        controller.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }

    public void setList(ArrayList<ApartTransactionDTO> dtos) {


        apartTransactionDTOS = dtos;

        List<String > strList = new ArrayList<>();
        String str = null;
        for(int i = 0; i<apartTransactionDTOS.size(); i++ ){
            str = apartTransactionDTOS.get(i).getId()+" / ";
            str += apartTransactionDTOS.get(i).getSiGunGu()+" / ";
            str += apartTransactionDTOS.get(i).getRoadName() +" / ";
            str += apartTransactionDTOS.get(i).getDanjiName() +"/";
            str += apartTransactionDTOS.get(i).getArea() +"/";
            str += apartTransactionDTOS.get(i).getContract_y_m() +" / ";
            str += apartTransactionDTOS.get(i).getTradeAmount() +" / ";
            str += apartTransactionDTOS.get(i).getFloor() +" / ";
            str += apartTransactionDTOS.get(i).getBuildYear();
            strList.add(str);
            str = null;
        }

        //  리스트 뷰 초기화
        ObservableList<String> list = FXCollections.observableArrayList(strList);
        realTransList.setItems(list);
        realTransList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


//        realTransList.setOnMouseClicked((MouseEvent) -> {
//            String obj = (String)realTransList.getSelectionModel().getSelectedItem();
//            String [] objArr = obj.split("/");
//            String id = "";
//            favoritesId = Integer.parseInt(StringUtils.removeEnd(objArr[0], " "));
//        });
    }


    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
}

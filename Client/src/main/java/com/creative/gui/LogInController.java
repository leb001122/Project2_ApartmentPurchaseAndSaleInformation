package com.creative.gui;

import dto.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Packet;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LogInController {

    // TODO 로그인 창에 아이다찾기, 패스워드 찾기, 회원가입 다 삭제함
    @FXML
    private Button btnBack;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtPassword;

    private InputStream in;
    private OutputStream out;


    @FXML
    void login(ActionEvent event) throws IOException {

        UserDTO user = UserDTO.builder()
                .id(txtID.getText())
                .password(txtPassword.getText())
                .build();

        if (login(user)) { // 로그인 성공 시

            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("userView.fxml"));
            Parent loginRoot = loginLoader.load();

            UserController userController = loginLoader.<UserController>getController();
            userController.setStream(in, out);

            Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            primaryStage.setScene(scene);

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("로그인 실패");
            alert.showAndWait();

        }
    }

    // 로그인
    public boolean login(UserDTO userDTO) throws IOException {
        System.out.println(in);
        System.out.println(out);

        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.LOGIN);
        sendPt.writeObject(out, userDTO);

        Packet recvPt = new Packet();
        recvPt.read(in);

        if (recvPt.getCode() == Protocol.SUCCESS) {
            System.out.println("로그인 성공");
            return true;
        } else if (recvPt.getCode() == Protocol.FAIL){
            System.out.println("로그인 실패");
            return false;
        }
        return false;
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

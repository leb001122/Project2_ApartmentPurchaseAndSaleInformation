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

public class EnrollmentController {
    @FXML
    private Button btnBack;
    @FXML
    private Button btnEnroll;

    @FXML
    private TextField userID;

    @FXML
    private TextField userMail;

    @FXML
    private TextField userName;

    @FXML
    private TextField userPassword;

    private InputStream in;
    private OutputStream out;


    @FXML
    void enroll_Clicked(ActionEvent event) throws IOException {
        btnEnroll.setOnAction((ActionEvent) -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userID.getText());
            userDTO.setPassword(userPassword.getText());
            userDTO.setName(userName.getText());
            userDTO.setEmail(userMail.getText());

            System.out.println(userDTO.toString());

            try {
                if(signUp(userDTO)) { // 회원가입 성공 시

                    System.out.println("회원가입 성공");

                    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginView.fxml"));
                    Parent loginRoot = loginLoader.load();

                    LogInController logInController = loginLoader.<LogInController>getController();
                    logInController.setStream(in, out);

                    Stage primaryStage = (Stage) btnEnroll.getScene().getWindow();
                    Scene scene = new Scene(loginRoot);
                    primaryStage.setScene(scene);

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("회원가입 실패");
                    alert.showAndWait();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    // 회원가입
    public boolean signUp(UserDTO userDTO) throws IOException {

        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.SIGN_UP);
        sendPt.writeObject(out, userDTO);

        Packet recvPt = new Packet();
        recvPt.read(in);

        if (recvPt.getCode() == Protocol.SUCCESS) {
            return true;
        } else if (recvPt.getCode() == Protocol.FAIL){
            System.out.println("회원가입실패");
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

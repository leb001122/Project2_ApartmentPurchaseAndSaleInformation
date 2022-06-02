package com.creative.gui;

import dto.ApartTransactionDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import network.Packet;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RealTrans_Choice_Select_Controller implements Initializable {
    @FXML
    private Button btnBack;

    private InputStream in;
    private OutputStream out;

    @FXML
    TreeView<String> treeView;

    @FXML
    private Button SelectApart;

    @FXML
    private ComboBox<String> buildyear; // 건축년도
    @FXML
    private ComboBox<String> buildYearUD; // 건축년도 이상, 이하 선택
    @FXML
    private ComboBox<String> floor;     // 층
    @FXML
    private ComboBox<String> floorUD;   // 층 이상, 이하
    @FXML
    private ComboBox<String> price;   // 가격
    @FXML
    private ComboBox<String> priceUD ;  // 가격 이상, 이하


    @FXML
    void Select_Clicked(ActionEvent event) throws IOException {
        ApartTransactionDTO reqDTO = null;

        if (treeView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("지역 선택 안됨");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("지역을 필수로 선택해 주세요");
            alert.showAndWait();
        } else {
            showAparts(reqDTO);
        }

    }

    private void showAparts(ApartTransactionDTO reqDTO) throws IOException {

        String rootRegion = treeView.getSelectionModel().getSelectedItem().getParent().getValue();

        String region = rootRegion;
        if (treeView.getSelectionModel().getSelectedItem() != null) {
            String childRegion = treeView.getSelectionModel().getSelectedItem().getValue();
            region += " " + childRegion;
        }

        reqDTO  = new ApartTransactionDTO();
        reqDTO.setSiGunGu(region);

        if (buildyear.getValue() != null && buildYearUD.getValue() != null) {
            reqDTO.setBuildYear(Integer.parseInt(buildyear.getValue()));
            reqDTO.setBuildYearOption(buildYearUD.getValue());

        }
        if (floor.getValue() != null && floorUD.getValue() != null) {
            reqDTO.setFloor(Integer.parseInt(floor.getValue()));
            reqDTO.setFloorOption(floorUD.getValue());
        }
        if (price.getValue() != null && priceUD.getValue() != null) {
            reqDTO.setTradeAmount(Integer.parseInt(price.getValue()));
            reqDTO.setPriceOption(priceUD.getValue());
        }
        System.out.println(reqDTO);

        ArrayList<ApartTransactionDTO> apartTransactionDTOS = recommendAparts(reqDTO);

        if (apartTransactionDTOS == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("해당 결과가 존재하지 않습니다");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("realTransSelectView.fxml"));
            Parent root = loader.load();

            RealTransController realTransController = loader.<RealTransController>getController();
            realTransController.setList(apartTransactionDTOS);
            realTransController.setStream(in, out);

            Stage primaryStage = (Stage) SelectApart.getScene().getWindow();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        }
    }


    // 아파트 추천
    public ArrayList<ApartTransactionDTO> recommendAparts(ApartTransactionDTO apartTransactionDTO) throws IOException {
        System.out.println(in);
        System.out.println(out);

        Packet sendPt = new Packet(Protocol.TYPE_REQUEST, Protocol.APART_RECOMMENDATION);
        sendPt.writeObject(out, apartTransactionDTO);

        Packet recvPt = new Packet();
        recvPt.read(in);

        if (recvPt.getCode() == Protocol.SUCCESS) {
            ArrayList<ApartTransactionDTO> dtos = new ArrayList<>();;
            ArrayList<Object> recvPtArrayList = recvPt.getArrayList();

            if (recvPtArrayList.size() != 0) {
                for (Object dto : recvPtArrayList) {
                    dtos.add((ApartTransactionDTO) dto);
                }
                return dtos;
            }
        }
        return null;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        TreeItem<String> root = new TreeItem<>("도/시군구 선택");
        TreeItem<String> gangwon, gyeonggi, gyeongnam, gyeongbuk, gwangju, daegu,
                daejeon, busan, seoul, sejong, ulsan, incheon, jeonnam, jeonbuk,
                jeju, chungnam, chungbuk;

        gangwon = new TreeItem<>("강원도");
        gyeonggi = new TreeItem<>("경기도");
        gyeongnam = new TreeItem<>("경상남도");
        gyeongbuk = new TreeItem<>("경상북도");
        gwangju = new TreeItem<>("광주광역시");
        daegu = new TreeItem<>("대구광역시");
        daejeon = new TreeItem<>("대전광역시");
        busan = new TreeItem<>("부산광역시");
        seoul = new TreeItem<>("서울특별시");
        sejong = new TreeItem<>("세종특별자치시");
        ulsan = new TreeItem<>("울산광역시");
        incheon = new TreeItem<>("인천광역시");
        jeonnam = new TreeItem<>("전라남도");
        jeonbuk = new TreeItem<>("전라북도");
        jeju = new TreeItem<>("제주특별자치도");
        chungnam = new TreeItem<>("충청남도");
        chungbuk = new TreeItem<>("충청북도");
        root.getChildren().addAll(gangwon, gyeonggi, gyeongnam, gyeongbuk, gwangju, daegu, daejeon, busan, seoul,
                sejong, ulsan, incheon, jeonnam, jeonbuk, jeju, chungnam, chungbuk);

        // 강원
        TreeItem<String> gangwon1 = new TreeItem<>("고성군");
        TreeItem<String> gangwon2 = new TreeItem<>("동해시");
        TreeItem<String> gangwon3 = new TreeItem<>("삼척시");
        TreeItem<String> gangwon4 = new TreeItem<>("속초시");
        TreeItem<String> gangwon5 = new TreeItem<>("양구군");
        TreeItem<String> gangwon6 = new TreeItem<>("양양군");
        TreeItem<String> gangwon7 = new TreeItem<>("영월군");
        TreeItem<String> gangwon8 = new TreeItem<>("원주시");
        TreeItem<String> gangwon9 = new TreeItem<>("인제군");
        TreeItem<String> gangwon10 = new TreeItem<>("정선군");
        TreeItem<String> gangwon11 = new TreeItem<>("철원군");
        TreeItem<String> gangwon12 = new TreeItem<>("춘천시");
        TreeItem<String> gangwon13 = new TreeItem<>("태백시");
        TreeItem<String> gangwon14 = new TreeItem<>("평창군");
        TreeItem<String> gangwon15 = new TreeItem<>("홍천군");
        TreeItem<String> gangwon16 = new TreeItem<>("횡성군");
        gangwon.getChildren().addAll(gangwon1, gangwon2, gangwon3, gangwon4, gangwon5, gangwon6, gangwon7, gangwon8,
                gangwon9, gangwon10, gangwon11, gangwon12, gangwon13, gangwon14, gangwon15, gangwon16);

        // 경기
        TreeItem<String> gyeonggi1 = new TreeItem<>("가평군");
        TreeItem<String> gyeonggi2 = new TreeItem<>("고양덕양구");
        TreeItem<String> gyeonggi3 = new TreeItem<>("고양일산동구");
        TreeItem<String> gyeonggi4 = new TreeItem<>("고양일산서구");
        TreeItem<String> gyeonggi5 = new TreeItem<>("과천시");
        TreeItem<String> gyeonggi6 = new TreeItem<>("광명시");
        TreeItem<String> gyeonggi7 = new TreeItem<>("광주시");
        TreeItem<String> gyeonggi8 = new TreeItem<>("구리시");
        TreeItem<String> gyeonggi9 = new TreeItem<>("군포시");
        TreeItem<String> gyeonggi10 = new TreeItem<>("김포시");
        TreeItem<String> gyeonggi11 = new TreeItem<>("남양주시");
        TreeItem<String> gyeonggi12 = new TreeItem<>("동두천시");
        TreeItem<String> gyeonggi13 = new TreeItem<>("부천시");
        TreeItem<String> gyeonggi14 = new TreeItem<>("성남분당구");
        TreeItem<String> gyeonggi15 = new TreeItem<>("성남수정구");
        TreeItem<String> gyeonggi16 = new TreeItem<>("성남중원구");
        TreeItem<String> gyeonggi17 = new TreeItem<>("수원권선구");
        TreeItem<String> gyeonggi18 = new TreeItem<>("수원영통구");
        TreeItem<String> gyeonggi19 = new TreeItem<>("수원장안구");
        TreeItem<String> gyeonggi20 = new TreeItem<>("수원팔달구");
        TreeItem<String> gyeonggi21 = new TreeItem<>("시흥시");
        TreeItem<String> gyeonggi22 = new TreeItem<>("안산단원구");
        TreeItem<String> gyeonggi23 = new TreeItem<>("안산상록구");
        TreeItem<String> gyeonggi24 = new TreeItem<>("안양동안구");
        TreeItem<String> gyeonggi25 = new TreeItem<>("안양만안구");
        TreeItem<String> gyeonggi26 = new TreeItem<>("양주시");
        TreeItem<String> gyeonggi27 = new TreeItem<>("양평군");
        TreeItem<String> gyeonggi28 = new TreeItem<>("여주시");
        TreeItem<String> gyeonggi29 = new TreeItem<>("연천군");
        TreeItem<String> gyeonggi30 = new TreeItem<>("오산시");
        TreeItem<String> gyeonggi31 = new TreeItem<>("용인기흥구");
        TreeItem<String> gyeonggi32 = new TreeItem<>("용인수지구");
        TreeItem<String> gyeonggi33 = new TreeItem<>("용인처인구");
        TreeItem<String> gyeonggi34 = new TreeItem<>("의왕시");
        TreeItem<String> gyeonggi35 = new TreeItem<>("의정부시");
        TreeItem<String> gyeonggi36 = new TreeItem<>("이천시");
        TreeItem<String> gyeonggi37 = new TreeItem<>("의정부시");
        TreeItem<String> gyeonggi38 = new TreeItem<>("파주시");
        TreeItem<String> gyeonggi39 = new TreeItem<>("평택시");
        TreeItem<String> gyeonggi40 = new TreeItem<>("포천시");
        TreeItem<String> gyeonggi41 = new TreeItem<>("하남시");
        TreeItem<String> gyeonggi42 = new TreeItem<>("화성시");
        gyeonggi.getChildren().addAll(gyeonggi1, gyeonggi2, gyeonggi3, gyeonggi4, gyeonggi5, gyeonggi6, gyeonggi7, gyeonggi8,
                gyeonggi9, gyeonggi10, gyeonggi11, gyeonggi12, gyeonggi13, gyeonggi14, gyeonggi15, gyeonggi16, gyeonggi17, gyeonggi18,
                gyeonggi19, gyeonggi20, gyeonggi21, gyeonggi22, gyeonggi23, gyeonggi24, gyeonggi25, gyeonggi26, gyeonggi27, gyeonggi28,
                gyeonggi29, gyeonggi30, gyeonggi31, gyeonggi32, gyeonggi33, gyeonggi34, gyeonggi35, gyeonggi36, gyeonggi37, gyeonggi38,
                gyeonggi39, gyeonggi40, gyeonggi41, gyeonggi42);

        // 경남
        TreeItem<String> gyeongnam1 = new TreeItem<>("거제시");
        TreeItem<String> gyeongnam2 = new TreeItem<>("거창군");
        TreeItem<String> gyeongnam3 = new TreeItem<>("고성군");
        TreeItem<String> gyeongnam4 = new TreeItem<>("김해시");
        TreeItem<String> gyeongnam5 = new TreeItem<>("밀양시");
        TreeItem<String> gyeongnam6 = new TreeItem<>("사천시");
        TreeItem<String> gyeongnam7 = new TreeItem<>("산청군");
        TreeItem<String> gyeongnam8 = new TreeItem<>("양산시");
        TreeItem<String> gyeongnam9 = new TreeItem<>("의령군");
        TreeItem<String> gyeongnam10 = new TreeItem<>("진주시");
        TreeItem<String> gyeongnam11 = new TreeItem<>("창녕군");
        TreeItem<String> gyeongnam12 = new TreeItem<>("창원마산합포구");
        TreeItem<String> gyeongnam13 = new TreeItem<>("창원마산회원구");
        TreeItem<String> gyeongnam14 = new TreeItem<>("창원성산구");
        TreeItem<String> gyeongnam15 = new TreeItem<>("창원의창구");
        TreeItem<String> gyeongnam16 = new TreeItem<>("창원진해구");
        TreeItem<String> gyeongnam17 = new TreeItem<>("통영시");
        TreeItem<String> gyeongnam18 = new TreeItem<>("하동군");
        TreeItem<String> gyeongnam19 = new TreeItem<>("함안군");
        TreeItem<String> gyeongnam20 = new TreeItem<>("함양군");
        gyeongnam.getChildren().addAll(gyeongnam1, gyeongnam2, gyeongnam3, gyeongnam4, gyeongnam5, gyeongnam6, gyeongnam7, gyeongnam8,
                gyeongnam9, gyeongnam10, gyeongnam11, gyeongnam12, gyeongnam13, gyeongnam14, gyeongnam15, gyeongnam16, gyeongnam17,
                gyeongnam18, gyeongnam19, gyeongnam20);

        // 경북
        TreeItem<String> gyeongbuk1 = new TreeItem<>("경산시");
        TreeItem<String> gyeongbuk2 = new TreeItem<>("경주시");
        TreeItem<String> gyeongbuk3 = new TreeItem<>("구미시");
        TreeItem<String> gyeongbuk4 = new TreeItem<>("김천시");
        TreeItem<String> gyeongbuk5 = new TreeItem<>("문경시");
        TreeItem<String> gyeongbuk6 = new TreeItem<>("봉화군");
        TreeItem<String> gyeongbuk7 = new TreeItem<>("상주시");
        TreeItem<String> gyeongbuk8 = new TreeItem<>("안동시");
        TreeItem<String> gyeongbuk9 = new TreeItem<>("영덕군");
        TreeItem<String> gyeongbuk10 = new TreeItem<>("영주시");
        TreeItem<String> gyeongbuk11 = new TreeItem<>("영천시");
        TreeItem<String> gyeongbuk12 = new TreeItem<>("예천군");
        TreeItem<String> gyeongbuk13 = new TreeItem<>("울진군");
        TreeItem<String> gyeongbuk14 = new TreeItem<>("의성군");
        TreeItem<String> gyeongbuk15 = new TreeItem<>("청도군");
        TreeItem<String> gyeongbuk16 = new TreeItem<>("청송군");
        TreeItem<String> gyeongbuk17 = new TreeItem<>("칠곡군");
        TreeItem<String> gyeongbuk18 = new TreeItem<>("포항남구");
        TreeItem<String> gyeongbuk19 = new TreeItem<>("포항북구");

        gyeongbuk.getChildren().addAll(gyeongbuk1, gyeongbuk2, gyeongbuk3, gyeongbuk4, gyeongbuk5, gyeongbuk6, gyeongbuk7, gyeongbuk8,
                gyeongbuk9, gyeongbuk10, gyeongbuk11, gyeongbuk12, gyeongbuk13, gyeongbuk14, gyeongbuk15, gyeongbuk16, gyeongbuk17,
                gyeongbuk18, gyeongbuk19);

        // 광주
        TreeItem<String> gwangju1 = new TreeItem<>("광산구");
        TreeItem<String> gwangju2 = new TreeItem<>("남구");
        TreeItem<String> gwangju3 = new TreeItem<>("동구");
        TreeItem<String> gwangju4 = new TreeItem<>("북구");
        TreeItem<String> gwangju5 = new TreeItem<>("서구");

        gwangju.getChildren().addAll(gwangju1, gwangju2, gwangju3, gwangju4, gwangju5);

        // 대구
        TreeItem<String> daegu1 = new TreeItem<>("남구");
        TreeItem<String> daegu2 = new TreeItem<>("달서구");
        TreeItem<String> daegu3 = new TreeItem<>("달성군");
        TreeItem<String> daegu4 = new TreeItem<>("동구");
        TreeItem<String> daegu5 = new TreeItem<>("북구");
        TreeItem<String> daegu6 = new TreeItem<>("수성구");
        TreeItem<String> daegu7 = new TreeItem<>("중구");

        daegu.getChildren().addAll(daegu1, daegu2, daegu3, daegu4, daegu5, daegu6, daegu7);

        // 대전
        TreeItem<String> daejeon1 = new TreeItem<>("대덕구");
        TreeItem<String> daejeon2 = new TreeItem<>("동구");
        TreeItem<String> daejeon3 = new TreeItem<>("서구");
        TreeItem<String> daejeon4 = new TreeItem<>("유성구");
        TreeItem<String> daejeon5 = new TreeItem<>("중구");

        daejeon.getChildren().addAll(daejeon1, daejeon2, daejeon3, daejeon4, daejeon5);

        // 부산
        TreeItem<String> busan1 = new TreeItem<>("강서구");
        TreeItem<String> busan2 = new TreeItem<>("금정구");
        TreeItem<String> busan3 = new TreeItem<>("기장군");
        TreeItem<String> busan4 = new TreeItem<>("남구");
        TreeItem<String> busan5 = new TreeItem<>("동구");
        TreeItem<String> busan6 = new TreeItem<>("동래구");
        TreeItem<String> busan7 = new TreeItem<>("부산진구");
        TreeItem<String> busan8 = new TreeItem<>("북구");
        TreeItem<String> busan9 = new TreeItem<>("사상구");
        TreeItem<String> busan10 = new TreeItem<>("사하구");
        TreeItem<String> busan11 = new TreeItem<>("서구");
        TreeItem<String> busan12 = new TreeItem<>("수영구");
        TreeItem<String> busan13 = new TreeItem<>("연제구");
        TreeItem<String> busan14 = new TreeItem<>("영도구");
        TreeItem<String> busan15 = new TreeItem<>("중구");
        TreeItem<String> busan16 = new TreeItem<>("해운대구");

        busan.getChildren().addAll(busan1, busan2, busan3, busan4, busan5, busan6, busan7, busan8, busan9, busan10,
                busan11, busan12, busan13, busan14, busan15, busan16);

        // 서울
        TreeItem<String> seoul1 = new TreeItem<>("강남구");
        TreeItem<String> seoul2 = new TreeItem<>("강동구");
        TreeItem<String> seoul3 = new TreeItem<>("강북구");
        TreeItem<String> seoul4 = new TreeItem<>("강서구");
        TreeItem<String> seoul5 = new TreeItem<>("관악구");
        TreeItem<String> seoul6 = new TreeItem<>("광진구");
        TreeItem<String> seoul7 = new TreeItem<>("구로구");
        TreeItem<String> seoul8 = new TreeItem<>("금천구");
        TreeItem<String> seoul9 = new TreeItem<>("노원구");
        TreeItem<String> seoul10 = new TreeItem<>("도봉구");
        TreeItem<String> seoul11 = new TreeItem<>("동대문구");
        TreeItem<String> seoul12 = new TreeItem<>("동작구");
        TreeItem<String> seoul13 = new TreeItem<>("마포구");
        TreeItem<String> seoul14 = new TreeItem<>("서대문구");
        TreeItem<String> seoul15 = new TreeItem<>("서초구");
        TreeItem<String> seoul16 = new TreeItem<>("성동구");
        TreeItem<String> seoul17 = new TreeItem<>("성북구");
        TreeItem<String> seoul18 = new TreeItem<>("송파구");
        TreeItem<String> seoul19 = new TreeItem<>("양천구");
        TreeItem<String> seoul20 = new TreeItem<>("영등포구");
        TreeItem<String> seoul21 = new TreeItem<>("용산구");
        TreeItem<String> seoul22 = new TreeItem<>("은평구");
        TreeItem<String> seoul23 = new TreeItem<>("종로구");
        TreeItem<String> seoul24 = new TreeItem<>("중구");
        TreeItem<String> seoul25 = new TreeItem<>("중랑구");

        seoul.getChildren().addAll(seoul1, seoul2, seoul3, seoul4, seoul5, seoul6, seoul7, seoul8, seoul9, seoul10,
                seoul11, seoul12, seoul13, seoul14, seoul15, seoul16, seoul17, seoul18, seoul19, seoul20, seoul21,
                seoul22, seoul23, seoul24, seoul25);

        // 세종
        TreeItem<String> sejong1 = new TreeItem<>("고운동");
        TreeItem<String> sejong2 = new TreeItem<>("나성동");
        TreeItem<String> sejong3 = new TreeItem<>("다정동");
        TreeItem<String> sejong4 = new TreeItem<>("대평동");
        TreeItem<String> sejong5 = new TreeItem<>("도담동");
        TreeItem<String> sejong6 = new TreeItem<>("반곡동");
        TreeItem<String> sejong7 = new TreeItem<>("보람동");
        TreeItem<String> sejong8 = new TreeItem<>("부강면");
        TreeItem<String> sejong9 = new TreeItem<>("새롬동");
        TreeItem<String> sejong10 = new TreeItem<>("소담동");
        TreeItem<String> sejong11 = new TreeItem<>("아름동");
        TreeItem<String> sejong12 = new TreeItem<>("어진동");
        TreeItem<String> sejong13 = new TreeItem<>("장군면");
        TreeItem<String> sejong14 = new TreeItem<>("전의면");
        TreeItem<String> sejong15 = new TreeItem<>("조치원읍");
        TreeItem<String> sejong16 = new TreeItem<>("종촌동");
        TreeItem<String> sejong17 = new TreeItem<>("한솔동");

        sejong.getChildren().addAll(sejong1, sejong2, sejong3, sejong4, sejong5, sejong6, sejong7, sejong8, sejong9, sejong10,
                sejong11, sejong12, sejong13, sejong14, sejong15, sejong16, sejong17);

        // 울산
        TreeItem<String> ulsan1 = new TreeItem<>("남구");
        TreeItem<String> ulsan2 = new TreeItem<>("동구");
        TreeItem<String> ulsan3 = new TreeItem<>("북구");
        TreeItem<String> ulsan4 = new TreeItem<>("울주군");
        TreeItem<String> ulsan5 = new TreeItem<>("중구");

        ulsan.getChildren().addAll(ulsan1, ulsan2, ulsan3, ulsan4, ulsan5);

        // 인천
        TreeItem<String> incheon1 = new TreeItem<>("강화군");
        TreeItem<String> incheon2 = new TreeItem<>("계양구");
        TreeItem<String> incheon3 = new TreeItem<>("남동구");
        TreeItem<String> incheon4 = new TreeItem<>("동구");
        TreeItem<String> incheon5 = new TreeItem<>("미추홀구");
        TreeItem<String> incheon6 = new TreeItem<>("부평구");
        TreeItem<String> incheon7 = new TreeItem<>("서구");
        TreeItem<String> incheon8 = new TreeItem<>("연수구");
        TreeItem<String> incheon9 = new TreeItem<>("중구");

        incheon.getChildren().addAll(incheon1, incheon2, incheon3, incheon4, incheon5, incheon6, incheon7, incheon8, incheon9);

        // 전남
        TreeItem<String> jeonnam1 = new TreeItem<>("강진군");
        TreeItem<String> jeonnam2 = new TreeItem<>("고흥군");
        TreeItem<String> jeonnam3 = new TreeItem<>("곡성군");
        TreeItem<String> jeonnam4 = new TreeItem<>("광양시");
        TreeItem<String> jeonnam5 = new TreeItem<>("구례군");
        TreeItem<String> jeonnam6 = new TreeItem<>("나주시");
        TreeItem<String> jeonnam7 = new TreeItem<>("담양군");
        TreeItem<String> jeonnam8 = new TreeItem<>("목포시");
        TreeItem<String> jeonnam9 = new TreeItem<>("무안군");
        TreeItem<String> jeonnam10 = new TreeItem<>("보성군");
        TreeItem<String> jeonnam11 = new TreeItem<>("순천시");
        TreeItem<String> jeonnam12 = new TreeItem<>("여수시");
        TreeItem<String> jeonnam13 = new TreeItem<>("영광군");
        TreeItem<String> jeonnam14 = new TreeItem<>("영암군");
        TreeItem<String> jeonnam15 = new TreeItem<>("완도군");
        TreeItem<String> jeonnam16 = new TreeItem<>("장성군");
        TreeItem<String> jeonnam17 = new TreeItem<>("장흥군");
        TreeItem<String> jeonnam18 = new TreeItem<>("진도군");
        TreeItem<String> jeonnam19 = new TreeItem<>("함평군");
        TreeItem<String> jeonnam20 = new TreeItem<>("해남군");
        TreeItem<String> jeonnam21 = new TreeItem<>("화순군");

        jeonnam.getChildren().addAll(jeonnam1, jeonnam2, jeonnam3, jeonnam4, jeonnam5, jeonnam6, jeonnam7, jeonnam8, jeonnam9, jeonnam10,
                jeonnam11, jeonnam12, jeonnam13, jeonnam14, jeonnam15, jeonnam16, jeonnam17, jeonnam18, jeonnam19, jeonnam20, jeonnam21);

        // 전북
        TreeItem<String> jeonbuk1 = new TreeItem<>("고창군");
        TreeItem<String> jeonbuk2 = new TreeItem<>("군산시");
        TreeItem<String> jeonbuk3 = new TreeItem<>("김제시");
        TreeItem<String> jeonbuk4 = new TreeItem<>("남원시");
        TreeItem<String> jeonbuk5 = new TreeItem<>("무주군");
        TreeItem<String> jeonbuk6 = new TreeItem<>("부안군");
        TreeItem<String> jeonbuk7 = new TreeItem<>("순창군");
        TreeItem<String> jeonbuk8 = new TreeItem<>("완주군");
        TreeItem<String> jeonbuk9 = new TreeItem<>("익산시");
        TreeItem<String> jeonbuk10 = new TreeItem<>("임실군");
        TreeItem<String> jeonbuk11 = new TreeItem<>("전주덕진구");
        TreeItem<String> jeonbuk12 = new TreeItem<>("전주완산구");
        TreeItem<String> jeonbuk13 = new TreeItem<>("정읍시");
        TreeItem<String> jeonbuk14 = new TreeItem<>("진안군");

        jeonbuk.getChildren().addAll(jeonbuk1, jeonbuk2, jeonbuk3, jeonbuk4, jeonbuk5, jeonbuk6, jeonbuk7, jeonbuk8,
                jeonbuk9, jeonbuk10, jeonbuk11, jeonbuk12, jeonbuk13, jeonbuk14);

        // 제주
        TreeItem<String> jeju1 = new TreeItem<>("서귀포시");
        TreeItem<String> jeju2 = new TreeItem<>("제주시");

        jeju.getChildren().addAll(jeju1, jeju2);

        // 충남
        TreeItem<String> chungnam1 = new TreeItem<>("계룡시");
        TreeItem<String> chungnam2 = new TreeItem<>("공주시");
        TreeItem<String> chungnam3 = new TreeItem<>("금산군");
        TreeItem<String> chungnam4 = new TreeItem<>("논산시");
        TreeItem<String> chungnam5 = new TreeItem<>("당진시");
        TreeItem<String> chungnam6 = new TreeItem<>("보령시");
        TreeItem<String> chungnam7 = new TreeItem<>("부여군");
        TreeItem<String> chungnam8 = new TreeItem<>("서산시");
        TreeItem<String> chungnam9 = new TreeItem<>("아산시");
        TreeItem<String> chungnam10 = new TreeItem<>("예산군");
        TreeItem<String> chungnam11 = new TreeItem<>("천안동남구");
        TreeItem<String> chungnam12 = new TreeItem<>("천안서북구");
        TreeItem<String> chungnam13 = new TreeItem<>("청양군");
        TreeItem<String> chungnam14 = new TreeItem<>("태안군");
        TreeItem<String> chungnam15 = new TreeItem<>("홍성군");

        chungnam.getChildren().addAll(chungnam1, chungnam2, chungnam3, chungnam4, chungnam5, chungnam6, chungnam7,
                chungnam8, chungnam9, chungnam10, chungnam11, chungnam12, chungnam13, chungnam14, chungnam15);

        // 충북
        TreeItem<String> chungbuk1 = new TreeItem<>("괴산군");
        TreeItem<String> chungbuk2 = new TreeItem<>("단양군");
        TreeItem<String> chungbuk3 = new TreeItem<>("보은군");
        TreeItem<String> chungbuk4 = new TreeItem<>("영동군");
        TreeItem<String> chungbuk5 = new TreeItem<>("옥천군");
        TreeItem<String> chungbuk6 = new TreeItem<>("음성군");
        TreeItem<String> chungbuk7 = new TreeItem<>("제천시");
        TreeItem<String> chungbuk8 = new TreeItem<>("증평군");
        TreeItem<String> chungbuk9 = new TreeItem<>("진천군");
        TreeItem<String> chungbuk10 = new TreeItem<>("청주상당구");
        TreeItem<String> chungbuk11 = new TreeItem<>("청주서원구");
        TreeItem<String> chungbuk12 = new TreeItem<>("청주청원구");
        TreeItem<String> chungbuk13 = new TreeItem<>("청주흥덕구");
        TreeItem<String> chungbuk14 = new TreeItem<>("충주시");

        chungbuk.getChildren().addAll(chungbuk1, chungbuk2, chungbuk3, chungbuk4, chungbuk5, chungbuk6, chungbuk7,
                chungbuk8, chungbuk9, chungbuk10, chungbuk11, chungbuk12, chungbuk13, chungbuk14);

        treeView.setRoot(root);


        String[] strArrList1 = {"1970", "1980", "1990", "2000", "2010", "2020"};
        ObservableList<String> fxComboNameList1 = FXCollections.observableArrayList(strArrList1);
        buildyear.setItems(fxComboNameList1);

        String[] strArrList2 = {"10000", "50000", "100000", "200000", "300000", "400000", "500000", "600000", "700000"};
        ObservableList<String> fxComboNameList2 = FXCollections.observableArrayList(strArrList2);
        price.setItems(fxComboNameList2);

        String[] strArrList3 = {"10", "20", "30", "40", "50"};
        ObservableList<String> fxComboNameList3 = FXCollections.observableArrayList(strArrList3);
        floor.setItems(fxComboNameList3);

        String[] strArrList4 = {"UP", "DOWN"};
        ObservableList<String> fxComboNameList4 = FXCollections.observableArrayList(strArrList4);
        buildYearUD.setItems(fxComboNameList4);

        String[] strArrList5 = {"UP", "DOWN"};
        ObservableList<String> fxComboNameList5 = FXCollections.observableArrayList(strArrList5);
        priceUD.setItems(fxComboNameList5);

        String[] strArrList6 = {"UP", "DOWN"};
        ObservableList<String> fxComboNameList6 = FXCollections.observableArrayList(strArrList6);
        floorUD.setItems(fxComboNameList6);

    }


    public void setStream(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void back_Clicked(ActionEvent event) throws IOException {
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("userView.fxml"));
        Parent backRoot = backLoader.load();

        UserController userController = backLoader.<UserController>getController();
        userController.setStream(in, out);

        Stage primaryStage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(backRoot);
        primaryStage.setScene(scene);
    }
}

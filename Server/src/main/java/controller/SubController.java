package controller;

import dto.*;
import network.Packet;
import network.Protocol;
import persistence.domain.User;
import service.ApartPriceIndicesService;
import service.ApartTransactionService;
import service.FavoritesService;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SubController {

    private InputStream in;
    private OutputStream out;
    private UserService userService;
    private ApartTransactionService apartTransactionService;
    private ApartPriceIndicesService apartPriceIndicesService;
    private FavoritesService favoritesService;
    private User myUser;


    public SubController(InputStream in, OutputStream out,
                         UserService userService,
                         ApartTransactionService apartTransactionService,
                         ApartPriceIndicesService apartPriceIndicesService,
                         FavoritesService favoritesService) {
        this.in = in;
        this.out = out;
        this.userService = userService;
        this.apartTransactionService = apartTransactionService;
        this.apartPriceIndicesService = apartPriceIndicesService;
        this.favoritesService = favoritesService;
    }

    public OutputStream getOut() {
        return out;
    }

    public void handler(Packet recvPt)  {
        switch (recvPt.getType()) {
            case Protocol.TYPE_REQUEST:
                try {
                    request(recvPt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void request(Packet recvPt) throws Exception {
        System.out.println(recvPt.getCode());
        switch (recvPt.getCode()) {
            case Protocol.APART_RECOMMENDATION:   // 아파트 추천
                recommendAparts(recvPt);
                break;
            case Protocol.INDICES_GRAPH:          // 아파트 매매 지수 그래프
                showIndiciesGraph(recvPt);
                break;
            case Protocol.SIGN_UP:                // 회원가입
                signUp(recvPt);
                break;
            case Protocol.USERINFO_UPDATE:        // 사용자 정보 수정
                updateUser(recvPt);
                break;
            case Protocol.USERINFO_READ:          // 사용자 정보 조회
                retrieveUser(recvPt);
                break;
            case Protocol.WITHDRAWAL:             // 회원탈퇴
                deleteUser(recvPt);
                break;
            case Protocol.LOGIN:                  // 로그인
                login(recvPt);
                break;
            case Protocol.LOGOUT:                 // 로그아웃
                logout();
                break;
            case Protocol.FAVORITES_CREATE:       // 즐겨찾기
                createFavorites(recvPt);
                break;
            case Protocol.FAVORITES_DELETE:       // 즐겨찾기 삭제
                deleteFavorites(recvPt);
                break;
            case Protocol.FAVORITES_READ:         // 즐겨찾기 조회
                readFavorites(recvPt);
                break;

        }
    }



    private void recommendAparts(Packet recvPt) throws Exception{
        ApartTransactionDTO reqDTO = (ApartTransactionDTO) recvPt.getArrayList().get(0);
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        ArrayList<ApartTransactionDTO> list = apartTransactionService.readApartTransaction(reqDTO);
        ArrayList<Object> objectList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            objectList.add(list.get(i));
        }
        if(objectList == null) {
            sendPt.setCode(Protocol.FAIL);
            sendPt.write(out);
        }
        else {
            sendPt.setCode(Protocol.SUCCESS);
            sendPt.writeObjects(out, objectList);
        }
    }



    private void showIndiciesGraph(Packet recvPt) throws Exception {
        ApartPriceIndicesDTO reqDTO = (ApartPriceIndicesDTO) recvPt.getArrayList().get(0);
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        ArrayList<ApartPriceIndicesDTO> list = apartPriceIndicesService.selectByDateAndRegion(reqDTO);

        ArrayList<Object> objectList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            objectList.add(list.get(i));
        }
        if(objectList.size() == 0) {
            sendPt.setCode(Protocol.FAIL);
            sendPt.write(out);
        }
        else {
            sendPt.setCode(Protocol.SUCCESS);
            sendPt.writeObjects(out, objectList);
        }
    }

    // 회원가입
    private void signUp(Packet recvPt) throws Exception{
        UserDTO reqDTO = (UserDTO) recvPt.getArrayList().get(0);
        System.out.println(reqDTO);
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            userService.createUser(reqDTO);
            sendPt.setCode(Protocol.SUCCESS);
            sendPt.write(out);

        } catch (Exception e) {
            e.printStackTrace();
            sendPt.setCode(Protocol.FAIL);
            sendPt.write(out);
        }
    }

    // 로그인
    private void login(Packet recvPt) throws Exception {
        UserDTO reqDTO = (UserDTO) recvPt.getArrayList().get(0);
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        UserDTO userDTO = userService.login(reqDTO);

        if(userDTO == null) {
            sendPt.setCode(Protocol.FAIL);
            sendPt.write(out);
        }

        else {
            myUser = User.builder()
                    .id(userDTO.getId())
                    .password(userDTO.getPassword())
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .build();

            sendPt.setCode(Protocol.SUCCESS);
            sendPt.writeObject(out, userDTO);
        }
    }

    // 로그아웃
    private void logout() {
        myUser = null;
    }

    // 사용자 정보 수정
    private void updateUser(Packet recvPt) throws Exception {
        UserDTO reqDTO = (UserDTO) recvPt.getArrayList().get(0);
        reqDTO.setId(myUser.getId());
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            userService.updateUser(reqDTO);
            UserDTO user = userService.retrieveUser(myUser.getId());
            myUser = User.builder()
                    .password(user.getPassword())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();

            sendPt.setCode(Protocol.SUCCESS);
            sendPt.write(out);

        } catch(Exception e) {
            sendPt.write(out);

            throw e;
        }
    }

    // 사용자 정보 조회
    public void retrieveUser(Packet recvPt) throws Exception{
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            UserDTO userDTO = userService.retrieveUser(myUser.getId());

            if(userDTO == null) {
                sendPt.write(out);

            }
            else {
                sendPt.setCode(Protocol.SUCCESS);
                sendPt.writeObject(out, userDTO);
            }

        } catch (Exception e) {
            sendPt.write(out);

            throw e;
        }
    }


    private void deleteUser(Packet recvPt) throws Exception{
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            userService.deleteUser(myUser.getId());
            sendPt.setCode(Protocol.SUCCESS);
            sendPt.write(out);
            myUser = null;

        } catch (Exception e) {
            sendPt.write(out);

        }
    }


    private void createFavorites(Packet recvPt) throws Exception{
        FavoritesDTO reqDTO = (FavoritesDTO) recvPt.getArrayList().get(0);
        reqDTO.setUserId(myUser.getId());
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            favoritesService.create(reqDTO);
            sendPt.setCode(Protocol.SUCCESS);
            sendPt.write(out);
            
        } catch (Exception e) {
            sendPt.write(out);

        }
    }

    private void deleteFavorites(Packet recvPt) throws Exception{
        FavoritesDTO reqDTO = (FavoritesDTO) recvPt.getArrayList().get(0);
        reqDTO.setUserId(myUser.getId());
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            favoritesService.delete(reqDTO);
            sendPt.setCode(Protocol.SUCCESS);
            sendPt.write(out);

        } catch (Exception e) {
            sendPt.write(out);

        }
    }

    private void readFavorites(Packet recvPt) throws Exception{
        Packet sendPt = new Packet(Protocol.TYPE_RESPONSE);

        try {
            System.out.println(myUser.getId());
            ArrayList<ApartTransactionDTO> list = favoritesService.selectAll(myUser.getId());

            ArrayList<Object> objectList = new ArrayList<>();
            for (int i   = 0; i < list.size(); i++) {
                objectList.add(list.get(i));
            }
            if(objectList == null) {
                sendPt.write(out);
            }
            else {
                System.out.println("성공");

                sendPt.setCode(Protocol.SUCCESS);
                sendPt.writeObjects(out, objectList);
            }
        } catch (Exception e) {
            sendPt.write(out);

        }
    }
}

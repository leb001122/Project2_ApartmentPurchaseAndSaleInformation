package controller;

import network.Packet;
import network.Protocol;
import network.Server;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import service.ApartPriceIndicesService;
import service.ApartTransactionService;
import service.FavoritesService;
import service.UserService;

import java.io.*;
import java.net.Socket;

public class MainController extends Thread{

    private SubController subController;

    private int clientID;
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private boolean running;

    private final UserService userService;
    private final ApartPriceIndicesService apartPriceIndicesService;
    private final ApartTransactionService apartTransactionService;
    private final FavoritesService favoritesService;
    private final SqlSessionFactory sqlSessionFactory;

    public MainController(Socket socket){
        this.socket = socket;
        clientID = socket.getPort();
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            System.out.println(out);

        } catch (IOException e) {
            e.printStackTrace();
        }

        sqlSessionFactory =  MyBatisConnectionFactory.getSqlSessionFactory();
        userService = new UserService(sqlSessionFactory);
        apartPriceIndicesService = new ApartPriceIndicesService(sqlSessionFactory);
        apartTransactionService = new ApartTransactionService(sqlSessionFactory);
        favoritesService = new FavoritesService(sqlSessionFactory);
        subController = new SubController(in, out, userService,
                apartTransactionService, apartPriceIndicesService, favoritesService);
    }

    @Override
    public void run() {
        running = true;
        System.out.println("main controller entry");
        while (running) {
            try {
                Packet recvPt = new Packet();
                subController.handler(recvPt.read(in));

            } catch (Exception e) {
                e.printStackTrace();
                exit(); // 스레드 종료
            }
        }
        System.out.println("스레드 종료");
    }

    public int getClientID() {
        return clientID;
    }


    // 소켓 종료 및 스레드 종료
    private void exit() {
        System.out.println("exit");
        Server.removeThread(clientID);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        running = false;
    }
}

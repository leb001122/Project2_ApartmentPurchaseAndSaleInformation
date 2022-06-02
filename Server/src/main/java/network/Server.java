package network;

import controller.MainController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;
    private static MainController clients[];
    private static int clientCount; // 연결된 클라이언트 개수

    static {
        try{
            serverSocket = new ServerSocket(7777);
            clients = new MainController[50];
            clientCount = 0;
        }catch(Exception e){
            e.getStackTrace();
        }
    }

    public void run() {
        System.out.println("Server running ...");
        while (serverSocket != null) {
            try {
                // 소켓연결
                Socket socket = serverSocket.accept();
                System.out.println("Success socket connection");
                // 스레드 생성
                addThread(socket);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    // 스레드 생성
    public synchronized void addThread(Socket socket) throws IOException {
        // 최대 스레드 개수를 넘지 않을 때만
        if (clientCount < clients.length) {
            MainController thread = new MainController(socket);
            clients[clientCount++] = thread;     // 스레드 배열에 생성한 스레드 추가
            System.out.println("Create thread : client Port : " + thread.getClientID() + ", clientCount = " + clientCount);
            thread.start();

        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
            socket.close();
        }
    }

    // clients 배열에서 해당 ID(port)를 가진 client pos 리턴
    public static int findClient(int ID) {
        for (int i = 0; i < clientCount; i++)
            if (clients[i].getClientID() == ID)
                return i;
        return -1;
    }

    // 스레드 제거
    public synchronized static void removeThread(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++)
                    clients[i - 1] = clients[i];
            clientCount--;
        }
        System.out.println("Remove thread : clientPort = " + ID + "clientCount = " + clientCount);
    }
}

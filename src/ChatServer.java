/*
- чат-сервер ожидает подключения на порту 1234
- обслуживает до 3-х подключенных клиентов
- ввод одного клиента дублируется в окна остальных клиентов
- отключение любого подключенного клиента никак не сказывается
на работе сервера или других клиентов
*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    static final int MAX_CLIENTS = 3;
    static List<ChatClient> connectedClients;

    public static void main(String[] args) throws IOException {
        connectedClients = new ArrayList<>(MAX_CLIENTS);
        // создаем серверный сокет на порту 1234
        ServerSocket server = new ServerSocket(1234);
        while (true) {
            System.out.println("Waiting...");
            // ждем клиента
            Socket s = server.accept();
            System.out.println("Client connected!");
            if (connectedClients.size() == MAX_CLIENTS) {
                System.out.println("MAX_CLIENTS (" + MAX_CLIENTS + ") reached! Connection not accepted!");
                s.close();
            } else {
                ChatClient client = new ChatClient(s);
                connectedClients.add(client);
                client.setAllClients(connectedClients);
                Thread thread = new Thread(client);
                thread.start();
            }
        }

    }
}

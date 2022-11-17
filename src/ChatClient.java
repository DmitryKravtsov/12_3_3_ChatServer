import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class ChatClient implements Runnable{
    Socket socket;
    List<ChatClient> allClients;
    String name;
    PrintStream out;

    public ChatClient(Socket socket){
        this.socket = socket;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setAllClients(List<ChatClient> allClients) {
        this.allClients = allClients;
    }

    private void sendToAll(String msg) {
        if (allClients.size() > 0) {
            for (int i=0; i < allClients.size(); i++) {
                if (allClients.get(i) != this) {
                    allClients.get(i).getOut().println(msg);
                }
            }
        }
    }

    @Override
    public void run(){
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            Scanner in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Wellcome to mountains!!");
            out.println("What is your name?");
            boolean gotName = false;
            //out.println("Hello, " + in.nextLine());  // Вариант № 1
            String input = in.nextLine();
            while (!input.equals("bye")) {
                if (!gotName){
                    gotName = true;
                    name = input;
                    sendToAll(name + " connected!");
                } else {
                    sendToAll(name + ">> " + input);
                }
                input = in.nextLine();
            }
            socket.close();
            allClients.remove(allClients.indexOf(this));
            sendToAll(name + " leaving chat!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

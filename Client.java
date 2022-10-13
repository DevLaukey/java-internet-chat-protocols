
import static java.lang.System.in;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static String userName;
    Scanner scanner = null;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Client.userName = userName;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Enter Message: ");

            scanner = new Scanner(System.in);
            if (scanner != null) {
                while (socket.isConnected()) {

                    String messageToSend = scanner.nextLine();
                    bufferedWriter.write(userName + ": " + messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        } finally {
            if (scanner != null)
                scanner.close();
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                if(scanner.hasNextLine()){
                    try {
                        while ((messageFromGroupChat = bufferedReader.readLine()) != null) {
                            System.out.println(messageFromGroupChat);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();

                        System.out.println(messageFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(in)) {
            System.out.println("Enter host Address: ");
            String host_address = scanner.nextLine();
            System.out.println("Enter your Username: ");
            String userName = scanner.nextLine();
            Socket socket = new Socket(host_address, 1234);
            Client client = new Client(socket, userName);
            client.listenForMessages();
            client.sendMessage();
        }
    }
}
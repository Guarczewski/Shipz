import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

class Host {
    final int PORT = 7777;

    public static ServerSocket serverSocket;
    public static Socket socket = null;
    public static ObjectOutputStream objectOutputStream = null;
    public static ObjectInputStream objectInputStream = null;

    Host() {

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            socket = serverSocket.accept();
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("123");
                try {
                    List<Node> tempList = (List<Node>) objectInputStream.readObject();
                    System.out.println(tempList.size());
                    for (Node tempNode : tempList) {
                        Main.enemyNodes[tempNode.cordX][tempNode.cordY].occupied = true;
                    }
                    Main.Update();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        List<Node> tempList = (List<Node>) objectInputStream.readObject();
                        System.out.println(tempList.size());
                        for (Node tempNode : tempList) {
                            Main.myNodes[tempNode.cordX][tempNode.cordY].hit = true;
                        }
                        Main.Update();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void Send(){
        try {
            objectOutputStream.reset();
            objectOutputStream.flush();
            objectOutputStream.writeUnshared(Main.nodesToSend);
            Main.Update();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}

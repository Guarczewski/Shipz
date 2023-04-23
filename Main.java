import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Host {

    final int PORT = 7777;

    Host(){
        Main.myTurn = true;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ServerSocket finalServerSocket = serverSocket;

        new Thread(() -> {

            Socket socket = null;
            ObjectOutputStream objectOutputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                socket = finalServerSocket.accept();
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (true) {
                try {

                    objectOutputStream.reset();
                    objectOutputStream.flush();
                    objectOutputStream.writeUnshared(Main.buttonsToSend);


                    List<Point> temp = (List<Point>) objectInputStream.readObject();
                    if (temp.size() > 0)
                        Main.updateMyNodes(temp);
                    // System.out.println(temp);

                    Thread.sleep(10);

                } catch (Exception ignored) {
                }
            }
        }).start();

    }
}

class Client {

    final int PORT = 7777;

    Client(){
        Main.myTurn = false;
        new Thread(() -> {
            Socket socket = null;
            ObjectOutputStream objectOutputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                socket = new Socket("localhost", PORT);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    List<Point> temp = (List<Point>) objectInputStream.readObject();
                    if (temp.size() > 0)
                        Main.updateMyNodes(temp);

                   // System.out.println(temp);
                    objectOutputStream.reset();
                    objectOutputStream.flush();
                    objectOutputStream.writeUnshared(Main.buttonsToSend);

                    Thread.sleep(100);

                } catch (Exception ignored) {
                }
            }
        }).start();

    }
}

class CButton extends JButton implements Serializable {
    int cordX, cordY;
    boolean occupied;
    boolean hitByEnemy;
    CButton(int cordX, int cordY){
        this.cordX = cordX;
        this.cordY = cordY;
    }
}

public class Main extends JFrame implements MouseListener, KeyListener {
    public static int Size = 10;
    public static List<Point> buttonsToSend = new ArrayList<>();
    static Main main;
    public static boolean rotationVer = false, oddTurn = false;
    public static boolean myTurn;
    public static int qShip = 1, tShip = 2, dShip = 3, oShip = 4;
    public static int turn = 0;
    public static CButton[][] enemyNodes;
    public static CButton[][] myNodes;

    public static CButton selectedButton;

    Main(){
        super("Shit!");
        setBounds(0, 0, 1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());



        JPanel areasHolder = new JPanel(new GridLayout(0,2));

        JPanel areaUno = new JPanel(new GridLayout(Size,Size));
        JPanel areaDuo = new JPanel(new GridLayout(Size,Size));

        enemyNodes = new CButton[Size][Size];
        myNodes = new CButton[Size][Size];

        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                enemyNodes[i][j] = new CButton(i,j);
                enemyNodes[i][j].setBackground(Color.CYAN);
                enemyNodes[i][j].addMouseListener(this);
                areaUno.add(enemyNodes[i][j]);

                myNodes[i][j] = new CButton(i,j);
                myNodes[i][j].setBackground(Color.CYAN);
                myNodes[i][j].addMouseListener(this);
                areaDuo.add(myNodes[i][j]);
            }
        }

        areasHolder.add(areaDuo);
        areasHolder.add(areaUno);

        add(areasHolder,BorderLayout.CENTER);
        areasHolder.addKeyListener(this);
        addKeyListener(this);
        requestFocus();
        setVisible(true);



    }
    public static void updateMyNodes(List<Point> pointList) {
        for (Point point : pointList)
            myNodes[point.x][point.y].hitByEnemy = true;

        Update();
    }
    public static void setShip(CButton cButton){
        if (qShip > 0) {
            if (rotationVer) {
                if (cButton.cordY + 3 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX][cButton.cordY + 1].occupied = true;
                    myNodes[cButton.cordX][cButton.cordY + 2].occupied = true;
                    myNodes[cButton.cordX][cButton.cordY + 3].occupied = true;
                    qShip--;
                }
            }
            else {
                if (cButton.cordX + 3 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX + 1][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX + 2][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX + 3][cButton.cordY].occupied = true;
                    qShip--;
                }
            }
        }
        else if (tShip > 0) {
            if (rotationVer) {
                if (cButton.cordY + 2 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX][cButton.cordY + 1].occupied = true;
                    myNodes[cButton.cordX][cButton.cordY + 2].occupied = true;
                    tShip--;
                }
            }
            else  {
                if (cButton.cordY + 2 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX + 1][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX + 2][cButton.cordY].occupied = true;
                    tShip--;
                }
            }
        }
        else if (dShip > 0) {
            if (rotationVer) {
                if (cButton.cordY + 1 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX][cButton.cordY + 1].occupied = true;
                    dShip--;
                }
            }
            else {
                if (cButton.cordY + 1 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].occupied = true;
                    myNodes[cButton.cordX + 1][cButton.cordY].occupied = true;
                    dShip--;
                }
            }
        }
        else if (oShip > 0) {
            myNodes[cButton.cordX][cButton.cordY].occupied = true;
            oShip--;
        }
        if (oShip == 0) {
            for (int i = 0; i < Size; i++) {
                for (int j = 0; j < Size; j++) {
                    myNodes[i][j].removeMouseListener(Main.main);
                }
            }
        }

    }
    public static void previewShip(CButton cButton){
        if (qShip > 0) {
            if (rotationVer) {
                if (cButton.cordY + 3 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX][cButton.cordY + 1].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX][cButton.cordY + 2].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX][cButton.cordY + 3].setBackground(Color.ORANGE);
                }
            }
            else {
                if (cButton.cordX + 3 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX + 1][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX + 2][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX + 3][cButton.cordY].setBackground(Color.ORANGE);
                }
            }
        }
        else if (tShip > 0) {
            if (rotationVer) {
                if (cButton.cordY + 2 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX][cButton.cordY + 1].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX][cButton.cordY + 2].setBackground(Color.ORANGE);
                }
            }
            else {
                if (cButton.cordX + 2 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX + 1][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX + 2][cButton.cordY].setBackground(Color.ORANGE);
                }
            }
        }
        else if (dShip > 0) {
            if (rotationVer) {
                if (cButton.cordY + 1 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX][cButton.cordY + 1].setBackground(Color.ORANGE);
                }
            }
            else {
                if (cButton.cordX + 1 < Size) {
                    myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
                    myNodes[cButton.cordX + 1][cButton.cordY].setBackground(Color.ORANGE);
                }
            }
        }
        else if (oShip > 0) {
            myNodes[cButton.cordX][cButton.cordY].setBackground(Color.ORANGE);
        }
        else if (oShip <= 0) {
            for (int i = 0; i < Size; i++) {
                for (int j = 0; j < Size; j++) {

                    myNodes[i][j].removeMouseListener(Main.main);
                }
            }
        }
    }
    public static void hitTheTarget(CButton cButton){
        buttonsToSend.add(new Point(cButton.cordX, cButton.cordY));
        enemyNodes[cButton.cordX][cButton.cordY].hitByEnemy = true;
    }
    public static void main(String[] args) {
       main = new Main();
      // new Host();
       new Client();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (oShip <= 0) {
            hitTheTarget(selectedButton);
        }
        else {
            setShip(selectedButton);
        }
        main.requestFocus();
    }

    public static void Update(){
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                if (myNodes[i][j].hitByEnemy) {
                    myNodes[i][j].setBackground(Color.BLACK);
                }
                else if (myNodes[i][j].occupied) {
                    myNodes[i][j].setBackground(Color.ORANGE);
                }
                else {
                    myNodes[i][j].setBackground(Color.CYAN);
                }

                if (enemyNodes[i][j].hitByEnemy) {
                    enemyNodes[i][j].setBackground(Color.BLACK);
                }
                else if (enemyNodes[i][j].occupied) {
                    enemyNodes[i][j].setBackground(Color.ORANGE);
                }
                else {
                    enemyNodes[i][j].setBackground(Color.CYAN);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Update();
        selectedButton = (CButton) e.getSource();

        if (turn < 2) {
            if (!oddTurn) {
                previewShip(selectedButton);
            }
        }

        main.requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        rotationVer =! rotationVer;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
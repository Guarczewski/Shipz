import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Node extends JButton implements Serializable {
    boolean occupied, hit;
    int cordX, cordY;
    public Node(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;

        occupied = false;
        hit = false;

    }
}

public class Main extends JFrame implements MouseListener, KeyListener {
    public static final int Size = 10;
    public static Main gameInstance;
    public static Node[][] myNodes;
    public static Node[][] enemyNodes;
    public static Client clientModule;
    public static Host hostModule;
    public static boolean rotationVer;
    public static int qShip = 1, tShip = 2, dShip = 3, oShip = 4;
    public static List<Node> nodesToSend = new ArrayList<>();

    Main(){
        super("Shit!");
        setBounds(0, 0, 1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myNodes = new Node[Size][Size];
        enemyNodes = new Node[Size][Size];

        JPanel areasHolder = new JPanel(new GridLayout(0,2));
        JPanel areaUno = new JPanel(new GridLayout(Size,Size));
        JPanel areaDuo = new JPanel(new GridLayout(Size,Size));

        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {

                myNodes[i][j] = new Node(i,j);
                myNodes[i][j].setBackground(Color.CYAN);
                myNodes[i][j].addMouseListener(this);
                areaDuo.add(myNodes[i][j]);

                enemyNodes[i][j] = new Node(i,j);
                enemyNodes[i][j].setBackground(Color.CYAN);
                areaUno.add(enemyNodes[i][j]);

            }
        }

        areasHolder.add(areaDuo);
        areasHolder.add(areaUno);

        addKeyListener(this);

        setContentPane(areasHolder);
        requestFocus();
        setVisible(true);
    }

    public static void main(String[] args) {
        gameInstance = new Main();
        clientModule = new Client();
      //  hostModule = new Host();

        System.out.println("Hello world!");
    }

    public static void setShip(Node selectedNode){
        if (qShip > 0) {
            if (rotationVer) {
                if (selectedNode.cordY + 3 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX][selectedNode.cordY + 1].occupied = true;
                    myNodes[selectedNode.cordX][selectedNode.cordY + 2].occupied = true;
                    myNodes[selectedNode.cordX][selectedNode.cordY + 3].occupied = true;
                    qShip--;
                }
            }
            else {
                if (selectedNode.cordX + 3 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX + 1][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX + 2][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX + 3][selectedNode.cordY].occupied = true;
                    qShip--;
                }
            }
        }
        else if (tShip > 0) {
            if (rotationVer) {
                if (selectedNode.cordY + 2 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX][selectedNode.cordY + 1].occupied = true;
                    myNodes[selectedNode.cordX][selectedNode.cordY + 2].occupied = true;
                    tShip--;
                }
            }
            else  {
                if (selectedNode.cordY + 2 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX + 1][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX + 2][selectedNode.cordY].occupied = true;
                    tShip--;
                }
            }
        }
        else if (dShip > 0) {
            if (rotationVer) {
                if (selectedNode.cordY + 1 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX][selectedNode.cordY + 1].occupied = true;
                    dShip--;
                }
            }
            else {
                if (selectedNode.cordY + 1 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
                    myNodes[selectedNode.cordX + 1][selectedNode.cordY].occupied = true;
                    dShip--;
                }
            }
        }
        else if (oShip > 0) {
            myNodes[selectedNode.cordX][selectedNode.cordY].occupied = true;
            oShip--;
        }
        if (oShip == 0) {
            for (int i = 0; i < Size; i++) {
                for (int j = 0; j < Size; j++) {
                    myNodes[i][j].removeMouseListener(Main.gameInstance);
                    enemyNodes[i][j].addMouseListener(Main.gameInstance);
                    if (myNodes[i][j].occupied)
                        nodesToSend.add(myNodes[i][j]);
                }
            }
            if (clientModule != null) {
                Client.Send();
                nodesToSend.clear();
                System.out.println("Sent Package Client");
            }
            else if (hostModule != null) {
                Host.Send();
                nodesToSend.clear();
                System.out.println("Sent Package Host");
            }
        }

    }

    public static void previewShip(Node selectedNode){
        if (qShip > 0) {
            if (rotationVer) {
                if (selectedNode.cordY + 3 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX][selectedNode.cordY + 1].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX][selectedNode.cordY + 2].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX][selectedNode.cordY + 3].setBackground(Color.ORANGE);
                }
            }
            else {
                if (selectedNode.cordX + 3 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX + 1][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX + 2][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX + 3][selectedNode.cordY].setBackground(Color.ORANGE);
                }
            }
        }
        else if (tShip > 0) {
            if (rotationVer) {
                if (selectedNode.cordY + 2 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX][selectedNode.cordY + 1].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX][selectedNode.cordY + 2].setBackground(Color.ORANGE);
                }
            }
            else {
                if (selectedNode.cordX + 2 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX + 1][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX + 2][selectedNode.cordY].setBackground(Color.ORANGE);
                }
            }
        }
        else if (dShip > 0) {
            if (rotationVer) {
                if (selectedNode.cordY + 1 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX][selectedNode.cordY + 1].setBackground(Color.ORANGE);
                }
            }
            else {
                if (selectedNode.cordX + 1 < Size) {
                    myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
                    myNodes[selectedNode.cordX + 1][selectedNode.cordY].setBackground(Color.ORANGE);
                }
            }
        }
        else if (oShip > 0) {
            myNodes[selectedNode.cordX][selectedNode.cordY].setBackground(Color.ORANGE);
        }
    }

    public static void Update(){
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                if (myNodes[i][j].hit && myNodes[i][j].occupied) {
                    myNodes[i][j].setBackground(Color.RED);
                }
                else if (myNodes[i][j].hit) {
                    myNodes[i][j].setBackground(Color.BLACK);
                }
                else if (myNodes[i][j].occupied) {
                    myNodes[i][j].setBackground(Color.ORANGE);
                }
                else {
                    myNodes[i][j].setBackground(Color.CYAN);
                }

                if (enemyNodes[i][j].hit && enemyNodes[i][j].occupied) {
                    enemyNodes[i][j].setBackground(Color.RED);
                }
                else if (enemyNodes[i][j].hit) {
                    enemyNodes[i][j].setBackground(Color.BLACK);
                }
                else if (enemyNodes[i][j].occupied) {
                   // enemyNodes[i][j].setBackground(Color.ORANGE);
                }
                else {
                    enemyNodes[i][j].setBackground(Color.CYAN);
                }
            }
        }
    }

    public static void hitTheTarget(Node selectedNode){
        nodesToSend.add(new Node(selectedNode.cordX, selectedNode.cordY));
        enemyNodes[selectedNode.cordX][selectedNode.cordY].hit = true;
        if (clientModule != null) {
            Client.Send();
            nodesToSend.clear();
            System.out.println("Sent Package Client");
        }
        else if (hostModule != null) {
            Host.Send();
            nodesToSend.clear();
            System.out.println("Sent Package Host");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Update();

        if (oShip > 0)
            setShip((Node) e.getSource());
        else
            hitTheTarget((Node) e.getSource());

        gameInstance.requestFocus();
        Update();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Update();

        if (oShip > 0)
            previewShip((Node) e.getSource());

        if (gameInstance != null)
            gameInstance.requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        rotationVer = !rotationVer;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
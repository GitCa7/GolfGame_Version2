package testing.system.framework;

import framework.main.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author martin
 */
public class MockMainMenu extends JFrame implements ActionListener {

    public static final double BALL_RADIUS = 7;

    Main main;
    ButtonGroup type;
    ButtonGroup alg;

    JLabel lastTotalValue;
    JTextField one;
    JTextField two;
    JTextField xt;
    JTextField yt;
    JTextField zt;
    JLabel strokes;
    JLabel win;
    JFrame playFrame;

    public MockMainMenu(Main main) throws IOException{
        super("Main Menu");
        this.main = main;
        JPanel buttonPanel = new JPanel();
        ImagePanel imagePanel = new ImagePanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        JButton b1 = new JButton("Start Game");
        b1.addActionListener(this);
        JButton b2 = new JButton("Course Editor");
        b2.addActionListener(this);
        JButton b3 = new JButton("Exit");
        b3.addActionListener(this);
        JButton b4 = new JButton("Editor info");
        b4.addActionListener(this);
        b1.setActionCommand("start");
        b2.setActionCommand("editor");
        b3.setActionCommand("exit");
        b4.setActionCommand("info");

        buttonPanel.add(b1);
        buttonPanel.add(b2);
        buttonPanel.add(b4);
        buttonPanel.add(b3);
        this.add(buttonPanel, BorderLayout.WEST);
        this.add(imagePanel,BorderLayout.CENTER);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(350,330);
        this.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")){
            System.exit(0);
        }
        if (e.getActionCommand().equals("editor")){
            main.launchEditor();
        }if(e.getActionCommand().equals("start")){
            String name = JOptionPane.showInputDialog("Course Name?");
            main.loadGame(name);
            main.run();
        }
        if(e.getActionCommand().equals("info")){
            JFrame infoFrame = new JFrame("Editor info");
            infoFrame.setSize(400,400);
            JPanel infoPanel = new JPanel(new GridLayout(12,1));
            JLabel l1 = new JLabel("P+click = Place Obstacle");
            infoPanel.add(l1);
            JLabel l2 = new JLabel("R+click = Rotate Obstacle");
            infoPanel.add(l2);
            JLabel l3 = new JLabel("Z+click = Remove Obstacle");
            infoPanel.add(l3);
            JLabel l8 = new JLabel("B+leftclick = Add player ball");
            infoPanel.add(l8);
            JLabel l9 = new JLabel("B+rightclick = Add bot ball");
            infoPanel.add(l9);
            JLabel l6 = new JLabel("Scroll = Change Obstacle scale");
            infoPanel.add(l6);
            JLabel l4 = new JLabel("U+Dragg = Increase Terrain Height");
            infoPanel.add(l4);
            JLabel l5 = new JLabel("J+Dragg = Decrease Terrain Height");
            infoPanel.add(l5);
            JLabel l7 = new JLabel("Dragg = Move Obstacle or Ball");
            infoPanel.add(l7);
            JLabel l10 = new JLabel("Q+click = Save Course(Name input might be behind editor window)");
            infoPanel.add(l10);
            infoFrame.add(infoPanel);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoFrame.setVisible(true);
        }
    }

}
class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel() {
        try {
            image = ImageIO.read(new File("golfcourse2.jpg"));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }


}

package framework.testing;

import Editor.Course;
import framework.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        buttonPanel.setLayout(new GridLayout(3,1));
        JButton b1 = new JButton("Start Game");
        b1.addActionListener(this);
        JButton b2 = new JButton("Course Editor");
        b2.addActionListener(this);
        JButton b3 = new JButton("Exit");
        b3.addActionListener(this);
        b1.setActionCommand("start");
        b2.setActionCommand("editor");
        b3.setActionCommand("exit");

        buttonPanel.add(b1);
        buttonPanel.add(b2);
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

package view;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;

import model.ImageState;

public class ImageProcessingGUIFrame extends JFrame implements ImageProcessingGUI {
  private final JLabel imageSlot;

  public ImageProcessingGUIFrame() {
    this.setTitle("Image Processing GUI"); // sets the title of the frame
    // exit out of the application when X button clicked
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false); // enable frame to be resizable
    this.setSize(1250, 750); // sets the x-dimension and y-dimension of the frame
    this.setLayout(null);
    // set the background of the frame
    this.getContentPane().setBackground(new Color(128, 128, 128));

    // create image part

    this.add(this.displayImage(null));

    JPanel extra = new JPanel();
    extra.setPreferredSize(new Dimension(500, 600));
    this.add(extra);

    this.setVisible(true); // makes the frame visible
  }

  private JComponent displayImage(Image img) {
    JPanel panel = new JPanel();
    ImageIcon image = new ImageIcon("res/arjun.png"); // image in the label
    Border border = BorderFactory.createLineBorder(Color.black); // border of the label

    // JLabel = a GUI display area for a string of text, an image, or both
    JLabel label = new JLabel(); // create a label
    // set vertical position of icon+text within label
//    label.setVerticalAlignment(JLabel.CENTER);
    // set horizontal position of icon+text within label
//    label.setHorizontalAlignment(JLabel.CENTER);
    label.setIcon(image);
    label.setBorder(border);

    panel.add(label);
    panel.setAutoscrolls(true);

    JScrollPane scroll = new JScrollPane(panel);
    scroll.setBounds(20, 50, 500, 600);
    return scroll;
  }
}

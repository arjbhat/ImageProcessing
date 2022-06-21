package view;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImageProcessingGUIFrame extends JFrame implements ImageProcessingGUI {
  private static final Color BACKGROUND_COLOR = new Color(128, 128, 128);
  private final JLabel imageSlot;

  private final JButton loadButton;
  private final JButton saveButton;
  private final DefaultListModel<String> imagesList;
  private final JList<String> imageSelection;
  private final JComboBox<String> commandSelector;
  private final CardLayout commandLayout;
  private final JTextField newName;
  private final JButton applyCommand;
  private final JSlider brightenSlider;
  
  public ImageProcessingGUIFrame() {
    this.setTitle("Image Processing GUI"); // sets the title of the frame
    // exit out of the application when X button clicked
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false); // enable frame to be resizable
    this.setSize(1250, 750); // sets the x-dimension and y-dimension of the frame
    this.setLayout(null);
    // set the background of the frame
    this.getContentPane().setBackground(BACKGROUND_COLOR);

    // create image part
    this.imageSlot = new JLabel();
    JScrollPane imageScroll = new JScrollPane(imageSlot);
    imageScroll.setBounds(20, 50, 500, 600);
    this.add(imageScroll);

    // load file button
    this.loadButton = new JButton("Load Image");
    loadButton.setBounds(600, 50, 100, 20);
    this.add(loadButton);

    // save file button
    this.saveButton = new JButton("Save Image");
    saveButton.setBounds(750, 50, 100, 20);
    this.add(saveButton);

    // images list
    this.imagesList = new DefaultListModel<>();
    this.imageSelection = new JList<>(imagesList);
    imageSelection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane imageListScroll = new JScrollPane(imageSelection);
    imageListScroll.setBounds(600, 200, 200, 400);
    this.add(imageListScroll);

    this.commandSelector = new JComboBox<>(new String[]{"brighten", "g", "asdfionwef"});
    commandSelector.setBounds(850, 250, 100, 50);
    this.add(commandSelector);

    // commands
    this.commandLayout = new CardLayout();
    JPanel commands = new JPanel(commandLayout);
    commands.setBackground(BACKGROUND_COLOR);
    commandSelector.addActionListener(a -> {
      commandLayout.show(commands, (String) commandSelector.getSelectedItem());
    });
    // brightness
    JPanel brightenCard = new JPanel();
    this.brightenSlider = new JSlider(JSlider.HORIZONTAL, -256, 256, 0);
    brightenSlider.setPreferredSize(new Dimension(300, 50));
    brightenSlider.setMinorTickSpacing(32);
    brightenSlider.setMajorTickSpacing(128);
    brightenSlider.setPaintTicks(true);
    brightenSlider.setPaintLabels(true);
    brightenCard.add(brightenSlider);
    JLabel brightenState = new JLabel("0");
    brightenCard.add(brightenState);
    brightenSlider.addChangeListener(e -> {
      brightenState.setText(String.valueOf(brightenSlider.getValue()));
    });
    // downscale
    JPanel downScale = new JPanel();


    commands.add(brightenCard, "brighten");
    String[] blank = {"g", "asdfionwef"};
    for (String name : blank) {
      JPanel panel = new JPanel();
      panel.setBackground(BACKGROUND_COLOR);
      commands.add(panel, name);
    }

    commands.setBounds(850, 300, 300, 150);
    this.add(commands);

    // new image name field
    JLabel nameLabel = new JLabel("New name");
    nameLabel.setBounds(850, 475, 75, 20);
    this.add(nameLabel);
    this.newName = new JTextField();
    newName.setBounds(850, 500, 75, 20);
    this.add(newName);

    // apply command button
    this.applyCommand = new JButton("Apply");
    applyCommand.setBounds(950, 500, 75, 20);
    this.add(applyCommand);

    this.setVisible(true); // makes the frame visible

    // TODO: Remove this stuff
    this.displayImage(null);
    this.addImage("test");
    this.addImage("test sadfj wegf wef wf sd fasdf asdgf asd fasd fsd fasdf");
    for (int i = 0; i < 50; i++) {
      this.addImage("test" + i);
    }
    this.setSelected("test27");
    //this.addFeatures();
  }

  public void displayImage(Image img) {
    this.imageSlot.setIcon(new ImageIcon("res/arjun.png"));
  }

  public void addImage(String imageName) {
    this.imagesList.addElement(imageName);
  }

  public void setSelected(String imageName) {
    this.imageSelection.setSelectedIndex(this.imagesList.indexOf(imageName));
  }

  public void addFeatures(Features f) {
    //TODO: WRITE COMMENT
    loadButton.addActionListener(a -> {
      final JFileChooser fileChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",
          "jpg", "jpeg", "png", "bmp", "ppm");
      fileChooser.setFileFilter(filter);
      int signal = fileChooser.showOpenDialog(this);
      if (signal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        f.load(file.getAbsolutePath());
      }
    });
    //TODO: WRITE COMMENT
    saveButton.addActionListener(a -> {
      final JFileChooser fileChooser = new JFileChooser(".");
      int signal = fileChooser.showSaveDialog(this);
      if (signal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        f.save(file.getAbsolutePath());
      }
    });
    //TODO: WRITE COMMENT
    imageSelection.addListSelectionListener(e -> {
      f.select(imageSelection.getSelectedValue());
    });
  }
}
